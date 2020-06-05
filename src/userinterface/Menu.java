package userinterface;
import javax.microedition.lcdui.*;


public class Menu extends List implements CommandListener{

private Command ok;
private Command back;

private UserInterface userInterface;


private static final String[] commandArrayMap = new String[]{
	"Выход",
	"Восстановить вид",
	"Открыть",
	"На стрницу",
	"Закладки",
	"Подсветка",
	
};

Menu (UserInterface inUserInterface){
	//Создаем меню
	super("kharkovMap", 
							List.IMPLICIT, 
							commandArrayMap,
							null);
	
	
	back = new Command("Назад", Command.BACK, 0);
	this.addCommand(back);
	
	//создаем команду 
	ok = new Command("Ok", Command.ITEM, 0);
	this.addCommand(ok);

	
	
	this.setCommandListener(this);
	
	userInterface = inUserInterface;

}

public void commandAction(Command c, Displayable d) {
	int selectNumber = this.getSelectedIndex();
	String selectCommand = this.getString(selectNumber);
	
	if (c == back){
		userInterface.goToBackScreen();
		return;
	}
	
	if (selectCommand.equals("Выход")) {
		userInterface.exitProgram();
	} else if (selectCommand.equals("На стрницу")){
		userInterface.showGoToPage();
	} else if (selectCommand.equals("Открыть")){
		//Listener файл-браузера - это и есть
		userInterface.showFolderBrowser();
	} else if (selectCommand.equals("Закладки")){
		userInterface.showBookmarksListEditor();
	} else if (selectCommand.equals("Подсветка")){
		userInterface.showLightSelector();
	} else if (selectCommand.equals("Восстановить вид")){
		userInterface.restoreCurrentView();
	}
	
}

}