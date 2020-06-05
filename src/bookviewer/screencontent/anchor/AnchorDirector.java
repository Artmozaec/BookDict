package anchor;

import bookpages.Page;
//Класс предназначен для позиционирования границ якоря
public class AnchorDirector{
private Page page;
private Anchor anchor;

public AnchorDirector(Page inPage, int inShirina, int inVisota){
	page = inPage;
	anchor = new Anchor(inShirina, inVisota);
}

public Anchor getAnchor(){
	return anchor;
}

public void locateFullAreaPage(){
	anchor.setLeftBound(0);
	anchor.setRightBound(page.getShirina());
	anchor.setUpBound(0);
	anchor.setDownBound(page.getVisota());
}

public void locateLeftAreaPage(){
	anchor.setLeftBound(0);
	anchor.setRightBound(page.getShirina()/2);
	anchor.setUpBound(0);
	anchor.setDownBound(page.getVisota());
}

public void locateRightAreaPage(){
	anchor.setLeftBound(page.getShirina()/2);
	anchor.setRightBound(page.getShirina());
	anchor.setUpBound(0);
	anchor.setDownBound(page.getVisota());
}


private boolean isLeftLocateAreaPage(){
	if ((anchor.getLeftBound()==0) && (anchor.getRightBound() != page.getShirina())) return true;
	return false;
}

private boolean isRightLocateAreaPage(){
	if ((anchor.getRightBound() == page.getShirina()) && (anchor.getLeftBound() !=0)) return true;
	return false;
}


public void moveMiddleBoundLeft(){

	if (isLeftLocateAreaPage()){
		int currentMiddle = anchor.getRightBound();
		currentMiddle-=10;
		anchor.setRightBound(currentMiddle);
	}	
	
	if (isRightLocateAreaPage()){
		int currentMiddle = anchor.getLeftBound();
		currentMiddle-=10;
		anchor.setLeftBound(currentMiddle);
	}
}

public void moveMiddleBoundRight(){
	if (isLeftLocateAreaPage()){
		int currentMiddle = anchor.getRightBound();
		currentMiddle+=10;
		anchor.setRightBound(currentMiddle);
	}	
	
	if (isRightLocateAreaPage()){
		int currentMiddle = anchor.getLeftBound();
		currentMiddle+=10;
		anchor.setLeftBound(currentMiddle);
	}
}


}