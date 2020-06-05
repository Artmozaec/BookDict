package rms;

import onestoremanyrecords.StoreRecord;
import bookmarks.Bookmark;
import java.io.*;
import bookviewer.BookView;

class BookmarkToStoreRecord{
private StoreRecord storeRecord;
private Bookmark bookmark;

BookmarkToStoreRecord(){}


private void addBookmarkName(){
	byte[] part = null;
	
	String name = bookmark.getName();
	try{
		part = name.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {}
	
	storeRecord.putPart(part);
	
}

private void addPatch(){
	BookView bookView = bookmark.getBookView();
	byte[] part = null;
	
	String patch = bookView.getPatch();
	try{
		part = patch.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {}
	
	storeRecord.putPart(part);
}

/////////////////////��������� ��������� ����/////////////////////////

private void addBookViewParameters(){
	BookView bookView = bookmark.getBookView();
	byte[] intBytes;
	byte[] result = new byte[38];
	
	//////////////����� ��������//////////////
	intBytes = ValueConverter.intToByteArray(bookView.getPageNumber());
	
	result[0] = intBytes[0];
	result[1] = intBytes[1];
	result[2] = intBytes[2];
	result[3] = intBytes[3];
	
	/////////////����� �����������////////////
	intBytes = ValueConverter.intToByteArray(bookView.getFoldingMode());
	
	result[4] = intBytes[0];
	result[5] = intBytes[1];
	result[6] = intBytes[2];
	result[7] = intBytes[3];
	
	/////////////���� ��������////////////////
	if (bookView.isRotate()){
		result[8] = (byte)1;
	} else {
		result[8] = (byte)0;
	}
	
	

	
	/////////������ ������ ��� ������ � ��������� �����/////////////
	intBytes = ValueConverter.intToByteArray(bookView.getStringVisota());
	
	result[9] = intBytes[0];
	result[10] = intBytes[1];
	result[11] = intBytes[2];
	result[12] = intBytes[3];
	
	////////������� �� ������///////////////////////////
	intBytes = ValueConverter.intToByteArray(bookView.getPositionVertical());
	
	result[13] = intBytes[0];
	result[14] = intBytes[1];
	result[15] = intBytes[2];
	result[16] = intBytes[3];
	
	///////������� �� ������////////////////////////////
	intBytes = ValueConverter.intToByteArray(bookView.getPositionHorizontal());
	
	result[17] = intBytes[0];
	result[18] = intBytes[1];
	result[19] = intBytes[2];
	result[20] = intBytes[3];
	
	///////������ ������� ����������� - ������//////////////
	intBytes = ValueConverter.intToByteArray(bookView.getContentAreaVisota());
	
	result[21] = intBytes[0];
	result[22] = intBytes[1];
	result[23] = intBytes[2];
	result[24] = intBytes[3];
	
	///////������ ������� ����������� - ������//////////////
	intBytes = ValueConverter.intToByteArray(bookView.getContentAreaShirina());
	
	result[25] = intBytes[0];
	result[26] = intBytes[1];
	result[27] = intBytes[2];
	result[28] = intBytes[3];
	
	///////����� ������� �� ��������//////////////////
	intBytes = ValueConverter.intToByteArray(bookView.getLeftBound());
	
	result[29] = intBytes[0];
	result[30] = intBytes[1];
	result[31] = intBytes[2];
	result[32] = intBytes[3];
	
	///////������ ������� �� ��������//////////////////
	intBytes = ValueConverter.intToByteArray(bookView.getRightBound());
	
	result[33] = intBytes[0];
	result[34] = intBytes[1];
	result[35] = intBytes[2];
	result[36] = intBytes[3];
	
	///////���� ���������///////////////////////////
	if (bookmark.isVisible()){
		result[37] = (byte)1;
	} else {
		result[37] = (byte)0;
	}
	
	storeRecord.putPart(result);
}


void convert(Bookmark inBookmark){
	bookmark = inBookmark;
	storeRecord = new StoreRecord();
	
	//���
	addBookmarkName();
	
	//����
	addPatch();
	
	//�� ���������
	addBookViewParameters();
}


public StoreRecord getResult(){
	return storeRecord;
}

}