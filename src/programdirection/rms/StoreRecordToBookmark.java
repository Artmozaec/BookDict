package rms;

import onestoremanyrecords.StoreRecord;
import bookmarks.Bookmark;
import bookviewer.BookView;
import java.io.*;


class StoreRecordToBookmark{
private Bookmark bookmark;
private StoreRecord storeRecord;
private boolean visible;
StoreRecordToBookmark(){}


private String getBookmarkName(){
	//System.out.println("getBookmarkName(){");
	byte[] part = storeRecord.getPart();
	String name = null;
	
	try{
		name = new String(part, "windows-1251");
	} catch (UnsupportedEncodingException ue) {}
	
	return name;
}

private String getPatch(){
	//System.out.println("getPatch()");
	byte[] part = storeRecord.getPart();
	String patch = null;
	
	try{
		patch = new String(part, "windows-1251");
	} catch (UnsupportedEncodingException ue) {}
	
	return patch;
}

private void restoreVisibleKey(byte[] part){
	if (part[37] == 1){
		visible = true;
	} else {
		visible = false;
	}
	
}

private boolean restoreBookViewParameters(BookView bookView){
	byte[] part = storeRecord.getPart();
	//Если длинна не соответствует стандарту, сигнализируем
	if (part.length != 38) return false;
	byte[] intBytes = new byte[4];
	
	
	//////////////НОМЕР СТРАНИЦЫ//////////////
	intBytes[0] = part[0];
	intBytes[1] = part[1];
	intBytes[2] = part[2];
	intBytes[3] = part[3];
	
	bookView.setPageNumber(ValueConverter.byteArrayToInt(intBytes));
	
	/////////////РЕЖИМ ОТОБРАЖЕНИЯ////////////
	intBytes[0] = part[4];
	intBytes[1] = part[5];
	intBytes[2] = part[6];
	intBytes[3] = part[7];
	
	bookView.setFoldingMode(ValueConverter.byteArrayToInt(intBytes));
	
	/////////////КЛЮЧ ПОВОРОТА////////////////

	if (part[8] == 1){
		bookView.setRotate(true);
	} else {
		bookView.setRotate(false);
	}
	
	/////////ВЫСОТА СТРОКИ ДЛЯ РЕЖИМА С ПЕРЕНОСОМ СТРОК/////////////
	intBytes[0] = part[9];
	intBytes[1] = part[10];
	intBytes[2] = part[11];
	intBytes[3] = part[12];
	
	bookView.setStringVisota(ValueConverter.byteArrayToInt(intBytes));
	
	////////ПОЗИЦИЯ ПО ВЫСОТЕ///////////////////////////
	intBytes[0] = part[13];
	intBytes[1] = part[14];
	intBytes[2] = part[15];
	intBytes[3] = part[16];
	
	bookView.setPositionVertical(ValueConverter.byteArrayToInt(intBytes));
	
	///////ПОЗИЦИЯ ПО ШИРИНЕ////////////////////////////
	intBytes[0] = part[17];
	intBytes[1] = part[18];
	intBytes[2] = part[19];
	intBytes[3] = part[20];
	
	bookView.setPositionHorizontal(ValueConverter.byteArrayToInt(intBytes));
	
	///////РАЗМЕР ОБЛАСТИ ОТОБРАЖЕНИЯ - ВЫСОТА//////////////
	
	intBytes[0] = part[21];
	intBytes[1] = part[22];
	intBytes[2] = part[23];
	intBytes[3] = part[24];
	
	bookView.setContentAreaVisota(ValueConverter.byteArrayToInt(intBytes));
	
	///////РАЗМЕР ОБЛАСТИ ОТОБРАЖЕНИЯ - ШИРИНА//////////////
	intBytes[0] = part[25];
	intBytes[1] = part[26];
	intBytes[2] = part[27];
	intBytes[3] = part[28];
	
	bookView.setContentAreaShirina(ValueConverter.byteArrayToInt(intBytes));
	
	///////ЛЕВАЯ ГРАНИЦА НА СТРАНИЦЕ//////////////////
	intBytes[0] = part[29];
	intBytes[1] = part[30];
	intBytes[2] = part[31];
	intBytes[3] = part[32];
	
	bookView.setLeftBound(ValueConverter.byteArrayToInt(intBytes));
	
	///////ПРАВАЯ ГРАНИЦА НА СТРАНИЦЕ//////////////////
	intBytes[0] = part[33];
	intBytes[1] = part[34];
	intBytes[2] = part[35];
	intBytes[3] = part[36];
	
	bookView.setRightBound(ValueConverter.byteArrayToInt(intBytes));
	
	//Значение ключа видимости хоть и не относится к BookView но по техническим причинам располагается в этой-же части
	restoreVisibleKey(part);
	
	return true;
}


boolean convert(StoreRecord inStoreRecord){
	storeRecord = inStoreRecord;
	
	String name = getBookmarkName();
	//System.out.println("convert - name = "+name);
	String patch = getPatch();
	//System.out.println("convert - patch = "+patch);
	BookView bookView = new BookView(patch);
	
	//В процессе восстановления параметров, ошибки
	if (!restoreBookViewParameters(bookView)) return false;
	
	bookmark = new Bookmark(bookView, name);
	
	if (visible){
		bookmark.visible();
	} else {
		bookmark.invisible();
	}
	
	return true;
}

public Bookmark getResult(){
	return bookmark;
}

}