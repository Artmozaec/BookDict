package rgbimage;
class Resampler{


private int[][] inImage; 
private int[][] outImage; 
private int[][] linesImage;

private int inImageVisota;
private int inImageShirina;
private int outImageVisota;
private int outImageShirina;


Resampler(int newVisota, int newShirina, int[][] image){
	inImage = image;

	
	outImageVisota = newVisota;
	outImageShirina = newShirina;
	inImageVisota = image.length;
	inImageShirina = image[0].length;

	linesImage = new int[inImageVisota][newShirina];
	outImage = new int[newVisota][newShirina];
	
	System.out.println(", высота = "+linesImage.length +" ширина =" +linesImage[1].length);
	//resample();
	//fastResample();
	
	//С начала расширяем или сужаем горизонталь, затем вертикаль
	if (outImageShirina>inImageShirina){
		//Увеличение по горизонтали
		increaseHorizontal();
	} else {
		//Уменьшение по горизонтали
		decreaseHorizontal();
	}
	
	if (outImageVisota>inImageVisota){
		//Увеличение по вертикали
		increaseVertical();
	} else {
		//Уменьшение по вертикали
		decreaseVertical();
	}
	
}


/////////////////////УВЕЛИЧЕНЕ////////////////////////////////
private void increaseHorizontal(){
	int part = inImageShirina;
	for (int k = 0; k < inImageVisota; k++) { // trough all lines
		//System.out.println("Расширение - ширина k="+k);
		int i = 0;
		int total= 0;
		int r=0, g=0, b=0, a=0;
		for (int m=0; m<outImageShirina; m++){
			//System.out.println("внутри  - i="+i);
			int R=0, G=0, B=0, A=0;
			if (total>=part){
				R=r*part; G=g*part; B=b*part; A=a*part;
				total-=part;
			}else{
				if (total != 0){
					R=r*total; G=g*total; B=b*total; A=a*total;
				}
					a = (inImage[k][i] >> 24) & 0xff;
					r = (inImage[k][i] >> 16) & 0xff;
					g = (inImage[k][i] >>  8) & 0xff;
					b =  inImage[k][i]      & 0xff;
					i++;
					int addon = part - total;
					R+=r*addon; G+=g*addon; B+=b*addon; A+=a*addon;
					total=outImageShirina - addon;
			}
///set new pixel
		linesImage[k][m]=((R/inImageShirina)<<16)|((G/inImageShirina)<<8)|(B/inImageShirina)|((A/inImageShirina)<<24); // A??
		}
	}
}


private void increaseVertical(){
	int part = inImageVisota;
	for (int k = 0; k < outImageShirina; k++) { // trough all lines
		//System.out.println("Расширение - высота k="+k);
		int i = 0;
		int j = 0;
		int total= 0;
		int r=0, g=0, b=0, a=0;
		for (int m=0; m<outImageVisota; m++){
			int R=0, G=0, B=0, A=0;
			if (total>=part){
				R=r*part; G=g*part; B=b*part; A=a*part;
				total-=part;
			}else{
				if (0!=total){
					R=r*total; G=g*total; B=b*total; A=a*total;
				}
				
				a = (linesImage[i][k] >> 24) & 0xff;
				r = (linesImage[i][k] >> 16) & 0xff;
				g = (linesImage[i][k] >>  8) & 0xff;
				b =  linesImage[i][k]        & 0xff;
				i++;
				int addon = part - total;
				R+=r*addon; G+=g*addon; B+=b*addon; A+=a;//*addon;
				total=outImageVisota - addon;
			}
	///set new pixel
			outImage[m][k]=((R/inImageVisota)<<16)|((G/inImageVisota)<<8)|(B/inImageVisota)|((A/inImageVisota)<<24);  // A??
		

		}
	}
}

/////////////////////УМЕНЬШЕНИЕ//////////////////////////////
private void decreaseHorizontal(){
	for (int k = 0; k < inImageVisota; k++) { // trough all lines
		int i = 0;
		int part = outImageShirina;
		int addon = 0, r=0, g=0, b=0, a=0;
		for (int m=0; m<outImageShirina; m++){ //Проход по всем линиям изображения
			int total = inImageShirina;
			int R=0, G=0, B=0, A=0;
			if (addon!=0){
				R=r*addon; G=g*addon; B=b*addon; A=a*addon;
				total-=addon;
			}
			while (total>0){
				a = (inImage[k][i] >> 24) & 0xff;
				r = (inImage[k][i] >> 16) & 0xff;
				g = (inImage[k][i] >>  8) & 0xff;
				b = inImage[k][i]      & 0xff;
				i++;
				if (total>part){
					R+=r*part; G+=g*part; B+=b*part; A+=a*part;
				}
				else{
					R+=r*total; G+=g*total; B+=b*total; A+=a*total;
					addon = part - total;
///set new pixel
					linesImage[k][m]=((R/inImageShirina)<<16)|((G/inImageShirina)<<8)|(B/inImageShirina)|((A/inImageShirina)<<24); // A??
				}
				total-=part;
			}
		}
	}
}

private void decreaseVertical(){
	for (int k = 0; k < outImageShirina; k++) { //Проход по всем столбцам картинки
		int i = 0;
		int part = outImageVisota;
		int addon = 0, r=0, g=0, b=0, a=0;
		for (int m=0; m<outImageVisota; m++){
			int total = inImageVisota;
			int R=0, G=0, B=0, A=0;
			if (addon!=0){
				R=r*addon; G=g*addon; B=b*addon; A=a;//*addon;
				total-=addon;
			}
			while (0<total){
				a = (linesImage[i][k] >> 24) & 0xff;
				r = (linesImage[i][k] >> 16) & 0xff;
				g = (linesImage[i][k] >>  8) & 0xff;
				b =  linesImage[i][k]        & 0xff;
				i++;
				if (total>part){
					R+=r*part; G+=g*part; B+=b*part; A+=a;//*part;
				}else{
					R+=r*total; G+=g*total; B+=b*total; A+=a;//*total;
					addon = part - total;
///set new pixel
					outImage[m][k]=((R/inImageVisota)<<16)|((G/inImageVisota)<<8)|(B/inImageVisota)|((A/inImageVisota)<<24);  // A??
				}
				total-=part;
			}
		}
	}
}

public int[][] getResampleImage(){
	return outImage;
}

}