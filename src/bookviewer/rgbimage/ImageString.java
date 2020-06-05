package rgbimage;

class ImageString{
private int[][] imageArray;
private int stringShirina;
private int stringVisota;
private int pointer;



ImageString(int[][] inImageArray){
	//System.out.println("ImageString inImageArray высота = "+inImageArray.length+" ширина = ");
	imageArray = inImageArray;
	
	//Высота изображения
	stringVisota = imageArray.length;
	//ширина
	stringShirina = imageArray[0].length;

}

public void movePointer(int length){
	pointer +=length;
	if (pointer>=stringShirina) pointer = stringShirina;
}

//Строка закончилась?
public boolean isEnded(){
	if(pointer==stringShirina) return true;
	return false;
}

public int[][] getFragment(int length){
	
	//Если очень значение длинны выходит за границы строки, ограничиваем его допустимой длинной
	if (length+pointer>stringShirina) length = stringShirina-pointer;
	
	int[][] fragment = new int[stringVisota][length];
	//System.out.println("ImageString - getFragment ширина = "+length);
	for(int chY=0; chY<fragment.length; chY++){
		for(int chX=0; chX<fragment[chY].length; chX++){
			fragment[chY][chX] = imageArray[chY][pointer+chX];
		}
	}
	
	return fragment;
}

}