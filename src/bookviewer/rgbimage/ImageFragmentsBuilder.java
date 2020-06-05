package rgbimage;

public class ImageFragmentsBuilder{
private int imageShirina;
private int imageVisota;
private int pointerVisota;
private int pointerShirina;
private int[][] content;

ImageFragmentsBuilder(int[][] inContent){
	//System.out.println("ImageFragmentsBuilder - конструктор");
	content = inContent;
	imageShirina = inContent[0].length;
	imageVisota = inContent.length;
	pointerVisota = 0;
	pointerShirina = 0;
}

public boolean existEmptyArea(){
	if (pointerVisota>=imageVisota) return false;
	return true;
}

private void movePointerVisota(int delta){
	pointerShirina = 0;
	pointerVisota+=delta;
	if (pointerVisota>imageVisota) pointerVisota = imageVisota;
}

private void movePointerShirina(int delta){
	pointerShirina+=delta;
}

//Указатель ширины вышел за рамки?
private boolean pointerShirinaOverBounds(){
	if (pointerShirina>=imageShirina) return true;
	return false;
}

private void addFragment(int[][] inFragment){
	//System.out.println("addFragment - inFragment высота  = "+inFragment.length+" inFragment ширина "+inFragment[0].length);
	//System.out.println("addFragment - pointerVisota = "+pointerVisota+" pointerShirina = "+pointerShirina);
	for(int inY=0; ((inY<inFragment.length) && (inY+pointerVisota<imageVisota)); inY++){
		for(int inX=0; ((inX<inFragment[inY].length) && (inX+pointerShirina<imageShirina)); inX++){
			content[inY+pointerVisota][inX+pointerShirina] = inFragment[inY][inX];
		}
	}
	
	//Смещаем указатель по ширине
	movePointerShirina(inFragment[0].length);
	
	//Если произошел выход за пределы смещаем по высоте
	if (pointerShirinaOverBounds()) movePointerVisota(inFragment.length);
}


public void addImageString(RGBImage inImageString, int shirina){
	
	//Если высота строки - 0, значит страница кончилась, выходим!
	//if (inImageString.getVisota() == 0) return;
	
	ImageString imageString = new ImageString(inImageString.getContent());
	
	int[][] fragment;
	while ((existEmptyArea()) && (!imageString.isEnded())){
		//System.out.println("addImageString - цикл");
		//Берём кусок изображения из строки размером от указателя до правой стороны экрана
		fragment = imageString.getFragment(imageShirina - pointerShirina);
		
		//Смещаем указатель на ширину imageShirina - pointerShirina
		imageString.movePointer(imageShirina - pointerShirina);
		
		//Добавляем фрагмент к общему изображению
		addFragment(fragment);
	}
}

}