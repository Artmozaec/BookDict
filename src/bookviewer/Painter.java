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

//Сканкоды джойстика
private static final int JOYSTICK_UP = -1;
private static final int JOYSTICK_DOWN = -2;
private static final int JOYSTICK_LEFT = -3;
private static final int JOYSTICK_RIGHT = -4;
private static final int JOYSTICK_CENTER = -5;

private static final int RIGHT_KEY = -7;
private static final int LEFT_KEY = -6;

private boolean repeatedKey;
private boolean infoPanelDraw;

//Счётчик для включения дешифратора
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
	//Высота панелей
	g.setColor(ORANGE);
	int panelVis = panelFont.getHeight()+6;
	g.setFont(panelFont);
	g.fillRect(0,getHeight()-panelVis, getWidth(), panelVis);
	
	g.setColor(BLACK);
	
	BookView bookView = bookViewer.getCurrentBookView();
	
	//Имя книги
	String bookName = bookView.getBookName();
	//Урезаем строку, если не помещается
	if (bookName.length() >8) bookName = bookName.substring(0, 8)+"...";
	
	g.drawString(bookName, 0, getHeight()-panelVis+2, g.TOP|g.LEFT);
	g.drawString("стр = "+bookView.getPageNumber(), getWidth(), getHeight()-panelVis+2, g.TOP|g.RIGHT);
	
}


public void paint(Graphics g){
	//System.out.println("PAINT!!!!!");	
	g.setColor(WHITE);
	g.fillRect(0,0,500,500);

	int screenVisota = getHeight();
	int screenShirina = getWidth();
	
	g.drawImage(contentDirection.getScreenImage(screenVisota, screenShirina), 0, 0, 0);
	
	//Рисуем панель
	if (infoPanelDraw){
		drawInfoPanel(g);
	}
}


//Управление программой в режиме переноса строк
private void ModeLineFoldingDirection(int keyCode){
	switch (keyCode){
		case RIGHT_KEY:{ //Отрисовка панелей информации
			infoPanelDraw = true;
		}
		break;
		
		case KEY_NUM2:{ //Вверх
			contentDirection.screenUp();
		}
		break;
		
		case KEY_NUM8:{ //вниз
			contentDirection.screenDown();
		}
		break;
		
		///////////////СДВИЖКА ТЕКСТА ВПРАВО////////////
		case KEY_NUM3:{
			contentDirection.textMoveLeft();
		}
		break;
		
		///////////////СДВИЖКА ТЕКСТА ВЛЕВО////////////
		case KEY_NUM1:{
			contentDirection.textMoveRight();
		}
		break;
		
		////////////// МАСШТАБ /////////////////////
		case KEY_NUM9:{ //Увеличение
			contentDirection.zoomIn();
		}
		break;
		
		case KEY_NUM7:{ //Уменьшение
			contentDirection.zoomOut();
		}
		break;
		////////////////////////////////////////////
		////////////СМЕЩЕНИЕ СРЕДНЕЙ ГРАНИЦЫ////////////
		case KEY_NUM6:{ //Середину в право
			contentDirection.moveMiddleBoundRight();
		}
		break;
		
		case KEY_NUM4:{ //Середину в лево
			contentDirection.moveMiddleBoundLeft();
		}
		break;
		////////////////////////////////////////////////
		
		
		case KEY_POUND:{ //Переключене режима
			contentDirection.setModeFlat();
		}
		break;
		
		case JOYSTICK_UP:{//Увеличение размера высоты разделяемых строк
			contentDirection.addStringVisota();
		}
		break;
		
		case JOYSTICK_DOWN:{//Уменьшение размера высоты разделяемых строк
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
		
		case JOYSTICK_CENTER:{//Вызов меню
			bookViewer.showMenu();
		}
		break;
		
		case KEY_STAR:{ //Список закладок
			bookViewer.showBookmarksListEditor();
		}
		break;	
		
		case KEY_NUM0:{ //Следующая закладка
			bookViewer.nextBookmark();
		}
		break;
		
		
	}
	repaint();
}

//Управление программой в обычном режиме
private void ModeFlatDirection(int keyCode){
	
	if ((encryptCount>0) && ((keyCode != KEY_NUM3) && (keyCode != KEY_NUM4) && (keyCode != KEY_NUM9))){
		//Нажата не та клавиша не KEY_NUM3 и не KEY_NUM4 и не KEY_NUM9 - сброс...
		System.out.println("СБРОС");
		encryptCount = 0;
	}
	//System.out.println(keyCode);
	switch (keyCode){
		case RIGHT_KEY:{ //Отрисовка панелей информации
			infoPanelDraw = true;
		}
		break;
		
		case KEY_NUM1:{ //В самое лево
			contentDirection.alignToLeftBound();
		}
		break;
		
		case KEY_NUM3:{ //В самое парво
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
		
		case KEY_NUM2:{ //Вверх
			contentDirection.screenUp();
		}
		break;
		
		case KEY_NUM8:{ //вниз
			contentDirection.screenDown();
		}
		break;
		
		case KEY_NUM6:{ //право
			contentDirection.screenRight();
		}
		break;
		
		case KEY_NUM4:{ //Лево
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
		
		////////////// МАСШТАБ /////////////////////
		case KEY_NUM9:{ //Увеличение
			if (encryptCount == 0){
				contentDirection.zoomIn();
			} else {
				if (encryptCount == 3){
					//System.out.println("!!!!!!ГОТОВО!!!!!");
					bookViewer.decryptMode();
				} else {
					encryptCount = 0;
				}
			}	
		}
		break;
		
		case KEY_NUM7:{ //Уменьшение
			contentDirection.zoomOut();
		}
		break;
		////////////////////////////////////////////
		
		

		
		case KEY_POUND:{ //Переключене режима
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
		
		case JOYSTICK_CENTER:{//Вызов меню
			bookViewer.showMenu();
		}
		break;
		
		case KEY_STAR:{ //Список закладок
			bookViewer.showBookmarksListEditor();
		}
		break;				
		
		case KEY_NUM0:{ //Следующая закладка
			bookViewer.nextBookmark();
		}
		break;
		
	}
	repaint();
}


private int rotateDirection(int inCode){

	switch (inCode){
		////////////ОСНОВНОЕ УПРАВЛЕНИЕ
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
		/////////////СМЕЩЕНИЕ К КРАЮ
		case KEY_NUM3:{
			return KEY_NUM1;
		}
		case KEY_NUM9:{
			return KEY_NUM3;
		}
		//////////////////МАСШТАБ /////////////////////
		case KEY_NUM7:{
			return KEY_NUM9;
		}
		case KEY_NUM1:{
			return KEY_NUM7;
		}
		///////////////////////////////////////////////
		/////////////ДЖОЙСТИК
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
	//В зависимоти от режима меняем спопоб управления
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

//Поворот переключается зажатием клавиш 7 или 9
protected void keyRepeated(int keyCode){
	if (contentDirection.isRotate()) keyCode = rotateDirection(keyCode);
	
	if ((!repeatedKey) && (keyCode == KEY_NUM5)){
		contentDirection.rotate();
		//Отключаем повтор до отпускания клавиши
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