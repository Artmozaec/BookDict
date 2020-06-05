package bookmarks;
import java.util.Vector;
import bookviewer.BookView;
import bookmarksinterface.BookmarkList;
import rms.BookmarksSaverRestorer;

public class BookmarkManager implements BookmarkList{

private Vector bookmarks;
public static final String RESERVED_BOOKMARK_NAME = "КНИГА";

//Указатель текущей закладки
private Bookmark currentBookmark;
private BookmarksSaverRestorer bookmarksSaverRestorer;

public BookmarkManager(){
	bookmarksSaverRestorer = new BookmarksSaverRestorer();
	bookmarks = bookmarksSaverRestorer.restoryBookmarks();
	
	//Если по каким-то причинам восстановления не получилось, ничего! Жизнь продолжается
	if (bookmarks == null){
		bookmarks = new Vector();	
		//Заполняем резервированную позицию
		//BookView bookView = new BookView("file:///E:/");
		BookView bookView = new BookView("file:///root1/");
		
		//bookmarks.addElement(bookView);
		currentBookmark = new Bookmark(bookView, new String(RESERVED_BOOKMARK_NAME));
		bookmarks.addElement(currentBookmark);
	}
}

public void addBookmark(BookView newView, String name){
	Bookmark newBookmark = new Bookmark(newView, name);
	bookmarks.addElement(newBookmark);
}


public void rewriteBookmarkToCurrentView(BookView bookView, String name){	
	Bookmark result = searchBookmarkByName(name);
	result.setBookView(bookView);
}

private Bookmark searchNextVisibleBookmark(int beginIndex){
	beginIndex++;
	if (beginIndex>bookmarks.size()) beginIndex=0;
	for (int ch = beginIndex; ch<bookmarks.size(); ch++){
		System.out.println("ch = "+ch);
		if (((Bookmark)bookmarks.elementAt(ch)).isVisible()) return (Bookmark)bookmarks.elementAt(ch);
	}
	
	for (int ch = 0; ch<bookmarks.size(); ch++){
		System.out.println("ch = "+ch);
		if (((Bookmark)bookmarks.elementAt(ch)).isVisible()) return (Bookmark)bookmarks.elementAt(ch);
	}
	System.out.println("Не найдена следующая");
	return null;
}

public BookView nextBookmark(){
	//Ищем номер текушего елемента
	int pos = bookmarks.indexOf(currentBookmark);
	System.out.println("pos = "+pos);
	//Ищем следующую видимую закладку
	currentBookmark = searchNextVisibleBookmark(pos);

	System.out.println("currentBookmark "+currentBookmark);
	return currentBookmark.getBookView();
}

private boolean isReservedBookmark(String name){
	if (RESERVED_BOOKMARK_NAME.equals(name)) return true;
	return false;
}

private Bookmark searchBookmarkByName(String name){
	for (int ch=0; ch<bookmarks.size(); ch++){
		if (name.equals(((Bookmark)bookmarks.elementAt(ch)).getName())) return (Bookmark)bookmarks.elementAt(ch);
	}
	return null;
}
public BookView getViewByBookmarkName(String name){
	System.out.println("getViewByBookmarkName >>>"+name);
	Bookmark result = searchBookmarkByName(name);
	if (result == null) return null;
	return result.getBookView();
}

//////////////ИНТЕРФЕЙС BookmarkList////////////////////
public String[] getList(){
	String[] list = new String[bookmarks.size()];
	for (int ch=0; ch<bookmarks.size(); ch++){
		list[ch] = ((Bookmark)bookmarks.elementAt(ch)).getName();
	}
	return list;
}

public boolean[] getVisibleKeys(){
	boolean[] visibleKeys = new boolean[bookmarks.size()];
		for (int ch=0; ch<bookmarks.size(); ch++){
		visibleKeys[ch] = ((Bookmark)bookmarks.elementAt(ch)).isVisible();
	}
	return visibleKeys;
}

public String getCurrentBookmarkName(){
	if (currentBookmark == null) return new String();
	return currentBookmark.getName();
}

public void setCurrentBookmarkName(String name){
	Bookmark result = searchBookmarkByName(name);
	if (result == null) return;
	currentBookmark = result;
}

public void moveUp(String name){
	Bookmark result = searchBookmarkByName(name);
	int pos = bookmarks.indexOf(result);
	
	if (pos<1) return;
	
	bookmarks.removeElementAt(pos);
	bookmarks.insertElementAt(result, pos-1);
}
public void moveDown(String name){
	Bookmark result = searchBookmarkByName(name);
	int pos = bookmarks.indexOf(result);
	
	if (pos>=bookmarks.size()-1) return;
	
	bookmarks.removeElementAt(pos);
	bookmarks.insertElementAt(result, pos+1);
}
	
public void setVisible(String name){
	Bookmark result = searchBookmarkByName(name);
	if (result.isVisible()){
		result.invisible();
	} else {
		result.visible();
	}
}

public void rename(String oldName, String newName){
	Bookmark result = searchBookmarkByName(oldName);
	result.setName(newName);
	
}

public void delete(String name){
	Bookmark result = searchBookmarkByName(name);
	bookmarks.removeElement(result);
}

public String generateBookmarkName(String bookName, int pageNumber){
		
	//Если длинна слишком велика, усекаем
	if (bookName.length() >8) bookName = bookName.substring(0, 8);
	
	String result = bookName+"_"+pageNumber;
	
	if (!isCorrectName(result)){
		int prefix = 0;
		while (!isCorrectName(result+'_'+prefix)){
			prefix++;
		}
		result = result+'_'+prefix;
	}
	return result;
}
	
public boolean isCorrectName(String name){
	for (int ch=0; ch<bookmarks.size(); ch++){
		if (name.equals(((Bookmark)bookmarks.elementAt(ch)).getName())) return false;
	}
	return true;
}

public void clearBookmarks(){
	bookmarks.removeAllElements();
	bookmarks.addElement(currentBookmark);
}

public void saveBookmarks(){
	bookmarksSaverRestorer.saveBookmarks(bookmarks);
}

}