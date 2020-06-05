package userinterface;
import javax.microedition.lcdui.*;


public class Menu extends List implements CommandListener{

private Command ok;
private Command back;

private UserInterface userInterface;


private static final String[] commandArrayMap = new String[]{
	"�����",
	"������������ ���",
	"�������",
	"�� �������",
	"��������",
	"���������",
	
};

Menu (UserInterface inUserInterface){
	//������� ����
	super("kharkovMap", 
							List.IMPLICIT, 
							commandArrayMap,
							null);
	
	
	back = new Command("�����", Command.BACK, 0);
	this.addCommand(back);
	
	//������� ������� 
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
	
	if (selectCommand.equals("�����")) {
		userInterface.exitProgram();
	} else if (selectCommand.equals("�� �������")){
		userInterface.showGoToPage();
	} else if (selectCommand.equals("�������")){
		//Listener ����-�������� - ��� � ����
		userInterface.showFolderBrowser();
	} else if (selectCommand.equals("��������")){
		userInterface.showBookmarksListEditor();
	} else if (selectCommand.equals("���������")){
		userInterface.showLightSelector();
	} else if (selectCommand.equals("������������ ���")){
		userInterface.restoreCurrentView();
	}
	
}

}