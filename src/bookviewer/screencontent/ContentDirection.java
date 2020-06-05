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
private ContentArea сontentArea;
private AbstractScreenContent screenContent;
//Создаёт двигатель якоря, управляет высотой разбиваемых строк
private AnchorMoverDirector anchorMoverDirector;

//Признак поворота
private boolean rotate;

//Текущий режим программы
private int currentMode;


public ContentDirection(Page inPage){
	page = inPage;
	currentMode = ContentModes.MODE_FLAT;
	
	//Фабрика двигателей якоря
	anchorMoverDirector = new AnchorMoverDirector(10);

	//Создаём объект размера отображаемой области
	сontentArea = new ContentArea(
		DisplaySizeDirector.getInstance().getScreenVisota(), 
		DisplaySizeDirector.getInstance().getScreenShirina()
	);
	
	//Создаём управление якорем
	anchorDirector = new AnchorDirector(page, 0, 0);
	
	//Создаём текущее содержимое экрана
	createScreenContent();
}

public void initContentDirection(
	int mode, 
	int anhorVisota, 
	int anhorShirina,
	
	int сontentAreaVisota,
	int сontentAreaShirina,
	
	int leftBound,
	int rightBound,
	
	int stringVisota,
	boolean inRotate
){
	rotate = inRotate;
	//currentMode = mode;
	currentMode = mode;
	//Фабрика двигателей якоря
	anchorMoverDirector = new AnchorMoverDirector(stringVisota);
	

	
	//Создаём объект размера отображаемой области
	сontentArea = new ContentArea(сontentAreaVisota, сontentAreaShirina);
	
	//Создаём управление якорем
	anchorDirector = new AnchorDirector(page, anhorShirina, anhorVisota);
	
	//Без этого не будет работать режим с переносом строк
	locateFullAreaPage();
	
	//Создаём текущее содержимое экрана
	createScreenContent();

	//Если границы корректны Устанавливаем
	if ((leftBound>=0) && (rightBound>=0)){
		anchorDirector.getAnchor().setLeftBound(leftBound);
		anchorDirector.getAnchor().setRightBound(rightBound);
	}
}


//Устанавливает ограничитель движения якоря 
private void setAnchorBoundFromPageSize(){
}

public AbstractScreenContent getContent(){
	return screenContent;
}



///////////////////////РАЗМЕРЫ ОБЛАСТИ////////////////////////////

public int getContentAreaVisota(){
	return сontentArea.getVisota();
}

public int getContentAreaShirina(){
	return сontentArea.getShirina();
}
/////////////////////ЯКОРЬ/////////////////////////////////

//Возвращает положение якоря по ширине
public int getPositionShirina(){
	return anchorDirector.getAnchor().getPositionShirina();
}

//Возвращает положение якоря по высоте
public int getPositionVisota(){
	return anchorDirector.getAnchor().getPositionVisota();
}

public int getLeftBound(){
	return anchorDirector.getAnchor().getLeftBound();
}

public int getRightBound(){
	return anchorDirector.getAnchor().getRightBound();
}
/////////////////////ДРУГИЕ ПАРАМЕТРЫ////////////////////
public int getStringVisota(){
	return anchorMoverDirector.getStringVisota();
}




/////////////////////////БОЛЬШОЕ_ДВИЖЕНИЕ////////////////////////
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



///////////////////////////МАЛОЕ_ДВИЖЕНИЕ///////////////////////
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



/////////////////////РАВНЕНИЕ_КОНТЕНТА/////////////////////////////
public void alignToLeftBound(){
	screenContent.alignToLeftBound();
}

public void alignToRightBound(){
	screenContent.alignToRightBound();
}

/////////////////////ВЫСОТА_РАЗДЕЛИТЕЛЯ_СТРОКИ/////////////////////////
public void addStringVisota(){
	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!addStringVisota()");
	anchorMoverDirector.addStringVisota();
}

public void subStringVisota(){
	anchorMoverDirector.subStringVisota();
}



////////////////////////////ПОВОРОТ//////////////////////////////////
public void rotate(){
	//Поворачиваем рабочую область
	сontentArea.rotateArea();
	
	//Размер области изменился
	while ((сontentArea.getShirina() > page.getShirina()) || (сontentArea.getVisota() > page.getVisota())){
		//Информируем
		UserInterface ui = UserInterface.getInstance();
		ui.showInfoMessage("Не помещается, увеличили!!!");
		//Вертаем в зад
		сontentArea.zoomIn();
	}
	
	//В результате переворота области, его край может выступить за границы страницы
	//Проверяем положение края и если выход, выравниваем по краю
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
//////////////////////////////РЕЖИМ КОНТЕНТА

private void createScreenContent(){
	if (currentMode == ContentModes.MODE_FLAT){
		screenContent = new FlatScreenContent(page, anchorDirector.getAnchor(), сontentArea);
	} else if (currentMode == ContentModes.MODE_LINE_FOLDING){
		screenContent = new LineFoldingScreenContent(page, anchorDirector.getAnchor(), сontentArea, anchorMoverDirector);
	} else {
		screenContent = new FlatScreenContent(page, anchorDirector.getAnchor(), сontentArea);
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

	//Если режим поворота
	if (rotate){
		content.rotateImage();
	}
	
	
	//Если текущие размеры области контента не соответствуют размерам экрана - делаем ресемплинг
	if ((needShirina!=сontentArea.getShirina())||(needVisota!=сontentArea.getVisota())){
		//System.out.println("!!!!!!!!!РЕСЕМПЛИНГ!!!!!!!!!");
		//System.out.println("требуемая высота = "+needVisota+" требуемая ширина ="+needShirina);
		//System.out.println("текущая высота = "+сontentArea.getVisota()+" текущая ширина ="+сontentArea.getShirina());
		content.resample(needVisota, needShirina);
		
		//Если вдобавок ко всему перенос строк, это значит подбавить контрасту!
		//100 - значение, 150- порог
		if (currentMode == ContentModes.MODE_LINE_FOLDING) content.contrast(100, 150);
	}
	

	return content.getImage();
}

///////////////////////////МАСШТАБ/////////////////////////////
public void zoomIn(){
	сontentArea.zoomIn();
}

public void zoomOut(){
	сontentArea.zoomOut();
	//Размер области увеличился, если он станет больше размера экрана будет пиздец!
	if ((сontentArea.getShirina() > page.getShirina()) || (сontentArea.getVisota() > page.getVisota())){
		//Информируем
		UserInterface ui = UserInterface.getInstance();
		ui.showInfoMessage("Дальше некуда!!!");
		//Вертаем в зад
		сontentArea.zoomIn();
	}
	if (screenContent.isRightOverrun()) screenContent.alignToRightBound();
	if (screenContent.isDownOverrun()) screenContent.alignToDownBound();
}

///////////////////////////////////////////////////////////////

//////////////////////////////////////////

//Необходимо инкапсулировать локализацию до вызова соответствующего метода из anchor!!!
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
////////////////////////////СМЕЩЕНИЕ СРЕДНЕЙ ГРАНИЦЫ////////////////////////


public void moveMiddleBoundLeft(){
	anchorDirector.moveMiddleBoundLeft();
}

public void moveMiddleBoundRight(){
	anchorDirector.moveMiddleBoundRight();
}
////////////////////////////////////////////////////////////////////////////
////////////////////////////СДВИГ ТЕКСТА///////////////////////////

public void textMoveLeft(){
	Anchor anchor = anchorDirector.getAnchor();
	anchor.moveLeft(5);
}

public void textMoveRight(){
	Anchor anchor = anchorDirector.getAnchor();
	anchor.moveRight(5);
}

}