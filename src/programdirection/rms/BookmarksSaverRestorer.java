package rms;

import onestoremanyrecords.*;
import bookmarks.Bookmark;
import bookviewer.BookView;
import java.util.Vector;

import java.io.*;

public class BookmarksSaverRestorer{

private final static String BOOKMARKS_STORY_NAME = "bookmarks";

public BookmarksSaverRestorer(){
}

public void saveBookmarks(Vector bookmarks){
	BookmarkToStoreRecord bookmarkToStoreRecord = new BookmarkToStoreRecord();
	
	OneStoryManyRecords oneStoryManyRecords = new OneStoryManyRecords(BOOKMARKS_STORY_NAME);
	Vector storeRecords = new Vector();
	
	//������� ������ �������
	for (int ch=0; ch<bookmarks.size(); ch++){
		StoreRecord currentRecord = new StoreRecord();
		Bookmark bookmark = (Bookmark)bookmarks.elementAt(ch);
		bookmarkToStoreRecord.convert(bookmark);
		storeRecords.addElement(bookmarkToStoreRecord.getResult());
	}
	
	//���������� ��� ����...
	oneStoryManyRecords.saveData(storeRecords);
}

public Vector restoryBookmarks(){
	StoreRecordToBookmark storeRecordToBookmark = new StoreRecordToBookmark();
	
	Vector bookmarks = new Vector();
	OneStoryManyRecords oneStoryManyRecords = new OneStoryManyRecords(BOOKMARKS_STORY_NAME);
	
	//�������� �������� ������ �� ���������
	Vector storeRecords = oneStoryManyRecords.restoreData();
	
	//System.out.println("�������� ---- restoryBookmarks()");
	//���� � ��������� �������� �������������
	if (!oneStoryManyRecords.isOk()) return null;
	
	for (int ch=0; ch<storeRecords.size(); ch++){
		//System.out.println("������"+ch);
		StoreRecord currentRecord = (StoreRecord)storeRecords.elementAt(ch);
		//���� �������� ������ �����, �������������
		if (!storeRecordToBookmark.convert(currentRecord)) return null;
		
		bookmarks.addElement(storeRecordToBookmark.getResult());
	}
	
	return bookmarks;
}

}