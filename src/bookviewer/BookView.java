package bookviewer;

import screencontent.ContentModes;
import displaysize.DisplaySizeDirector;

public class BookView{

private String bookPatch;
private int pageNumber;

//Позиция отображения на странице
private int positionHorizontal;
private int positionVertical;

//Размер области отображения
int сontentAreaVisota;
int сontentAreaShirina;

//границы на странице
int leftBound;
int rightBound;

//Режим отображения документа
private int folding;

//В режимах разделения строк, высота строки
private int stringVisota;

//Поворот на 90 градусов
private boolean rotate; 

//Ключ изменений
private boolean chastity;

public BookView(String inPatch){
	bookPatch = inPatch;
	pageNumber = 1;
	stringVisota = 10;
	folding = ContentModes.MODE_FLAT;
	positionHorizontal = 0;
	positionVertical = 0;
	rotate = false;
	сontentAreaVisota = DisplaySizeDirector.getInstance().getScreenVisota();
	сontentAreaShirina = DisplaySizeDirector.getInstance().getScreenShirina();
	
	//С начала не известно какого размера будет страница, поэтому задать размеры границ невозможно
	//-1 это ключ который указывает что границы не установленны!
	leftBound = -1;
	rightBound = -1;
	
	chastity = true;
}

public String getPatch(){
	return bookPatch;
}



public String getBookName(){
	//Отсекаем ненужную левую часть пути
	//Убираем последний слеш
	String tmp = bookPatch.substring(0, bookPatch.length()-1);
	int pos = tmp.lastIndexOf('/');
	String bookName = tmp.substring(pos+1, tmp.length());
	
	return bookName;
}


/////////////////////////////////////////////

public int getPageNumber(){
	return pageNumber;
}


public void setPageNumber(int inPageNumber){
	pageNumber = inPageNumber;
	chastity = false;
}
////////////////////////////////////////////

public int getStringVisota(){
	return stringVisota;
}

public void setStringVisota(int inStringVisota){
	stringVisota = inStringVisota;
	chastity = false;
}
////////////////////////////////////////////

public int getFoldingMode(){
	return folding;
}

public void setFoldingMode(int inFolding){
	folding = inFolding;
	chastity = false;
}

/////////////////////////////////////////////

public int getPositionHorizontal(){
	return positionHorizontal;
}

public void setPositionHorizontal(int inPositionHorizontal){
	positionHorizontal = inPositionHorizontal;
	chastity = false;
}
////////////////////////////////////////////

public int getPositionVertical(){
	return positionVertical;
}

public void setPositionVertical(int inPositionVertical){
	positionVertical = inPositionVertical;
	chastity = false;
}
///////////////////////////////////////////
public boolean isRotate(){
	return rotate;
}

public void setRotate(boolean inRotate){
	rotate = inRotate;
	chastity = false;
}
////////////////////////////////////////////

public int getContentAreaVisota(){
	return сontentAreaVisota;
}

public int getContentAreaShirina(){
	return сontentAreaShirina;
}

public void setContentAreaVisota(int inContentAreaVisota){
	сontentAreaVisota = inContentAreaVisota;
	chastity = false;
}

public void setContentAreaShirina(int inContentAreaShirina){
	сontentAreaShirina = inContentAreaShirina;
	chastity = false;
}

//////////////////////////////////////////
public int getRightBound(){
	return rightBound;
}

public int getLeftBound(){
	return leftBound;
}

public void setRightBound(int inRightBound){
	rightBound = inRightBound;
	chastity = false;
}

public void setLeftBound(int inLeftBound){
	leftBound = inLeftBound;
	chastity = false;
}
//////////////////////////////////////////////

////////////////ИЗМЕНЕНИЯ///////////////////////
public void restoreChastity(){
	chastity = true;
}

public boolean isChastity(){
	return chastity;
}
}