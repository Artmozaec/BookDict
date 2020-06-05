package userinterface;
import javax.microedition.lcdui.*;
import bookviewer.FoldingModeAreas;

class LinesFoldingAreaSelector extends List implements CommandListener{
private Command ok;
private UserInterface userInterface;

private static final String[] commandArrayMap = new String[]{
	"��� ��������",
	"����� �������",
	"������ �������",
	"�����"
};

LinesFoldingAreaSelector (UserInterface inUserInterface){
	//������� ����
	super("kharkovMap", 
							List.IMPLICIT, 
							commandArrayMap,
							null);
		
	//������� ������� 
	ok = new Command("Ok", Command.ITEM, 0);
	this.addCommand(ok);
	this.setCommandListener(this);
	
	userInterface = inUserInterface;
							
}

public void commandAction(Command c, Displayable d) {
	int selectNumber = this.getSelectedIndex();
	String selectCommand = this.getString(selectNumber);

	if (selectCommand.equals("��� ��������")) {
		userInterface.setLineFoldingMode(FoldingModeAreas.FULL);
	} else if (selectCommand.equals("����� �������")){
		userInterface.setLineFoldingMode(FoldingModeAreas.LEFT);
	} else if (selectCommand.equals("������ �������")){
		userInterface.setLineFoldingMode(FoldingModeAreas.RIGHT);
	} else if (selectCommand.equals("�����")){
		userInterface.goToBackScreen();
	}
}

}