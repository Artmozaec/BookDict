package bookmarks;
import bookviewer.BookView;

public class Bookmark{
private BookView bookView;
private boolean visible;
private String name;

public Bookmark(BookView inView, String inName){
	name = inName;
	bookView = inView;
	visible = true;
}
	
public BookView getBookView(){
	return bookView;
}

public void setBookView(BookView inView){
	bookView = inView;
}

public boolean isVisible(){
	return visible;
}

public void visible(){
	visible = true;
}

public void invisible(){
	visible = false;
}

////////////////»Ãﬂ////////////////

public String getName(){
	return name;
}

public void setName(String inName){
	name = inName;	
}
}