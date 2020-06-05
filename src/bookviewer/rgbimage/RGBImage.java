package rgbimage;

import javax.microedition.lcdui.Image;

public class RGBImage{
private int[][] content;
private int shirina;
private int visota;

public static int[][] oneDimensToTwoDimens(int[] inArray, int outShirina){
	int outVisota = inArray.length/outShirina;
	int[][] outArray = new int[outVisota][outShirina];
	
	int inCh=0;
	for(int outY=0; outY<outVisota; outY++){
		for (int outX=0; outX<outShirina; outX++){
			outArray[outY][outX] = inArray[inCh];
			inCh++;
		}
	}
	return outArray;
}


public static int[] twoDimensToOneDimens(int[][] inArray){
	int[] outArray = new int[inArray.length*inArray[0].length];
	
	int outCh = 0;
	for(int inY=0; inY<inArray.length; inY++){
		for (int inX=0; inX<inArray[inY].length; inX++){
			outArray[outCh] = inArray[inY][inX];
			outCh++;
		}
	}
	
	return outArray;
}

//value - на сколько опустить нижнюю границу, threshold - порог действия алгоритма
public void contrast(int value, int threshold){
	int a=0, r=0, g=0, b=0;
	for(int chY=0; chY<content.length; chY++){
		for(int chX=0; chX<content[chY].length; chX++){
			a = (content[chY][chX] >> 24) & 0xff;
			r = (content[chY][chX] >> 16) & 0xff;
			g = (content[chY][chX] >>  8) & 0xff;
			b = content[chY][chX] & 0xff;
			
			if (r<threshold){
				r = r-value;
			} 
			
			if (g<threshold){
				g = g-value;
			} 
			
			if (b<threshold){
				b = b-value;
			}
			
			if (r<0) r=0;
			if (g<0) g=0;
			if (b<0) b=0;
			
			
			content[chY][chX] = (a<<24)|(r<<16)|(g<<8)|(b);
		}
		
		
	}
}

public void rotateImage(){
	int newVisota = shirina;
	int newShirina = visota;
	int[][] newImage = new int[newVisota][newShirina];
	
	int inX=0;
	for (int outX=newShirina-1; outX>0; outX--,inX++){
		int inY=0; 
		for(int outY=0; outY<newVisota; outY++,inY++){
			newImage[outY][outX] = content[inX][inY];
		}
	}
	visota = newVisota;
	shirina = newShirina;
	content = newImage;
}

public int[][] getContent(){
	return content;
}

public ImageFragmentsBuilder getFragmentsBuilder(){
	return new ImageFragmentsBuilder(content);
}

//Конструктор создающий пустой RGBImage с размерами imageVisota, imageShirina
public RGBImage(int imageVisota, int imageShirina){
	//System.out.println("Конструктор RGBImage");
	shirina = imageShirina;
	visota = imageVisota;
	content = new int[imageVisota][imageShirina];
}


//Конструктор создающий RGBImage из одномерного массива изображения
public RGBImage(int[] inImage, int imageShirina){
	//System.out.println("Create RGBImage razmer = "+inImage.length+" ширина = "+imageShirina);
	shirina = imageShirina;
	visota = inImage.length/imageShirina;
	
	content = oneDimensToTwoDimens(inImage, imageShirina);
}


public void resample(int newVisota, int newShirina){
	visota = newVisota;
	shirina = newShirina;

	Resampler resampler = new Resampler(newVisota, newShirina, content);
	content = resampler.getResampleImage();
	//System.out.println("СЕЙЧАС ПРОЧИТАЕМ РАЗМЕРЫ!!!!!");
	//System.out.println("Контент получен, высота = "+content.length +" ширина =" +content[1].length);
}

public int getVisota(){
	return visota;
}

public int getShirina(){
	return shirina;
}

public Image getImage(){
	
	return Image.createRGBImage(twoDimensToOneDimens(content),shirina, visota, false);
}


}