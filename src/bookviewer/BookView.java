package bookviewer;

import screencontent.ContentModes;
import displaysize.DisplaySizeDirector;

public class BookView{

private String bookPatch;
private int pageNumber;

//������� ����������� �� ��������
private int positionHorizontal;
private int positionVertical;

//������ ������� �����������
int �ontentAreaVisota;
int �ontentAreaShirina;

//������� �� ��������
int leftBound;
int rightBound;

//����� ����������� ���������
private int folding;

//� ������� ���������� �����, ������ ������
private int stringVisota;

//������� �� 90 ��������
private boolean rotate; 

//���� ���������
private boolean chastity;

public BookView(String inPatch){
	bookPatch = inPatch;
	pageNumber = 1;
	stringVisota = 10;
	folding = ContentModes.MODE_FLAT;
	positionHorizontal = 0;
	positionVertical = 0;
	rotate = false;
	�ontentAreaVisota = DisplaySizeDirector.getInstance().getScreenVisota();
	�ontentAreaShirina = DisplaySizeDirector.getInstance().getScreenShirina();
	
	//� ������ �� �������� ������ ������� ����� ��������, ������� ������ ������� ������ ����������
	//-1 ��� ���� ������� ��������� ��� ������� �� ������������!
	leftBound = -1;
	rightBound = -1;
	
	chastity = true;
}

public String getPatch(){
	return bookPatch;
}



public String getBookName(){
	//�������� �������� ����� ����� ����
	//������� ��������� ����
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
	return �ontentAreaVisota;
}

public int getContentAreaShirina(){
	return �ontentAreaShirina;
}

public void setContentAreaVisota(int inContentAreaVisota){
	�ontentAreaVisota = inContentAreaVisota;
	chastity = false;
}

public void setContentAreaShirina(int inContentAreaShirina){
	�ontentAreaShirina = inContentAreaShirina;
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

////////////////���������///////////////////////
public void restoreChastity(){
	chastity = true;
}

public boolean isChastity(){
	return chastity;
}
}