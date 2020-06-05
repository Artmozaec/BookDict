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
	
	//���� ��� �������� ������ ������� ����� ������ � ������� ��� ��� ��� ����
	findTextField = new TextField("��� ������", beginFindString, 200, TextField.ANY);
	this.append(findTextField);
	
	findCommand = new Command("find", Command.ITEM, 0);
	this.addCommand(findCommand);
	
	backCommand = new Command("back", Command.ITEM, 0);
	this.addCommand(backCommand);
	
	this.setCommandListener(this);
}
	
public void commandAction(Command c, Displayable d) {
	if(c == findCommand){
		//������� ��� ���� �� ������� ���������� ����
		UserInterface.getInstance().goToBackScreen();
		findTextDialogListener.findWordInDictionary(findTextField.getString());
	} else if (c == backCommand) {
		UserInterface.getInstance().goToBackScreen();		
	}	
}

}