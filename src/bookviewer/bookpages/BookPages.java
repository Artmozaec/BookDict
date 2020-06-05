package bookpages;

import javax.microedition.lcdui.Image;
import java.io.*;

//Ќазначение класса скрыть детали хранени€ страниц книги
public class BookPages{


private final String JPEG = ".jpg";
private final String GIF = ".gif";
private final String PNG = ".png";


//—пециальна€ картика нет страницы
//¬озвращаетс€ когда запрашиваемой страницы нет
private Image notPage;

public BookPages(){
	try{
		notPage = Image.createImage("/not_page.png");
	} catch(IOException e){}
}

private String getStringNumberPage(int pageNumber){
	if (pageNumber<10){
		return new String("00"+pageNumber);
	} else if (pageNumber<100){
		return new String("0"+pageNumber);
	}
	return new String()+pageNumber;
}

private boolean isExist(String patch, int pageNumber, String fileType){
	return (CheckName.checkFileExist(patch+getStringNumberPage(pageNumber)+fileType));
}

public boolean isExist(String patch, int pageNumber){
	if (isExist(patch, pageNumber, JPEG)){
		return true;
	} else if (isExist(patch, pageNumber, PNG)){
		return true;
	}
	return false;
}


public Image getCurrentPage(String patch, int pageNumber){
	if (isExist(patch, pageNumber, JPEG)){
		ImageRead imageRead = new ImageRead(patch+getStringNumberPage(pageNumber)+JPEG);
		return imageRead.getImage();
	}  else if (isExist(patch, pageNumber, PNG)){
		ImageRead imageRead = new ImageRead(patch+getStringNumberPage(pageNumber)+PNG);
		return imageRead.getImage();
	}
	return notPage;
}

}