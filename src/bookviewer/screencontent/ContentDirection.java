package screencontent;

import bookpages.Page;
import javax.microedition.lcdui.Image;
import rgbimage.RGBImage;
import anchormover.AnchorMover;
import anchormover.AnchorMoverDirector;
import anchor.AnchorDirector;
import anchor.Anchor;
import displaysize.DisplaySizeDirector;
import userinterface.UserInterface;

public class ContentDirection{
private Page page;
private AnchorDirector anchorDirector;
private ContentArea �ontentArea;
private AbstractScreenContent screenContent;
//������ ��������� �����, ��������� ������� ����������� �����
private AnchorMoverDirector anchorMoverDirector;

//������� ��������
private boolean rotate;

//������� ����� ���������
private int currentMode;


public ContentDirection(Page inPage){
	page = inPage;
	currentMode = ContentModes.MODE_FLAT;
	
	//������� ���������� �����
	anchorMoverDirector = new AnchorMoverDirector(10);

	//������ ������ ������� ������������ �������
	�ontentArea = new ContentArea(
		DisplaySizeDirector.getInstance().getScreenVisota(), 
		DisplaySizeDirector.getInstance().getScreenShirina()
	);
	
	//������ ���������� ������
	anchorDirector = new AnchorDirector(page, 0, 0);
	
	//������ ������� ���������� ������
	createScreenContent();
}

public void initContentDirection(
	int mode, 
	int anhorVisota, 
	int anhorShirina,
	
	int �ontentAreaVisota,
	int �ontentAreaShirina,
	
	int leftBound,
	int rightBound,
	
	int stringVisota,
	boolean inRotate
){
	rotate = inRotate;
	//currentMode = mode;
	currentMode = mode;
	//������� ���������� �����
	anchorMoverDirector = new AnchorMoverDirector(stringVisota);
	

	
	//������ ������ ������� ������������ �������
	�ontentArea = new ContentArea(�ontentAreaVisota, �ontentAreaShirina);
	
	//������ ���������� ������
	anchorDirector = new AnchorDirector(page, anhorShirina, anhorVisota);
	
	//��� ����� �� ����� �������� ����� � ��������� �����
	locateFullAreaPage();
	
	//������ ������� ���������� ������
	createScreenContent();

	//���� ������� ��������� �������������
	if ((leftBound>=0) && (rightBound>=0)){
		anchorDirector.getAnchor().setLeftBound(leftBound);
		anchorDirector.getAnchor().setRightBound(rightBound);
	}
}


//������������� ������������ �������� ����� 
private void setAnchorBoundFromPageSize(){
}

public AbstractScreenContent getContent(){
	return screenContent;
}



///////////////////////������� �������////////////////////////////

public int getContentAreaVisota(){
	return �ontentArea.getVisota();
}

public int getContentAreaShirina(){
	return �ontentArea.getShirina();
}
/////////////////////�����/////////////////////////////////

//���������� ��������� ����� �� ������
public int getPositionShirina(){
	return anchorDirector.getAnchor().getPositionShirina();
}

//���������� ��������� ����� �� ������
public int getPositionVisota(){
	return anchorDirector.getAnchor().getPositionVisota();
}

public int getLeftBound(){
	return anchorDirector.getAnchor().getLeftBound();
}

public int getRightBound(){
	return anchorDirector.getAnchor().getRightBound();
}
/////////////////////������ ���������////////////////////
public int getStringVisota(){
	return anchorMoverDirector.getStringVisota();
}




/////////////////////////�������_��������////////////////////////
public void screenUp(){
	screenContent.screenUp();
}

public void screenDown(){
	screenContent.screenDown();
}

public void screenLeft(){
	screenContent.screenLeft();
}

public void screenRight(){
	screenContent.screenRight();
}



///////////////////////////�����_��������///////////////////////
public void smallUp(){
	screenContent.smallUp();
}

public void smallDown(){
	screenContent.smallDown();
}

public void smallLeft(){
	screenContent.smallLeft();
}

public void smallRight(){
	screenContent.smallRight();
}



/////////////////////��������_��������/////////////////////////////
public void alignToLeftBound(){
	screenContent.alignToLeftBound();
}

public void alignToRightBound(){
	screenContent.alignToRightBound();
}

/////////////////////������_�����������_������/////////////////////////
public void addStringVisota(){
	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!addStringVisota()");
	anchorMoverDirector.addStringVisota();
}

public void subStringVisota(){
	anchorMoverDirector.subStringVisota();
}



////////////////////////////�������//////////////////////////////////
public void rotate(){
	//������������ ������� �������
	�ontentArea.rotateArea();
	
	//������ ������� ���������
	while ((�ontentArea.getShirina() > page.getShirina()) || (�ontentArea.getVisota() > page.getVisota())){
		//�����������
		UserInterface ui = UserInterface.getInstance();
		ui.showInfoMessage("�� ����������, ���������!!!");
		//������� � ���
		�ontentArea.zoomIn();
	}
	
	//� ���������� ���������� �������, ��� ���� ����� ��������� �� ������� ��������
	//��������� ��������� ���� � ���� �����, ����������� �� ����
	if (screenContent.isRightOverrun()) screenContent.alignToRightBound();
	if (screenContent.isDownOverrun()) screenContent.alignToDownBound();
	if (rotate) rotate = false; 
				else 
				rotate = true;
}

public boolean isRotate(){
	return rotate;
}
////////////////////////////////////////////
//////////////////////////////����� ��������

private void createScreenContent(){
	if (currentMode == ContentModes.MODE_FLAT){
		screenContent = new FlatScreenContent(page, anchorDirector.getAnchor(), �ontentArea);
	} else if (currentMode == ContentModes.MODE_LINE_FOLDING){
		screenContent = new LineFoldingScreenContent(page, anchorDirector.getAnchor(), �ontentArea, anchorMoverDirector);
	} else {
		screenContent = new FlatScreenContent(page, anchorDirector.getAnchor(), �ontentArea);
	}
}


public void setModeLineFolding(){
	currentMode=ContentModes.MODE_LINE_FOLDING;
	createScreenContent();
}

public void setModeFlat(){
	currentMode=ContentModes.MODE_FLAT;
	createScreenContent();
	if (screenContent.isRightOverrun()) screenContent.alignToRightBound();
	if (screenContent.isDownOverrun()) screenContent.alignToDownBound();
}

public int getMode(){
	return currentMode;
}
////////////////////////////////////////////////////////

public Image getScreenImage(int needVisota, int needShirina){
	RGBImage content = screenContent.getContent();

	//���� ����� ��������
	if (rotate){
		content.rotateImage();
	}
	
	
	//���� ������� ������� ������� �������� �� ������������� �������� ������ - ������ ����������
	if ((needShirina!=�ontentArea.getShirina())||(needVisota!=�ontentArea.getVisota())){
		//System.out.println("!!!!!!!!!����������!!!!!!!!!");
		//System.out.println("��������� ������ = "+needVisota+" ��������� ������ ="+needShirina);
		//System.out.println("������� ������ = "+�ontentArea.getVisota()+" ������� ������ ="+�ontentArea.getShirina());
		content.resample(needVisota, needShirina);
		
		//���� �������� �� ����� ������� �����, ��� ������ ��������� ���������!
		//100 - ��������, 150- �����
		if (currentMode == ContentModes.MODE_LINE_FOLDING) content.contrast(100, 150);
	}
	

	return content.getImage();
}

///////////////////////////�������/////////////////////////////
public void zoomIn(){
	�ontentArea.zoomIn();
}

public void zoomOut(){
	�ontentArea.zoomOut();
	//������ ������� ����������, ���� �� ������ ������ ������� ������ ����� ������!
	if ((�ontentArea.getShirina() > page.getShirina()) || (�ontentArea.getVisota() > page.getVisota())){
		//�����������
		UserInterface ui = UserInterface.getInstance();
		ui.showInfoMessage("������ ������!!!");
		//������� � ���
		�ontentArea.zoomIn();
	}
	if (screenContent.isRightOverrun()) screenContent.alignToRightBound();
	if (screenContent.isDownOverrun()) screenContent.alignToDownBound();
}

///////////////////////////////////////////////////////////////

//////////////////////////////////////////

//���������� ��������������� ����������� �� ������ ���������������� ������ �� anchor!!!
public void locateFullAreaPage(){
	anchorDirector.locateFullAreaPage();
}

public void locateLeftAreaPage(){
	anchorDirector.locateLeftAreaPage();
}

public void locateRightAreaPage(){
	anchorDirector.locateRightAreaPage();
}
////////////////////////////////////////////////////////////////////////////
////////////////////////////�������� ������� �������////////////////////////


public void moveMiddleBoundLeft(){
	anchorDirector.moveMiddleBoundLeft();
}

public void moveMiddleBoundRight(){
	anchorDirector.moveMiddleBoundRight();
}
////////////////////////////////////////////////////////////////////////////
////////////////////////////����� ������///////////////////////////

public void textMoveLeft(){
	Anchor anchor = anchorDirector.getAnchor();
	anchor.moveLeft(5);
}

public void textMoveRight(){
	Anchor anchor = anchorDirector.getAnchor();
	anchor.moveRight(5);
}

}