package screencontent;

import bookpages.Page;
import javax.microedition.lcdui.Image;
import rgbimage.RGBImage;
import anchor.Anchor;
import userinterface.UserInterface;

public class FlatScreenContent extends AbstractScreenContent{
private Page page;
private Anchor anchor;
private ContentArea contentArea;

FlatScreenContent(Page inPage, Anchor inAnchor, ContentArea inContentArea){
	page = inPage;
	contentArea = inContentArea; 
	anchor = inAnchor;
	
	//Проверяем на выход за границы
	if (isRightOverrun()) alignToRightBound();
	if (isDownOverrun()) alignToDownBound();
	
	//Расставляем ограничители движения якоря по размеру страницы
	setAnchorBoundsInPageSize();
}

private void setAnchorBoundsInPageSize(){
	anchor.setLeftBound(0);
	anchor.setRightBound(page.getShirina());
	anchor.setUpBound(0);
	anchor.setDownBound(page.getVisota());
}

/////////////////////////////////////////РАВНЕНИЕ КОНТЕНТА////////////////////////////////
//Выравнивает область контента по левому краю страницы
public void alignToLeftBound(){
	anchor.setPositionShirina(0);
}

//Выравнивает область контента по правому краю страницы
public void alignToRightBound(){
	anchor.setPositionShirina(page.getShirina()-contentArea.getShirina());
}

//Выравнивает область контента по верхнему краю страницы
public void alignToUpBound(){
	anchor.setPositionVisota(0);
}

//Выравнивает область контента по нижнему краю страницы
public void alignToDownBound(){
	anchor.setPositionVisota(page.getVisota()-contentArea.getVisota());
}
//////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////СМЕЩЕНИЕ ЯКОРЯ///////////////////////////////////
private void moveAnchorUp(int delta){	
	//Смещаем вверх
	//Если при движении вверх - выход за границы
	if (!anchor.moveUp(delta)){
		//равняем верх экрана по верху контента
		alignToUpBound();
	}
}

private void moveAnchorDown(int delta){

	
	//Смещаем вниз
	anchor.moveDown(delta);
	
	//Если при движении вниз мы вышли за границы	
	if (anchor.getPositionVisota()+contentArea.getVisota() > page.getVisota()){
		//равняем низ экрана по низу контента
		alignToDownBound();
	}
}

private void moveAnchorLeft(int delta){
	//Смещаем в лево
	//Если при движении влево мы вышли за границы	
	if (!anchor.moveLeft(delta)){
		alignToLeftBound();
	}
}

private void moveAnchorRight(int delta){
	//Смещаем в лево
	anchor.moveRight(delta);
	
	//Если при движении влево мы вышли за границы	
	if (anchor.getPositionShirina()+contentArea.getShirina() > page.getShirina()){
		alignToRightBound();
	}
}
/////////////////////////////////////////////////////////////////////////////////////////


///////////////////////////////////////ДВИЖЕНИЕ КОНТЕНТА////////////////////////////////////
public void screenDown(){
	//Если якорь занимает нижнюю позицию, осущевстляем переход на страницу дальше
	if (anchor.getPositionVisota()+contentArea.getVisota() == page.getVisota()){
		//System.out.println("screenDown() >>>>>>>Next page ");
		page.pageDown();
		setAnchorBoundsInPageSize();
		alignToUpBound();

		//Если размер области больше размера страницы, нужно сбавить пыл!
		while ((contentArea.getShirina() > page.getShirina()) || (contentArea.getVisota() > page.getVisota())){
			//Информируем
			UserInterface ui = UserInterface.getInstance();
			ui.showInfoMessage("Предыдущая страница меньше");
			//Вертаем в зад
			contentArea.zoomIn();
		}

		//Если размеры страницы поменялись а позиця якоря перестала соответствовать реалиям суровой действительности
		if (anchor.getPositionShirina()+contentArea.getShirina() > page.getShirina()){
			alignToRightBound();
		}
		

		
		
		return;
	}
	
	int delta = contentArea.getVisota()-contentArea.getSmallMoveDelta();
	
	//Если смещение не получилось - выравниваем контент по нижнему краю
	moveAnchorDown(delta);
}


public void screenUp(){
	//Если якорь занимает верхнюю позицию, осущевстляем переход на предыдущую страницу
	if (anchor.getPositionVisota()==0){
		page.pageUp();
		setAnchorBoundsInPageSize();
		
		//Если размер области больше размера страницы, нужно сбавить пыл!
		while ((contentArea.getShirina() > page.getShirina()) || (contentArea.getVisota() > page.getVisota())){
			//Информируем
			UserInterface ui = UserInterface.getInstance();
			ui.showInfoMessage("Следующая страница меньше");
			//Вертаем в зад
			contentArea.zoomIn();
		}

		//Если якорь вышел за границы в право
		if (anchor.getPositionShirina()+contentArea.getShirina() > page.getShirina()){
			alignToRightBound();
		}
		

		
		alignToDownBound();
		return;
	}
	
	int delta = contentArea.getVisota()-contentArea.getSmallMoveDelta();
	moveAnchorUp(delta);
}

public void screenRight(){
	int delta = contentArea.getShirina()-contentArea.getSmallMoveDelta();
	moveAnchorRight(delta);
}

public void screenLeft(){
	int delta = contentArea.getShirina()-contentArea.getSmallMoveDelta();
	moveAnchorLeft(delta);
}
//////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////МАЛОЕ ДВИЖЕНИЕ КОНТЕНТА////////////////////////////////////
public void smallUp(){
	moveAnchorUp(contentArea.getSmallMoveDelta());
}

public void smallDown(){
	moveAnchorDown(contentArea.getSmallMoveDelta());
}

public void smallLeft(){
	moveAnchorLeft(contentArea.getSmallMoveDelta());
}

public void smallRight(){
	moveAnchorRight(contentArea.getSmallMoveDelta());
}
////////////////////////////////////////////////////////////////////////////////////////

//Отдаёт RGB массив изображения экрана
public RGBImage getContent(){
//Если размер области больше размера страницы, нужно сбавить пыл!
	while ((contentArea.getShirina() > page.getShirina()) || (contentArea.getVisota() > page.getVisota())){
	//Информируем
		UserInterface ui = UserInterface.getInstance();
		ui.showInfoMessage("Ой-ёй-ёй! Мало памяти");
		//Вертаем в зад
		contentArea.zoomIn();
	}
	RGBImage image = null;
	try{
	image=page.getImageArea(
		anchor.getPositionVisota(), 
		anchor.getPositionShirina(),
		contentArea.getVisota(),
		contentArea.getShirina()
	);
	} catch(OutOfMemoryError e){
			UserInterface ui = UserInterface.getInstance();
			ui.showErrorMessage("Пиздец, нет памяти");
			//Вертаем в зад
			contentArea.zoomIn();
			
			image=page.getImageArea(
				anchor.getPositionVisota(), 
				anchor.getPositionShirina(),
				contentArea.getVisota(),
				contentArea.getShirina()
			);
	}
	return image;
}






//////////////////////////////////////////////ВЫСТУПЫ ЯКОРЯ///////////////////////////////////////////

//выступ с права
public boolean isRightOverrun(){
	//Если сумма текущей позиции якоря по ширине плюс размер экрана больше чем размер страницы
	if ((anchor.getPositionShirina()+contentArea.getShirina())>page.getShirina()) return true;
	return false;
}

//выступ с низу
public boolean isDownOverrun(){
	//Если сумма текущей позиции якоря по высоте плюс высота экрана больше чем размер страницы
	if ((anchor.getPositionVisota()+contentArea.getVisota())>page.getVisota()) return true;
	return false;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////



}