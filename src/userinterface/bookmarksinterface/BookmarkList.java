package bookmarksinterface;

public interface BookmarkList{
	public String[] getList();
	public boolean[] getVisibleKeys();
	public String getCurrentBookmarkName();
	
	public void moveUp(String name);
	public void moveDown(String name);
	
	public void setVisible(String name);
	
	public void delete(String name);
	
	public void rename(String oldName, String newName);
	
	
	public String generateBookmarkName(String patch, int pageNumber);
	
	public boolean isCorrectName(String name);
	
	public void clearBookmarks();
}