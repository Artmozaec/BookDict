package userinterface;
import bookmarksinterface.BookmarkList;
public interface UserInterfaceListener{
public void exitProgram();

public String getWorkFolder();
public String getBookName();

public int getPageNumber();
public void goToPage(int page);
public void setLineFoldingMode(int areaMode);
public void addCurrentViewToBookmark(String name);
public BookmarkList getBookmarkList();
public void goToBookmark(String name);
public void setLight(int newLight);
public void rewriteBookmarkToCurrentView(String name);
public void restoreCurrentView();
public int getLightValue();
public void patchChoosed(String directory);
}