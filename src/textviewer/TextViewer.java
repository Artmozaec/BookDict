package textviewer;

import javax.microedition.lcdui.*;
import programdirection.ProgramDirection;
import bookviewer.BookView;

public class TextViewer{
private ScreenContent screenContent;
private TextPainter textPainter;
private ProgramDirection programDirection;
private BookView currentView;

static private final int TEXT_BUFER_SIZE = 10000;

public TextViewer(ProgramDirection inProgramDirection){
	programDirection = inProgramDirection;
	currentView = null;
}

//Сохранение изменённого вида
public void saveChangedView(){
	if (currentView == null) return;
	currentView.setPageNumber(screenContent.getPositionInFile());
}

public void showText(BookView newView){
	System.out.println("showText >> patch = "+newView.getPatch()+" posInfile = "+newView.getPageNumber());
	screenContent = new ScreenContent(
		newView.getPatch(), 
		newView.getPageNumber(), 
		TEXT_BUFER_SIZE
	);
	currentView = newView;
	textPainter = new TextPainter(screenContent, this);
}

public Displayable getDisplayable(){
	return textPainter;
}

public void findWordInDictionary(String findWord){
	//System.out.println("find word >>>>>>"+findWord);
	programDirection.findWordInDictionary(findWord);
}

}