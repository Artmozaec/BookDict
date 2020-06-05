package userinterface;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import message.Message;
import filebrowser.*;
import message.QuestionListener;
import message.Question;
import bookmarksinterface.*;
import decryptoptions.*;
import programdirection.ScreenManager;
import findtextdialog.*;

//�� ����� ������ ��������� � ������������ �������� �������� ����������������� ����������. ��� �� �� ���������� ���������� ������ ������� ��� �.
//��� ������ ������ ����� ����� ������ ���� �� ��������� ��������� ���������� ������ � ����. ��� ����� ���������� ������� ������ �� ������������ ������ ������ ������ ����� �������� � ���� ������



public final class UserInterface implements TextEditorListener, FileBrowserListener{
private static UserInterface userInterfaceInstance = new UserInterface();

private UserInterfaceListener userInterfaceListener;
private ScreenManager screenManager;


private UserInterface(){
	screenManager = ScreenManager.getInstance();
}

public void patchChoosed(String directory){
	userInterfaceListener.patchChoosed(directory);
}

public void showQuestion(String questionText, QuestionListener questionListener){
	Question question = new Question(questionText, questionListener);
	
	screenManager.showCurrent(question);
}


public void changeUserInterfaceListener(UserInterfaceListener inListener){
	userInterfaceListener = inListener;
}

public static UserInterface getInstance(){
	return userInterfaceInstance;
}

public void setLight(int newLight){
	userInterfaceListener.setLight(newLight);
}

public void showLightSelector(){
	LightSelector lightSelector = new LightSelector(this);
	lightSelector.selectCurrentLightValue(userInterfaceListener.getLightValue());
	screenManager.showCurrent(lightSelector);
}
//////////////////////////////////////////����� ���������////////////////////////////////

public void showErrorMessage(String message){
	Message mes = new Message(this, message, Message.MODE_ERROR);

	//���������� ���������
	screenManager.showCurrent(mes);
}

public void showInfoMessage(String message){
	Message mes = new Message(this, message, Message.MODE_INFO);

	//���������� ���������
	screenManager.showCurrent(mes);
}
////////////////////////////////////////////////////////////////////////////////////////

public void showLinesFoldingAreaSelector(){
	LinesFoldingAreaSelector linesFoldingAreaSelector = new LinesFoldingAreaSelector(this);
	screenManager.showCurrent(linesFoldingAreaSelector);
}


public void showFileBrowser(FileBrowserListener fileBrowserListener){
	FileBrowser fileBrowser;
	fileBrowser = new FileBrowser(fileBrowserListener);
	screenManager.showCurrent(fileBrowser);
}


public void showFolderBrowser(){
	showFileBrowser(this);
	/*
	FolderBrowser folderBrowser;
	//�������� ������� ���� � ������� ������� �����
	String beginPatch = userInterfaceListener.getWorkFolder();
	folderBrowser = new FolderBrowser(this, beginPatch);
	screenManager.showCurrent(folderBrowser);
	*/
}

public void showGoToPage(){
	GoToPage goToPage = new GoToPage(this);
	screenManager.showCurrent(goToPage);

}

public void exitProgram(){
	userInterfaceListener.exitProgram();
}

public void goToBackScreen(){
	screenManager.restoreSavedSvreen();
}

public void goToPage(int page){
	userInterfaceListener.goToPage(page);
	goToBackScreen();
	goToBackScreen();
}

public void setLineFoldingMode(int areaMode){
	userInterfaceListener.setLineFoldingMode(areaMode);
	goToBackScreen();
}

public void showMenu(){
	Menu currentMenu;
	
	//������� ����
	currentMenu = new Menu(this);

	//���������� ���� �� ������
	screenManager.showCurrent(currentMenu);
}



public void showBookmarksListEditor(){
	BookmarkListEditor bookmarkListEditor = new BookmarkListEditor(this, userInterfaceListener.getBookmarkList());
	screenManager.showCurrent(bookmarkListEditor);
}

public void goToBookmark(String bookmarkName){
	//goToBackScreen();
	//goToBackScreen();
	//goToBackScreen();
	userInterfaceListener.goToBookmark(bookmarkName);
}

public void showTextEditor(TextEditorListener textEditorListener, String beginText){
	TextEditor textEditor = new TextEditor(this, textEditorListener, beginText);
	screenManager.showCurrent(textEditor);
}

public void showDecryptOptionsSelectDialog(DecryptOptionsListener decryptOptionsListener){
	DecryptOptions decryptOptions = new DecryptOptions(decryptOptionsListener, this);
	screenManager.showCurrent(decryptOptions);
}


public void addCurrentViewToBookmark(){
	//������ ��� ��������� �����
	String name = userInterfaceListener.getBookName();
	int pageNumber = userInterfaceListener.getPageNumber();
	
	BookmarkList bookmarkList = userInterfaceListener.getBookmarkList();
	String newName = bookmarkList.generateBookmarkName(name, pageNumber);
	
	showTextEditor(this, newName);
}

public void rewriteBookmarkToCurrentView(String name){
	userInterfaceListener.rewriteBookmarkToCurrentView(name);
}


public void textInputIsFinished(String text){
	BookmarkList bookmarkList = userInterfaceListener.getBookmarkList();
	if (bookmarkList.isCorrectName(text)){
		userInterfaceListener.addCurrentViewToBookmark(text);
		goToBackScreen();
		goToBackScreen();
		goToBackScreen();
	} else {
		showInfoMessage("����� ��� ��� ����");
	}
}

public void restoreCurrentView(){
	userInterfaceListener.restoreCurrentView();
	goToBackScreen();
}

public void ShowFindTextDialog(String text){
	FindTextDialog findTextDialog = new FindTextDialog((FindTextDialogListener)userInterfaceListener, text);
	screenManager.showCurrent(findTextDialog);
}

}
