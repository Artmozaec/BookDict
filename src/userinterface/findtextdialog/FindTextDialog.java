package findtextdialog;

import javax.microedition.lcdui.*;
import userinterface.UserInterface;

public class FindTextDialog extends Form implements CommandListener{


private FindTextDialogListener findTextDialogListener;


private TextField findTextField;
private Command findCommand;
private Command backCommand;

public FindTextDialog(FindTextDialogListener inFindTextDialogListener, String beginFindString){
	super("find dict");
	findTextDialogListener = inFindTextDialogListener;
	
	//Поле для редакции текста который будем искать в словаре или ещё где либо
	findTextField = new TextField("что искать", beginFindString, 200, TextField.ANY);
	this.append(findTextField);
	
	findCommand = new Command("find", Command.ITEM, 0);
	this.addCommand(findCommand);
	
	backCommand = new Command("back", Command.ITEM, 0);
	this.addCommand(backCommand);
	
	this.setCommandListener(this);
}
	
public void commandAction(Command c, Displayable d) {
	if(c == findCommand){
		//Удаляем это окно из истории сохранённых окон
		UserInterface.getInstance().goToBackScreen();
		findTextDialogListener.findWordInDictionary(findTextField.getString());
	} else if (c == backCommand) {
		UserInterface.getInstance().goToBackScreen();		
	}	
}

}