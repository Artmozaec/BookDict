package userinterface;
import javax.microedition.lcdui.*;
import bookviewer.FoldingModeAreas;

class LinesFoldingAreaSelector extends List implements CommandListener{
private Command ok;
private UserInterface userInterface;

private static final String[] commandArrayMap = new String[]{
	"Вся страница",
	"Левая сторона",
	"Правая сторона",
	"Назад"
};

LinesFoldingAreaSelector (UserInterface inUserInterface){
	//Создаем меню
	super("kharkovMap", 
							List.IMPLICIT, 
							commandArrayMap,
							null);
		
	//создаем команду 
	ok = new Command("Ok", Command.ITEM, 0);
	this.addCommand(ok);
	this.setCommandListener(this);
	
	userInterface = inUserInterface;
							
}

public void commandAction(Command c, Displayable d) {
	int selectNumber = this.getSelectedIndex();
	String selectCommand = this.getString(selectNumber);

	if (selectCommand.equals("Вся страница")) {
		userInterface.setLineFoldingMode(FoldingModeAreas.FULL);
	} else if (selectCommand.equals("Левая сторона")){
		userInterface.setLineFoldingMode(FoldingModeAreas.LEFT);
	} else if (selectCommand.equals("Правая сторона")){
		userInterface.setLineFoldingMode(FoldingModeAreas.RIGHT);
	} else if (selectCommand.equals("Назад")){
		userInterface.goToBackScreen();
	}
}

}