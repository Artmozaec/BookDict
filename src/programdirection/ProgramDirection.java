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
//Просмотрщик книг
private BookViewer bookViewer;
private TextViewer textViewer;

private BookImage bookImage;
//private ProgramParameters programParameters;
private Light light;

//Текущий словарь
private Dictionary dictionary;

//Для просмотра словаря
private TextViewer dictionaryTextViewer;

public ProgramDirection(BookImage inMidl){
	bookImage = inMidl;
	//Менджер закладок
	bookmarkManager = new BookmarkManager();
	
	programParametrs = ProgramParametrs.getInstance();
	
	//System.out.println("подсветка - " +programParametrs.getLightValue());
	
	//Подсветочка
	setLight(programParametrs.getLightValue());

	//System.out.println("закладка - " +programParametrs.getBookmarkName());
	
	//Получаем дисплей
	display = bookImage.getDisplay();
	//Инициализируем менеджер экрана
	ScreenManager.getInstance().setDisplay(display);
	
	//инициализируем интерфейс пользователя
	userInterface = UserInterface.getInstance();
	
	//Обработчик событий интерфейса здесь!
	userInterface.changeUserInterfaceListener(this);	

	//Просмотрщик книг
	//bookViewer = new BookViewer(this);
	textViewer = new TextViewer(this);
	dictionaryTextViewer = new TextViewer(this);
	
	//Создаём словарь
	dictionary = new Dictionary("file:///E:/mueller/");
	//dictionary = new Dictionary("file:///root1/1/mueller/");
	
	//Переходим на сохранённую заклкдку
	goToBookmark(programParametrs.getBookmarkName());
	
	//Передаём паинтер в дисплей
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

//Обработчик выбора директории в окне выбора
public void patchChoosed(String patch){
	viewBook(patch);
}

public void goToPage(int page){
	bookViewer.goToPage(page);
}


//Открытие книги
private void viewBook(String bookPatch){
	//System.out.println(bookPatch);
	//textViewer.showText(bookPatch, 0);
	//ScreenManager.getInstance().showCurrent(textViewer.getDisplayable());

	//Создаём объект вида, номер страницы, и остальные парамеры устанавливаются по умолчанию
	BookView bookView = new BookView(bookPatch);
	
	String bookmarkName = bookmarkManager.generateBookmarkName(bookView.getBookName(), 0);
	
	//добавляем закладку
	bookmarkManager.addBookmark(bookView, bookmarkName);
	
	//Устанавливаем ее как текущую
	bookmarkManager.setCurrentBookmarkName(bookmarkName);
	
	//Сохраняем возможные изменения в закладках
	textViewer.saveChangedView();
	
	//Покaзываем
	textViewer.showText(bookView);
	
	//В оригинале этого нет, для bookViewer помещается один объект дисплея в display
	//Новая концепция предполагает динамическую смену дисплнев, старую систему нужно переделать
	ScreenManager.getInstance().showCurrent(textViewer.getDisplayable());
}

private void saveParameters(){
	//Подсветка 
	programParametrs.setLightValue(Light.getLight());
	
	//Закладка
	programParametrs.setBookmarkName(bookmarkManager.getCurrentBookmarkName());
	
	//Текущее отображение
	textViewer.saveChangedView();
	
	programParametrs.saveParametrs();
	
	//Закладки, если были изменения
	bookmarkManager.saveBookmarks();
}

public void exitProgram(){
	//Сохраняем параметры
	saveParameters();

	//Собс-но выход	
	bookImage.destroyApp(true);
}

public void addCurrentViewToBookmark(String name){
	bookmarkManager.addBookmark(bookViewer.getCurrentBookView(), name);
}


public void rewriteBookmarkToCurrentView(String name){
	bookmarkManager.rewriteBookmarkToCurrentView(bookViewer.getCurrentBookView(), name);
}

//////////////////////ЦЕПОЧКА ОБЯЗАННОСТЕЙ///////////////////////
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
		//Сохраняем возможные изменения в закладках
		bookViewer.saveChangedView();
		bookViewer.showBook(nextView);
	} else {
		userInterface.showInfoMessage("Закладок нет!");
	}
}

public BookmarkList getBookmarkList(){
	return (BookmarkList)bookmarkManager;
}

public void goToBookmark(String name){
	BookView bookView = bookmarkManager.getViewByBookmarkName(name);
	
	//Если найти закладку с таким именем не удалось, устанавливаем умолчание
	if (bookView == null){
		bookView = bookmarkManager.getViewByBookmarkName(bookmarkManager.RESERVED_BOOKMARK_NAME);
	}
	
	//Устанавливаем текущую закладку
	bookmarkManager.setCurrentBookmarkName(name);
	
	//bookViewer.saveChangedView();
	textViewer.saveChangedView();
	//bookViewer.showBook(bookView);
	textViewer.showText(bookView);
	
	//В оригинале этого нет, для bookViewer помещается один объект дисплея в display
	//Новая концепция предполагает динамическую смену дисплнев, старую систему нужно переделать
	ScreenManager.getInstance().showCurrent(textViewer.getDisplayable());
}

public void restoreCurrentView(){
	bookViewer.restoreCurrentView();
}

public void findWordInDictionary(String findWord){
	int positionInFile = dictionary.findWord(findWord);
	
	if (positionInFile == Dictionary.NOT_FOUND){
		userInterface.showErrorMessage("Слово не найдено!");
		return;
	}
	BookView bookView = new BookView(dictionary.getDictionaryPatch());
	bookView.setPageNumber(positionInFile);
	//String dictPatch = dictionary.getDictionaryPatch();
	
	dictionaryTextViewer.showText(bookView);
	ScreenManager.getInstance().showCurrent(dictionaryTextViewer.getDisplayable());
	
	//System.out.println("dictPatch = "+dictPatch);
	//System.out.println("индекс = "+positionInFile);
}

}