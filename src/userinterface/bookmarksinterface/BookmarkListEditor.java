package bookmarksinterface;

import javax.microedition.lcdui.*;
import userinterface.UserInterface;
import java.io.*;
import userinterface.TextEditorListener;
import message.QuestionListener;

public class BookmarkListEditor extends List implements CommandListener, TextEditorListener, QuestionListener{

private BookmarkList bookmarkList;

//private Command commandChangeVisible;
private Command commandGoToBookmark;
private Command commandBack;
private Command commandDelete;
private Command commandRename;
private Command commandBookmarkUp;
private Command commandBookmarkDown;
private Command commandRewrite;
private Command commandAdd;
private Command commandClear;

private UserInterface userInterface;
//private static String[] bookmarksArray;

private Image ImageCheckboxSelect;
private Image ImageCheckboxUnselect;

public BookmarkListEditor(UserInterface inUserInterface, BookmarkList inBookmarkList){
	//������� ����
	super("bookmarks", List.IMPLICIT);

	try{
		ImageCheckboxSelect = Image.createImage("/icons/checkbox_select.png");
		ImageCheckboxUnselect = Image.createImage("/icons/checkbox_unselect.png");
	}catch(IOException e){
		ImageCheckboxSelect = null;
		ImageCheckboxUnselect = null;
	}

	//�����
	commandBack = new Command("�����", Command.BACK, 0);
	this.addCommand(commandBack);
	
	//������� �������� �� ��������
	commandGoToBookmark = new Command("�������", Command.ITEM, 0);
	this.addCommand(commandGoToBookmark);

	//�������� ��������
	commandAdd = new Command("��������", Command.ITEM, 1);
	this.addCommand(commandAdd);

	//����������
	commandRewrite = new Command("����������", Command.ITEM, 2);
	this.addCommand(commandRewrite);	
	
	//�������� �����
	commandBookmarkUp = new Command("�����", Command.ITEM, 3);
	this.addCommand(commandBookmarkUp);
	
	//�������� ����
	commandBookmarkDown = new Command("����", Command.ITEM, 4);
	this.addCommand(commandBookmarkDown);
	
	//�������������
	commandRename = new Command("�������������", Command.ITEM, 5);
	this.addCommand(commandRename);
	
	//�������
	commandDelete = new Command("�������", Command.ITEM, 6);
	this.addCommand(commandDelete);
	
	
	//�������� ����
	commandClear = new Command("��������", Command.ITEM, 7);
	this.addCommand(commandClear);
	
	this.setCommandListener(this);
	
	
	userInterface = inUserInterface;
	bookmarkList = inBookmarkList;
	fillBookmarks();
}

private void fillBookmarks(){
	this.deleteAll();
	String[] bookmarks = bookmarkList.getList();
	boolean[] visibleKeys = bookmarkList.getVisibleKeys();
	
	for(int ch=0; ch<bookmarks.length; ch++){
		if (visibleKeys[ch]){
			this.append(bookmarks[ch], ImageCheckboxSelect);
		} else {
			this.append(bookmarks[ch], ImageCheckboxUnselect);
		}
	}
	
	selectBookmarkInList(bookmarkList.getCurrentBookmarkName());
}

private void selectBookmarkInList(String bookmarkName){
	for (int ch=0; ch<this.size(); ch++){
		if (bookmarkName.equals(this.getString(ch))) this.setSelectedIndex(ch, true);
	}
}

public void commandAction(Command c, Displayable d) {
	int selectNumber = this.getSelectedIndex();
	String selectBookmark = this.getString(selectNumber);
	
	if (c == SELECT_COMMAND) {
		bookmarkList.setVisible(selectBookmark);
		fillBookmarks();
		selectBookmarkInList(selectBookmark);
	} else if (c == commandBack){
		userInterface.goToBackScreen();
	} else if (c == commandGoToBookmark){
		userInterface.goToBookmark(selectBookmark);
	} else if (c == commandDelete){
		int selectIndex = this.getSelectedIndex();
		selectIndex--;
		if (selectIndex<0) selectIndex=0;
		bookmarkList.delete(selectBookmark);
		fillBookmarks();
		this.setSelectedIndex(selectIndex, true);
	} else if (c == commandRename){
		userInterface.showTextEditor(this, selectBookmark);	
	} else if (c == commandBookmarkUp){
		bookmarkList.moveUp(selectBookmark);
		fillBookmarks();
		selectBookmarkInList(selectBookmark);
	} else if (c == commandBookmarkDown){
		bookmarkList.moveDown(selectBookmark);
		fillBookmarks();
		selectBookmarkInList(selectBookmark);
	} else if (c == commandRewrite){
		userInterface.rewriteBookmarkToCurrentView(selectBookmark);
	} else if (c == commandClear){
		userInterface.showQuestion("�������� ��������?", this);
	} else if (c == commandAdd){
		userInterface.addCurrentViewToBookmark();
	}
}

public void textInputIsFinished(String text){	
	if (bookmarkList.isCorrectName(text)){
		bookmarkList.rename(this.getString(this.getSelectedIndex()), text);
		fillBookmarks();
		selectBookmarkInList(text);
	} else {
		userInterface.showInfoMessage("����� ��� ��� ����");
	}
	userInterface.goToBackScreen();
}

public void selectNo(){
	userInterface.goToBackScreen();
}

public void selectYes(){
	userInterface.goToBackScreen();
	bookmarkList.clearBookmarks();
	fillBookmarks();
}

}