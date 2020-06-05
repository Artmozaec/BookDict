package bookpages;

import javax.microedition.lcdui.*;
import userinterface.UserInterface;
import bookpages.BookPages;
import rgbimage.RGBImage;
import displaysize.DisplaySizeDirector;
//import decrypter.Decrypter;

public class Page{

private BookPages bookPages;
private Image currentPageImage;

private String bookPatch;
private String bookFolder;

//private Decrypter decrypter;
private int pageNumber;
private int screenVisota;
private int screenShirina;

public Page(){
	//������ pages
	bookPages = new BookPages();
	bookPatch = "file:///E:/";
	pageNumber = 0;
	

	screenVisota = DisplaySizeDirector.getInstance().getScreenVisota();
	screenShirina = DisplaySizeDirector.getInstance().getScreenShirina();
	//decrypter = null;
	loadPageImage();
}

public void decryptMode(){
	//decrypter = new Decrypter();
	//decrypter.setFolderKey(bookFolder);
}

public void initPage(String patch, String folderKey, int beginPageNumber){
	//System.out.println("init page page = "+beginPageNumber+" patch = "+patch);
	clearMemory();
	bookPatch = patch;
	bookFolder = folderKey;
	pageNumber = beginPageNumber;
	loadPageImage();	
/*	
	if (decrypter != null){
		decrypter.setFolderKey(folderKey);
		decrypter.goToPage(pageNumber);
	}
*/
}


public String getBookPatch(){
	//���� ���� ���, �� ���������� null
	if (!bookPages.isExist(bookPatch, pageNumber)) return null;
	return bookPatch;
}

public int getCurrentPageNumber(){
	return pageNumber;
}

//////////////////////������ � ���������////////////////////////////////////
public void loadPageImage(){
		//System.out.println("loadPageImage() page = "+pageNumber+" patch = "+bookPatch);
		currentPageImage = bookPages.getCurrentPage(bookPatch, pageNumber);
}

//���������� ������� � ���� RGBImage
public RGBImage getImageArea(int beginVisota, int beginShirina, int visota, int shirina){
	int[] rgb; 
	//System.out.println("getImageArea beginVisota = "+beginVisota+" beginShirina = "+beginShirina);
	//System.out.println("getImageArea visota = "+visota+" shirina = "+shirina);
	
	//���� ������� ��������� ������� ������ ��� ������ �������� ����� ��� ����������� ��������
	
	if ((shirina<=currentPageImage.getWidth()) && (visota<=currentPageImage.getHeight())){
		rgb = new int[visota*shirina];
		currentPageImage.getRGB(rgb, //������������ ������
			0, //��������
			shirina, //������, �����-��...? ���� �� � ��� ��������� ������ � ������! ������� ���� ��������?
			beginShirina,
			beginVisota,
			shirina,
			visota
		);
/*		
		//System.out.println("decrypter = "+decrypter);
		if (decrypter != null){
			
			//������������� ��� ����� ������� ��������
			decrypter.setImageSize(currentPageImage.getHeight(), currentPageImage.getWidth());
			decrypter.decrypt(rgb, shirina, beginVisota, beginShirina);
		}
*/		
		return new RGBImage(rgb, shirina);
	
	} else { //������� �������� ������ ��� �����... ������������
		rgb = new int[currentPageImage.getHeight()*currentPageImage.getWidth()];
		currentPageImage.getRGB(rgb, //������������ ������
			0, //��������
			currentPageImage.getWidth(), //������, �����-��...? ���� �� � ��� ��������� ������ � ������! ������� ���� ��������?
			0,
			0,
			currentPageImage.getWidth(),
			currentPageImage.getHeight()
		);
		RGBImage resampleImage = new RGBImage(rgb, currentPageImage.getWidth());
		resampleImage.resample(visota, shirina);
		return resampleImage;
	}
	
}


public void clearMemory(){
	Runtime rt = Runtime.getRuntime();
	currentPageImage=null;
	rt.gc();
}
///////////////////////////////////////////////////////////////////////////


///////////////////////////�������� �������/////////////////////////////
public int getVisota(){
	if (currentPageImage.getHeight()<screenVisota) return screenVisota;
	return currentPageImage.getHeight();
}
//������ ������������ �������� �� ������ ������ ��� ������� ������!
//�������� ������ ������� ������ ����������� �� ������� ������
public int getShirina(){
	if (currentPageImage.getWidth()<screenShirina) return screenShirina;
	return currentPageImage.getWidth();
}
///////////////////////////////////////////////////////////////////////



////////////////////�������� ����� ����////////////////////////////////
public void pageUp(){
	pageNumber--;
/*	
	if (decrypter != null){
		decrypter.goToPage(pageNumber);
	}
*/
	clearMemory();
	
	//�������� ������� ��������
	loadPageImage();
}

public void pageDown(){
/*
	if (decrypter != null){
		decrypter.nextPage();
	}
*/
	pageNumber++;
	
	clearMemory();
	
	//�������� ������� ��������
	loadPageImage();
}
///////////////////////////////////////////////////////////////////////
}