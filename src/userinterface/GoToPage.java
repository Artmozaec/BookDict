package userinterface;

import javax.microedition.lcdui.*;

class GoToPage extends Form implements CommandListener{
private UserInterface userInterface;
private Command ok;
private Command cancel;
private TextField needPage;


GoToPage(UserInterface inUserInterface){
	super("goToPage");

	userInterface = inUserInterface;
	
	needPage = new TextField("�� ��������", "", 5, TextField.NUMERIC);
	
	this.append(needPage);
	
	
	//�������
	ok = new Command("Ok", Command.ITEM, 0);
	cancel = new Command("cancel", Command.ITEM, 0);
	
	//��������� ��������
	this.addCommand(ok);
	this.addCommand(cancel);
	
	//���������� �����!
	this.setCommandListener(this);
}

public void commandAction(Command c, Displayable d) {
	if(c==ok){
		userInterface.goToPage(Integer.parseInt(needPage.getString()));
	} else if (c==cancel) {
		userInterface.goToBackScreen();
	}
	
}

}