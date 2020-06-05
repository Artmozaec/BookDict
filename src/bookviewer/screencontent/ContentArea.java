package screencontent;

import displaysize.DisplaySizeDirector;
class ContentArea{

private int visota;
private int shirina;

private int beginShirina;
private int beginVisota;

//Величина на которую изменяется масштаб
private int zoomStep;

//Размер малого перемещения - 1/10 экрана
private int smallMoveDelta;

ContentArea(int inVisota, int inShirina){
	zoomStep = 100; //10 - пикселей
	visota = inVisota;
	shirina = inShirina;
	
	beginVisota = DisplaySizeDirector.getInstance().getScreenVisota();
	beginShirina = DisplaySizeDirector.getInstance().getScreenShirina();
	
	culculateSmallMoveDelta();
}

private void culculateSmallMoveDelta(){
	//Размер малого перемещения
	smallMoveDelta = shirina/10;
}

public void rotateArea(){
	int tmp = visota;
	visota = shirina;
	shirina = tmp;

	
	tmp=beginVisota;
	beginVisota = beginShirina;
	beginShirina = tmp;
	
}

//////////////////////////////ИЗМЕНЕНИЕ РАЗМЕРОВ ОБЛАСТИ КОНТЕНТА/////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////



public void zoomIn(){
	//Мы приближаем изображение на экране, значит мы делаем область контента меньше, 
	//что-бы при отображении на экране её увеличили до размеров экрана
	//System.out.println("zoomIn()");
	visota = visota - beginVisota/5;
	shirina = shirina - beginShirina/5;
	culculateSmallMoveDelta();
}

public void zoomOut(){
	//visota = visota + zoomStep;
	//shirina = shirina + zoomStep;
	visota = visota + beginVisota/5;
	shirina = shirina + beginShirina/5;
	/*if ((visota>beginVisota) || (shirina>beginShirna)){
		visota = beginVisota;
		shirina = beginShirna;
	}
	*/
	culculateSmallMoveDelta();
}

public int getVisota(){
	return visota;
}

public int getShirina(){
	return shirina;
}

public int getSmallMoveDelta(){
	return smallMoveDelta;
}

}