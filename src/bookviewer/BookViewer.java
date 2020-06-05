package bookviewer;

import javax.microedition.lcdui.*;
import screencontent.ContentDirection;
import screencontent.ContentModes;
import bookpages.Page;
import programdirection.ProgramDirection;

public class BookViewer{
private Page page;
private Painter painter;
private ContentDirection contentDirection;
private ProgramDirection programDirection;

//������� ��� ������ - ��� ������ ������ �������
private BookView savedBookView;

public BookViewer(ProgramDirection inProgramDirection){
	programDirection = inProgramDirection;
	
	page = new Page();
	
	//���������� ���������� ������
	contentDirection = new ContentDirection(page);
	
	//������ ���������
	painter = new Painter(contentDirection, this);		
}

public void goToPage(int newPage){
	page.initPage(
		savedBookView.getPatch(), 
		savedBookView.getBookName(),
		newPage
	);
}


public void showBook(BookView inBookView){
	savedBookView = inBookView;
	
	//System.out.println("showBook!!! "+savedBookView.getPatch()+ "  pn>>"+savedBookView.getPageNumber());
	page.initPage(
		savedBookView.getPatch(), 
		savedBookView.getBookName(),
		savedBookView.getPageNumber()
	);
	
	//���������� ����������
	contentDirection.initContentDirection(
		savedBookView.getFoldingMode(), //�����
		//�������
		savedBookView.getPositionVertical(), //������
		savedBookView.getPositionHorizontal(), //������
		
		//������ ������� �����������
		savedBookView.getContentAreaVisota(),
		savedBookView.getContentAreaShirina(),
		
		//������� �� ��������
		savedBookView.getLeftBound(),
		savedBookView.getRightBound(),
		
		//������ ������ ��� ������ �������� �����
		savedBookView.getStringVisota(),
		
		//���� ��������
		savedBookView.isRotate()
	);
}

public void setLineFoldingMode(int areaMode){
	if (areaMode == FoldingModeAreas.LEFT){
		contentDirection.locateLeftAreaPage();
	} else if (areaMode == FoldingModeAreas.RIGHT){
		contentDirection.locateRightAreaPage();
	} else if (areaMode == FoldingModeAreas.FULL){
		contentDirection.locateFullAreaPage();
	}
	
	contentDirection.setModeLineFolding();
}


public Displayable getDisplayable(){
	return painter;
}


//������ ������ ���� �� ������� �������
public BookView getCurrentBookView(){
	BookView bookView = new BookView(page.getBookPatch());
	
	writeCurrentParametersToBookView(bookView);
	
	return bookView;
}

private void writeCurrentParametersToBookView(BookView bookView){
	bookView.setPageNumber(page.getCurrentPageNumber());
	bookView.setFoldingMode(contentDirection.getMode());
	//������� ������
	bookView.setPositionHorizontal(contentDirection.getPositionShirina());
	bookView.setPositionVertical(contentDirection.getPositionVisota());
	
	//������� ������� �����������
	bookView.setContentAreaVisota(contentDirection.getContentAreaVisota());
	bookView.setContentAreaShirina(contentDirection.getContentAreaShirina());
	
	//�������
	bookView.setRightBound(contentDirection.getRightBound());
	bookView.setLeftBound(contentDirection.getLeftBound());
	
	//������ ������ ��� ������ �������� �����
	bookView.setStringVisota(contentDirection.getStringVisota());
	bookView.setRotate(contentDirection.isRotate());
}

//���� �� ��������� ����?
private boolean viewIsChanged(){
	if (savedBookView.getPageNumber() != page.getCurrentPageNumber()) return true;
	if (savedBookView.getFoldingMode() != contentDirection.getMode()) return true;
	if (savedBookView.isRotate() != contentDirection.isRotate()) return true;
	//������� ������
	if (savedBookView.getPositionVertical() != contentDirection.getPositionVisota()) return true;
	if (savedBookView.getPositionHorizontal() != contentDirection.getPositionShirina()) return true;
	//������� ������� �����������
	if (savedBookView.getContentAreaVisota() != contentDirection.getContentAreaVisota()) return true;
	if (savedBookView.getContentAreaShirina() != contentDirection.getContentAreaShirina()) return true;
	//�������
	if (savedBookView.getLeftBound() != contentDirection.getLeftBound()) return true;
	if (savedBookView.getRightBound() != contentDirection.getRightBound()) return true;
	
	//������ ������ ��� ������ �������� �����
	if (savedBookView.getStringVisota() != contentDirection.getStringVisota()) return true;
	return false;
}

//���������� ���������� ����
public void saveChangedView(){
	if ((savedBookView != null) && (viewIsChanged())){
		writeCurrentParametersToBookView(savedBookView);
	}
}

//������������ ���!
public void restoreCurrentView(){
	showBook(savedBookView);
}

//////////////////////������� ������������///////////////////////

public void showBookmarksListEditor(){
	programDirection.showBookmarksListEditor();
}

public void showMenu(){
	programDirection.showMenu();
}

public void showLinesFoldingAreaSelector(){
	programDirection.showLinesFoldingAreaSelector();
}


public void nextBookmark(){
	programDirection.nextBookmark();
}


public void decryptMode(){
	page.decryptMode();
}

}