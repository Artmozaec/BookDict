package decryptoptions;

import javax.microedition.lcdui.*;
import userinterface.UserInterface;
import filebrowser.FileBrowserListener;

public class DecryptOptions extends Form implements CommandListener, FileBrowserListener{

private UserInterface userInterface;

private TextField beginIdentTextField;
private TextField lengthTextField;
private TextField masterPassTextField;

private Command ok;
private Command showKeyFileSelector;
private DecryptOptionsListener decryptOptionsListener;

//���� � ������� �������
private String keyPatch;



public DecryptOptions(DecryptOptionsListener listener, UserInterface inUserInterface){
	super("decrypt");
	
	userInterface = inUserInterface;
	decryptOptionsListener = listener;
	
	beginIdentTextField = new TextField("������", "", 15, TextField.NUMERIC);
	this.append(beginIdentTextField);
	
	lengthTextField = new TextField("������", "", 15, TextField.NUMERIC);
	this.append(lengthTextField);
	
	masterPassTextField = new TextField("������", "", 200, TextField.ANY);
	this.append(masterPassTextField);
	
	ok = new Command("Ok", Command.ITEM, 0);
	this.addCommand(ok);
	
	showKeyFileSelector = new Command("����", Command.BACK, 0);
	this.addCommand(showKeyFileSelector);
	
	//���������� �����!
	this.setCommandListener(this);

	keyPatch = null;
}

private void optionsFinish(){
	int ident = 0;
	int length = 0;
	try{
		ident = Integer.parseInt(beginIdentTextField.getString());
		length = Integer.parseInt(lengthTextField.getString());
	} catch (NumberFormatException ex){
		userInterface.showErrorMessage("�����?! ������������!");
		return;
	}
	
	String pass = masterPassTextField.getString();
	if (pass.length() == 0){
		userInterface.showErrorMessage("� ������?!");
		return;
	}
	
	if (keyPatch == null){
		userInterface.showErrorMessage("�� ������ ����");
		return;
	}
	decryptOptionsListener.optionsSelect(ident, length, pass, keyPatch);
	
	//���������� ������� �����
	userInterface.goToBackScreen();
}

public void commandAction(Command c, Displayable d) {
	if(c == ok){
		//�������� � ����� ���������
		optionsFinish();
	} else if (c == showKeyFileSelector) {
		userInterface.showFileBrowser(this);
	}
	
}

public void patchChoosed(String directory){
	keyPatch = directory;
	//System.out.println("Ds,hfkb ==== "+keyPatch);
	userInterface.goToBackScreen();
}


public void goToBackScreen(){
	userInterface.goToBackScreen();
}

}