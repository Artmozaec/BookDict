package userinterface;

import javax.microedition.lcdui.*;

class TextEditor extends Form implements CommandListener{
private UserInterface userInterface;
private Command ok;
private Command cancel;
private TextField textField;
private TextEditorListener textEditorListener;

TextEditor(UserInterface inUserInterface, TextEditorListener inTextEditorListener, String beginText){
	super("TextEditor");

	textEditorListener = inTextEditorListener;
	userInterface = inUserInterface;
	
	textField = new TextField("�", beginText, 100, TextField.ANY);
	
	this.append(textField);
	
	
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
		textEditorListener.textInputIsFinished(textField.getString());
	} else if (c==cancel) {
		//userInterface.goToBackScreen();
	}
	
}

}