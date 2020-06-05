package bookviewer;

import rgbimage.RGBImage;
import screencontent.ContentDirection;
import javax.microedition.lcdui.*;
import screencontent.ContentModes;
import bookviewer.BookViewer;


class Painter extends Canvas{

private BookViewer bookViewer;
private ContentDirection contentDirection;


private static final int WHITE = 0xFFFFFF;
private static final int ORANGE = 0xFFA800;
private static final int BLACK = 0x000000;

private static Font panelFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);

//�������� ���������
private static final int JOYSTICK_UP = -1;
private static final int JOYSTICK_DOWN = -2;
private static final int JOYSTICK_LEFT = -3;
private static final int JOYSTICK_RIGHT = -4;
private static final int JOYSTICK_CENTER = -5;

private static final int RIGHT_KEY = -7;
private static final int LEFT_KEY = -6;

private boolean repeatedKey;
private boolean infoPanelDraw;

//������� ��� ��������� �����������
private int encryptCount;


Painter(ContentDirection inContentDirection, BookViewer inBookViewer){
	setFullScreenMode(true);
	bookViewer = inBookViewer;
	contentDirection = inContentDirection;
	repeatedKey = false;
	infoPanelDraw = false;
	encryptCount = 0;
}


public void drawInfoPanel(Graphics g){
	//������ �������
	g.setColor(ORANGE);
	int panelVis = panelFont.getHeight()+6;
	g.setFont(panelFont);
	g.fillRect(0,getHeight()-panelVis, getWidth(), panelVis);
	
	g.setColor(BLACK);
	
	BookView bookView = bookViewer.getCurrentBookView();
	
	//��� �����
	String bookName = bookView.getBookName();
	//������� ������, ���� �� ����������
	if (bookName.length() >8) bookName = bookName.substring(0, 8)+"...";
	
	g.drawString(bookName, 0, getHeight()-panelVis+2, g.TOP|g.LEFT);
	g.drawString("��� = "+bookView.getPageNumber(), getWidth(), getHeight()-panelVis+2, g.TOP|g.RIGHT);
	
}


public void paint(Graphics g){
	//System.out.println("PAINT!!!!!");	
	g.setColor(WHITE);
	g.fillRect(0,0,500,500);

	int screenVisota = getHeight();
	int screenShirina = getWidth();
	
	g.drawImage(contentDirection.getScreenImage(screenVisota, screenShirina), 0, 0, 0);
	
	//������ ������
	if (infoPanelDraw){
		drawInfoPanel(g);
	}
}


//���������� ���������� � ������ �������� �����
private void ModeLineFoldingDirection(int keyCode){
	switch (keyCode){
		case RIGHT_KEY:{ //��������� ������� ����������
			infoPanelDraw = true;
		}
		break;
		
		case KEY_NUM2:{ //�����
			contentDirection.screenUp();
		}
		break;
		
		case KEY_NUM8:{ //����
			contentDirection.screenDown();
		}
		break;
		
		///////////////������� ������ ������////////////
		case KEY_NUM3:{
			contentDirection.textMoveLeft();
		}
		break;
		
		///////////////������� ������ �����////////////
		case KEY_NUM1:{
			contentDirection.textMoveRight();
		}
		break;
		
		////////////// ������� /////////////////////
		case KEY_NUM9:{ //����������
			contentDirection.zoomIn();
		}
		break;
		
		case KEY_NUM7:{ //����������
			contentDirection.zoomOut();
		}
		break;
		////////////////////////////////////////////
		////////////�������� ������� �������////////////
		case KEY_NUM6:{ //�������� � �����
			contentDirection.moveMiddleBoundRight();
		}
		break;
		
		case KEY_NUM4:{ //�������� � ����
			contentDirection.moveMiddleBoundLeft();
		}
		break;
		////////////////////////////////////////////////
		
		
		case KEY_POUND:{ //����������� ������
			contentDirection.setModeFlat();
		}
		break;
		
		case JOYSTICK_UP:{//���������� ������� ������ ����������� �����
			contentDirection.addStringVisota();
		}
		break;
		
		case JOYSTICK_DOWN:{//���������� ������� ������ ����������� �����
			contentDirection.subStringVisota();
		}
		break;
		
		case JOYSTICK_LEFT:{
			contentDirection.smallUp();
		}
		break;
		
		case JOYSTICK_RIGHT:{
			contentDirection.smallDown();
		}
		break;
		
		case JOYSTICK_CENTER:{//����� ����
			bookViewer.showMenu();
		}
		break;
		
		case KEY_STAR:{ //������ ��������
			bookViewer.showBookmarksListEditor();
		}
		break;	
		
		case KEY_NUM0:{ //��������� ��������
			bookViewer.nextBookmark();
		}
		break;
		
		
	}
	repaint();
}

//���������� ���������� � ������� ������
private void ModeFlatDirection(int keyCode){
	
	if ((encryptCount>0) && ((keyCode != KEY_NUM3) && (keyCode != KEY_NUM4) && (keyCode != KEY_NUM9))){
		//������ �� �� ������� �� KEY_NUM3 � �� KEY_NUM4 � �� KEY_NUM9 - �����...
		System.out.println("�����");
		encryptCount = 0;
	}
	//System.out.println(keyCode);
	switch (keyCode){
		case RIGHT_KEY:{ //��������� ������� ����������
			infoPanelDraw = true;
		}
		break;
		
		case KEY_NUM1:{ //� ����� ����
			contentDirection.alignToLeftBound();
		}
		break;
		
		case KEY_NUM3:{ //� ����� �����
			if (encryptCount == 0){
				contentDirection.alignToRightBound();
			} else {
				if (encryptCount == 1){
					System.out.println("enc 2");
					encryptCount++;
				} else {
					encryptCount = 0;
				}
			}
		}
		break;
		
		case KEY_NUM2:{ //�����
			contentDirection.screenUp();
		}
		break;
		
		case KEY_NUM8:{ //����
			contentDirection.screenDown();
		}
		break;
		
		case KEY_NUM6:{ //�����
			contentDirection.screenRight();
		}
		break;
		
		case KEY_NUM4:{ //����
			if (encryptCount == 0){
				contentDirection.screenLeft();
			} else {
				if (encryptCount == 2){
					System.out.println("enc 3");
					encryptCount++;
				} else {
					encryptCount = 0;
				}
			}
		}
		break;
		
		////////////// ������� /////////////////////
		case KEY_NUM9:{ //����������
			if (encryptCount == 0){
				contentDirection.zoomIn();
			} else {
				if (encryptCount == 3){
					//System.out.println("!!!!!!������!!!!!");
					bookViewer.decryptMode();
				} else {
					encryptCount = 0;
				}
			}	
		}
		break;
		
		case KEY_NUM7:{ //����������
			contentDirection.zoomOut();
		}
		break;
		////////////////////////////////////////////
		
		

		
		case KEY_POUND:{ //����������� ������
			//contentDirection.setModeLineFolding();
			bookViewer.showLinesFoldingAreaSelector();
		}
		break;
		
		case JOYSTICK_UP:{
			contentDirection.smallUp();
		}
		break;
		
		case JOYSTICK_DOWN:{
			contentDirection.smallDown();
		}
		break;
		
		case JOYSTICK_LEFT:{
			contentDirection.smallLeft();
		}
		break;
		
		case JOYSTICK_RIGHT:{
			contentDirection.smallRight();
		}
		break;
		
		case JOYSTICK_CENTER:{//����� ����
			bookViewer.showMenu();
		}
		break;
		
		case KEY_STAR:{ //������ ��������
			bookViewer.showBookmarksListEditor();
		}
		break;				
		
		case KEY_NUM0:{ //��������� ��������
			bookViewer.nextBookmark();
		}
		break;
		
	}
	repaint();
}


private int rotateDirection(int inCode){

	switch (inCode){
		////////////�������� ����������
		case KEY_NUM2:{
			return KEY_NUM4;
		}
		case KEY_NUM8:{
			return KEY_NUM6;
		}
		case KEY_NUM6:{
			return KEY_NUM2;
		}
		case KEY_NUM4:{
			return KEY_NUM8;
		}
		/////////////�������� � ����
		case KEY_NUM3:{
			return KEY_NUM1;
		}
		case KEY_NUM9:{
			return KEY_NUM3;
		}
		//////////////////������� /////////////////////
		case KEY_NUM7:{
			return KEY_NUM9;
		}
		case KEY_NUM1:{
			return KEY_NUM7;
		}
		///////////////////////////////////////////////
		/////////////��������
		case JOYSTICK_UP:{
			return JOYSTICK_LEFT;
		}
		case JOYSTICK_DOWN:{
			return JOYSTICK_RIGHT;
		}
		case JOYSTICK_RIGHT:{
			return JOYSTICK_UP;
		}
		case JOYSTICK_LEFT:{
			return JOYSTICK_DOWN;
		}
	}
	return inCode;
}


public void keyPressed(int keyCode) {
	if (contentDirection.isRotate()) keyCode = rotateDirection(keyCode);
	//� ���������� �� ������ ������ ������ ����������
	if (contentDirection.getMode() == ContentModes.MODE_FLAT){ 
		ModeFlatDirection(keyCode);
		return;
	} else if (contentDirection.getMode() == ContentModes.MODE_LINE_FOLDING){
		ModeLineFoldingDirection(keyCode);
	}
}

protected void keyReleased(int keyCode){
	repeatedKey = false;
	infoPanelDraw = false;
	repaint();
}

//������� ������������� �������� ������ 7 ��� 9
protected void keyRepeated(int keyCode){
	if (contentDirection.isRotate()) keyCode = rotateDirection(keyCode);
	
	if ((!repeatedKey) && (keyCode == KEY_NUM5)){
		contentDirection.rotate();
		//��������� ������ �� ���������� �������
		repeatedKey = true;
		repaint();
	}
	
	if ((!repeatedKey) && (keyCode == KEY_NUM1)){
		repeatedKey = true;
		System.out.println("!keyCode == KEY_NUM1!");
		encryptCount = 1;
	}
}

}