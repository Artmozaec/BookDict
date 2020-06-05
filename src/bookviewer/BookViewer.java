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

//Текущий вид книжки - эту книжку сейчас листаем
private BookView savedBookView;

public BookViewer(ProgramDirection inProgramDirection){
	programDirection = inProgramDirection;
	
	page = new Page();
	
	//Управление сожержимым экрана
	contentDirection = new ContentDirection(page);
	
	//Создаём рисовалку
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
	
	//Управление содержимым
	contentDirection.initContentDirection(
		savedBookView.getFoldingMode(), //Режим
		//Позиция
		savedBookView.getPositionVertical(), //высота
		savedBookView.getPositionHorizontal(), //ширина
		
		//Размер области отображения
		savedBookView.getContentAreaVisota(),
		savedBookView.getContentAreaShirina(),
		
		//Границы на странице
		savedBookView.getLeftBound(),
		savedBookView.getRightBound(),
		
		//размер строки для режима переноса строк
		savedBookView.getStringVisota(),
		
		//Ключ поворота
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


//Создаёт объект вида из текущей позиции
public BookView getCurrentBookView(){
	BookView bookView = new BookView(page.getBookPatch());
	
	writeCurrentParametersToBookView(bookView);
	
	return bookView;
}

private void writeCurrentParametersToBookView(BookView bookView){
	bookView.setPageNumber(page.getCurrentPageNumber());
	bookView.setFoldingMode(contentDirection.getMode());
	//Позиция экрана
	bookView.setPositionHorizontal(contentDirection.getPositionShirina());
	bookView.setPositionVertical(contentDirection.getPositionVisota());
	
	//Размеры области отображения
	bookView.setContentAreaVisota(contentDirection.getContentAreaVisota());
	bookView.setContentAreaShirina(contentDirection.getContentAreaShirina());
	
	//Границы
	bookView.setRightBound(contentDirection.getRightBound());
	bookView.setLeftBound(contentDirection.getLeftBound());
	
	//Высота строки для режима переноса строк
	bookView.setStringVisota(contentDirection.getStringVisota());
	bookView.setRotate(contentDirection.isRotate());
}

//Были ли изменения вида?
private boolean viewIsChanged(){
	if (savedBookView.getPageNumber() != page.getCurrentPageNumber()) return true;
	if (savedBookView.getFoldingMode() != contentDirection.getMode()) return true;
	if (savedBookView.isRotate() != contentDirection.isRotate()) return true;
	//Позиция экрана
	if (savedBookView.getPositionVertical() != contentDirection.getPositionVisota()) return true;
	if (savedBookView.getPositionHorizontal() != contentDirection.getPositionShirina()) return true;
	//Размеры области отображения
	if (savedBookView.getContentAreaVisota() != contentDirection.getContentAreaVisota()) return true;
	if (savedBookView.getContentAreaShirina() != contentDirection.getContentAreaShirina()) return true;
	//Границы
	if (savedBookView.getLeftBound() != contentDirection.getLeftBound()) return true;
	if (savedBookView.getRightBound() != contentDirection.getRightBound()) return true;
	
	//Высота строки для режима переноса строк
	if (savedBookView.getStringVisota() != contentDirection.getStringVisota()) return true;
	return false;
}

//Сохранение изменённого вида
public void saveChangedView(){
	if ((savedBookView != null) && (viewIsChanged())){
		writeCurrentParametersToBookView(savedBookView);
	}
}

//Воостановить вид!
public void restoreCurrentView(){
	showBook(savedBookView);
}

//////////////////////ЦЕПОЧКА ОБЯЗАННОСТЕЙ///////////////////////

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