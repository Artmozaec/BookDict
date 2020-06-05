package screencontent;

import rgbimage.RGBImage;
import bookpages.Page;
import rgbimage.ImageFragmentsBuilder;
import anchormover.AnchorMoverDirector;
import anchormover.AnchorMover;
import anchor.Anchor;


class LineFoldingScreenContent extends AbstractScreenContent{
private Page page;
private Anchor anchor;
private ContentArea contentArea;
private AnchorMoverDirector anchorMoverDirector;


private int columnLeftBound;
private int columnRightBound;
private int columnLength;



LineFoldingScreenContent(Page inPage, Anchor inAnchor, ContentArea inContentArea, AnchorMoverDirector inAnchorMoverDirector){
	page = inPage;
	contentArea = inContentArea;
	anchor = inAnchor;
	anchorMoverDirector = inAnchorMoverDirector;
	
	columnLeftBound = 0;
	columnRightBound = page.getShirina();
	columnLength = columnRightBound - columnLeftBound;
	//System.out.println("LineFoldingScreenContent");
}

///////////////////////////////////////ДВИЖЕНИЕ КОНТЕНТА////////////////////////////////////
public void screenDown(){
	//Создаём валкер
	LineFoldingScreenWalker screenWalker = new LineFoldingScreenWalker(anchorMoverDirector, contentArea);
	//screenWalker.moveDovnInPixelsFoldingString(anchor, contentArea.getVisota() - (contentArea.getVisota()/5));
	screenWalker.moveDovnInPixelsFoldingString(anchor, contentArea.getVisota() - (contentArea.getVisota()/3));
}

public void screenUp(){
	//Создаём валкер
	LineFoldingScreenWalker screenWalker = new LineFoldingScreenWalker(anchorMoverDirector, contentArea);
	//screenWalker.moveDovnInPixelsFoldingString(anchor, contentArea.getVisota() - (contentArea.getVisota()/5));
	screenWalker.moveUpInPixelsFoldingString(anchor, contentArea.getVisota() - (contentArea.getVisota()/3));
}


public void screenLeft(){

}

public void screenRight(){

}
//////////////////////////////////////////////////////////////////////////////////////////////




private void fillRGBImage(StringSplitter stringSplitter, ImageFragmentsBuilder imageBuilder){
	//System.out.println("fillRGBImage");
	while (imageBuilder.existEmptyArea()){
		//System.out.println("fillRGBImage");
		//Получаем текущую строку-изображение из страницы
		RGBImage imageString = stringSplitter.getString();
		
		//Если высота строки - 0, значит страница кончилась, выходим! !!!!!!!!УДОЛИТЬ НАХуЙ ПоТоМ!!!!
		//System.out.println("imageString.getVisota() = "+imageString.getVisota());
		if (imageString.getVisota() == 0) return;
		
		//Добавляем эту строку к изображению
		imageBuilder.addImageString(imageString, columnLength);
		
		//Продвигаем сплиттер на одну строку в низ и если оно не сдвигается, тогда выходим
		if (!stringSplitter.nextString()) return;
	}
}

public RGBImage getContent(){
	//создаём итератор
	//System.out.println("LineFoldingScreenContent - getContent()");
	
	AnchorMover anchorMover = anchorMoverDirector.createAnchorMover(anchor.getDublicate());
	
	StringSplitter stringSplitter = new StringSplitter(anchorMover, page);
	
	//создаём RGBImage
	RGBImage rgbimage = new RGBImage(contentArea.getVisota(), contentArea.getShirina());
	
	//достаём строитель
	ImageFragmentsBuilder bulider = rgbimage.getFragmentsBuilder();
	
	//Заполняем RGBImage строками
	fillRGBImage(stringSplitter, bulider);
	
	//System.out.println("СОЗДАЛИ КАРТИНКУ ЭКРАНА!!!!!!!!!!!!");
	//Возвращаем результат
	return rgbimage;
}








///////////////////////////////////////МАЛОЕ ДВИЖЕНИЕ КОНТЕНТА////////////////////////////////////
public void smallUp(){
	anchor.moveUp(1);
}
public void smallDown(){
	anchor.moveDown(1);
}
public void smallLeft(){
}
public void smallRight(){
}
///////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////РАВНЕНИЕ КОНТЕНТА////////////////////////////////
public void alignToLeftBound(){
}
public void alignToRightBound(){
}
public void alignToUpBound(){
}
public void alignToDownBound(){
}
//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////ВЫСТУПЫ ЯКОРЯ///////////////////////////////////////////
public boolean isRightOverrun(){
	return false;
}
public boolean isDownOverrun(){
	return false;
}
/////////////////////////////////////////////////////////////////////////////////////////
}
