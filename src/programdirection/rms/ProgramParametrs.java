package rms;

import onestoremanyrecords.*;
import bookmarks.BookmarkManager;
import java.util.Vector;
import java.io.*;

public class ProgramParametrs{

private static ProgramParametrs programParametrs = null;
private final static String PARAMETRS_STORY_NAME = "parametrs";
private int lightValue;
private String bookmarkName;
//Ключ изменений
private boolean chastity;


private ProgramParametrs(){
	restoreParametrs();
	chastity = true;
}

public static ProgramParametrs getInstance(){
	if (programParametrs == null){
		programParametrs = new ProgramParametrs();
	}
	return programParametrs;
}

///////////////Уровень подсветки////////////////
public int getLightValue(){
	return lightValue;
}

public void setLightValue(int newLightValue){
	if (newLightValue == lightValue) return;
	//System.out.println("setLightValue");
	chastity = false;
	lightValue = newLightValue;
}

///////////////Имя закладки////////////////////
public String getBookmarkName(){
	return bookmarkName;
}

public void setBookmarkName(String inBookmarkName){
	if (inBookmarkName.equals(bookmarkName)) return;
	//System.out.println("setBookmarkName");
	chastity = false;
	bookmarkName = inBookmarkName;
}

private void setDefault(){
	lightValue = 100;
	bookmarkName = BookmarkManager.RESERVED_BOOKMARK_NAME;
}

private void restoreParametrs(){
	OneStoryManyRecords oneStoryManyRecords = new OneStoryManyRecords(PARAMETRS_STORY_NAME);
	
	//Пытаемся восстановить параметры
	Vector storeRecords = oneStoryManyRecords.restoreData();
	
	//Если с загрузкой проблемы восстанавливаем значения по умолчанию
	if (!oneStoryManyRecords.isOk()){ 
		//System.out.println("Хранилища нененененТТТ");
		setDefault();
		return;
	}
	
	//Если элементов не-то колличество устанавливаем умолчания
	if (storeRecords.size()!=1){ 
		//System.out.println("Размер вектора какой-то другой!!!! "+storeRecords.size());
		setDefault();
		return;
	}
	
	StoreRecord storeRecord = (StoreRecord)storeRecords.elementAt(0);
	
	byte[] part;
	////////////////////////ПОДСВЕТКА//////////////////////////
	part = storeRecord.getPart();
	
	if (part.length!=4){
		//System.out.println("Подсветка - ошибка хранилища");
		setDefault();
		return;
	}
	
	lightValue = ValueConverter.byteArrayToInt(part);
	
	
	////////////////////////ЗАКЛАДКА///////////////////////////
	part = storeRecord.getPart();
	
	try{
		bookmarkName = new String(part, "windows-1251");
	} catch (UnsupportedEncodingException ue) {
		setDefault();
	}
	//System.out.println("ЗаКлАТКА === -" +bookmarkName);
	
}

public void saveParametrs(){
	//Если изменений небыло, тогда и созранять ни чего не надо
	if (chastity) return;
	//System.out.println("~~~~~~~~~~~~saveParametrs()~~~~~~~~~~`");
	
	OneStoryManyRecords oneStoryManyRecords = new OneStoryManyRecords(PARAMETRS_STORY_NAME);
	
	StoreRecord storeRecord = new StoreRecord();
	
	byte[] part;
	////////////////////////ПОДСВЕТКА//////////////////////////
	part = ValueConverter.intToByteArray(lightValue);
	storeRecord.putPart(part);
	
	////////////////////////ЗАКЛАДКА///////////////////////////
	try{
		part = bookmarkName.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {}
	storeRecord.putPart(part);
	
	Vector data = new Vector();
	
	data.addElement(storeRecord);
	oneStoryManyRecords.saveData(data);
}


}