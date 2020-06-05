package programdirection;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import userinterface.UserInterface;
import userinterface.UserInterfaceListener;
import bookpages.BookPages;
import bookviewer.BookView;
import bookviewer.BookViewer;
import bookmarks.BookmarkManager;
import java.util.Vector;
import bookmarksinterface.BookmarkList;
import rms.ProgramParametrs;
import findtextdialog.FindTextDialogListener;



import textviewer.TextViewer;

import dictionary.Dictionary;

public class ProgramDirection implements UserInterfaceListener, FindTextDialogListener{
private Display display;
private UserInterface userInterface;
private Vector testVector;
private BookmarkManager bookmarkManager;
private ProgramParametrs programParametrs;
//����������� ����
private BookViewer bookViewer;
private TextViewer textViewer;

private BookImage bookImage;
//private ProgramParameters programParameters;
private Light light;

//������� �������
private Dictionary dictionary;

//��� ��������� �������
private TextViewer dictionaryTextViewer;

public ProgramDirection(BookImage inMidl){
	bookImage = inMidl;
	//������� ��������
	bookmarkManager = new BookmarkManager();
	
	programParametrs = ProgramParametrs.getInstance();
	
	//System.out.println("��������� - " +programParametrs.getLightValue());
	
	//�����������
	setLight(programParametrs.getLightValue());

	//System.out.println("�������� - " +programParametrs.getBookmarkName());
	
	//�������� �������
	display = bookImage.getDisplay();
	//�������������� �������� ������
	ScreenManager.getInstance().setDisplay(display);
	
	//�������������� ��������� ������������
	userInterface = UserInterface.getInstance();
	
	//���������� ������� ���������� �����!
	userInterface.changeUserInterfaceListener(this);	

	//����������� ����
	//bookViewer = new BookViewer(this);
	textViewer = new TextViewer(this);
	dictionaryTextViewer = new TextViewer(this);
	
	//������ �������
	dictionary = new Dictionary("file:///E:/mueller/");
	//dictionary = new Dictionary("file:///root1/1/mueller/");
	
	//��������� �� ���������� ��������
	goToBookmark(programParametrs.getBookmarkName());
	
	//������� ������� � �������
	ScreenManager.getInstance().showCurrent(textViewer.getDisplayable());
	//userInterface.showFolderBrowser();
}

public void setLight(int newLight){
	light = new Light(newLight);
}

public int getLightValue(){
	return Light.getLight();
}

public void setLineFoldingMode(int areaMode){
	bookViewer.setLineFoldingMode(areaMode);
}

public String getWorkFolder(){
	return bookViewer.getCurrentBookView().getPatch();
}

public String getBookName(){
	return bookViewer.getCurrentBookView().getBookName();
}

public int getPageNumber(){
	return ((BookView)bookViewer.getCurrentBookView()).getPageNumber();
}

//���������� ������ ���������� � ���� ������
public void patchChoosed(String patch){
	viewBook(patch);
}

public void goToPage(int page){
	bookViewer.goToPage(page);
}


//�������� �����
private void viewBook(String bookPatch){
	//System.out.println(bookPatch);
	//textViewer.showText(bookPatch, 0);
	//ScreenManager.getInstance().showCurrent(textViewer.getDisplayable());

	//������ ������ ����, ����� ��������, � ��������� �������� ��������������� �� ���������
	BookView bookView = new BookView(bookPatch);
	
	String bookmarkName = bookmarkManager.generateBookmarkName(bookView.getBookName(), 0);
	
	//��������� ��������
	bookmarkManager.addBookmark(bookView, bookmarkName);
	
	//������������� �� ��� �������
	bookmarkManager.setCurrentBookmarkName(bookmarkName);
	
	//��������� ��������� ��������� � ���������
	textViewer.saveChangedView();
	
	//���a������
	textViewer.showText(bookView);
	
	//� ��������� ����� ���, ��� bookViewer ���������� ���� ������ ������� � display
	//����� ��������� ������������ ������������ ����� ��������, ������ ������� ����� ����������
	ScreenManager.getInstance().showCurrent(textViewer.getDisplayable());
}

private void saveParameters(){
	//��������� 
	programParametrs.setLightValue(Light.getLight());
	
	//��������
	programParametrs.setBookmarkName(bookmarkManager.getCurrentBookmarkName());
	
	//������� �����������
	textViewer.saveChangedView();
	
	programParametrs.saveParametrs();
	
	//��������, ���� ���� ���������
	bookmarkManager.saveBookmarks();
}

public void exitProgram(){
	//��������� ���������
	saveParameters();

	//����-�� �����	
	bookImage.destroyApp(true);
}

public void addCurrentViewToBookmark(String name){
	bookmarkManager.addBookmark(bookViewer.getCurrentBookView(), name);
}


public void rewriteBookmarkToCurrentView(String name){
	bookmarkManager.rewriteBookmarkToCurrentView(bookViewer.getCurrentBookView(), name);
}

//////////////////////������� ������������///////////////////////
public void showBookmarksListEditor(){
	userInterface.showBookmarksListEditor();
}


public void showMenu(){
	userInterface.showMenu();
}

public void showLinesFoldingAreaSelector(){
	userInterface.showLinesFoldingAreaSelector();
}



public void nextBookmark(){
	BookView nextView = bookmarkManager.nextBookmark();
	if (nextView != null){
		//��������� ��������� ��������� � ���������
		bookViewer.saveChangedView();
		bookViewer.showBook(nextView);
	} else {
		userInterface.showInfoMessage("�������� ���!");
	}
}

public BookmarkList getBookmarkList(){
	return (BookmarkList)bookmarkManager;
}

public void goToBookmark(String name){
	BookView bookView = bookmarkManager.getViewByBookmarkName(name);
	
	//���� ����� �������� � ����� ������ �� �������, ������������� ���������
	if (bookView == null){
		bookView = bookmarkManager.getViewByBookmarkName(bookmarkManager.RESERVED_BOOKMARK_NAME);
	}
	
	//������������� ������� ��������
	bookmarkManager.setCurrentBookmarkName(name);
	
	//bookViewer.saveChangedView();
	textViewer.saveChangedView();
	//bookViewer.showBook(bookView);
	textViewer.showText(bookView);
	
	//� ��������� ����� ���, ��� bookViewer ���������� ���� ������ ������� � display
	//����� ��������� ������������ ������������ ����� ��������, ������ ������� ����� ����������
	ScreenManager.getInstance().showCurrent(textViewer.getDisplayable());
}

public void restoreCurrentView(){
	bookViewer.restoreCurrentView();
}

public void findWordInDictionary(String findWord){
	int positionInFile = dictionary.findWord(findWord);
	
	if (positionInFile == Dictionary.NOT_FOUND){
		userInterface.showErrorMessage("����� �� �������!");
		return;
	}
	BookView bookView = new BookView(dictionary.getDictionaryPatch());
	bookView.setPageNumber(positionInFile);
	//String dictPatch = dictionary.getDictionaryPatch();
	
	dictionaryTextViewer.showText(bookView);
	ScreenManager.getInstance().showCurrent(dictionaryTextViewer.getDisplayable());
	
	//System.out.println("dictPatch = "+dictPatch);
	//System.out.println("������ = "+positionInFile);
}

}