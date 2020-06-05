package userinterface;
import javax.microedition.lcdui.*;

public class LightSelector extends List implements CommandListener{

private static final String[] lightGradations = new String[]{
	"0",
	"5",
	"10",
	"20",
	"30",
	"40",
	"50",
	"60",	
	"70",
	"80",
	"90",
	"100"
};


private Command back;

private UserInterface userInterface;

//Выделение текущего значения подсветки
public void selectCurrentLightValue(int value){
	String stringValue = Integer.toString(value);
	
	for (int ch=0; ch<this.size(); ch++){
		if (stringValue.equals(this.getString(ch))) this.setSelectedIndex(ch, true);
	}
}

LightSelector (UserInterface inUserInterface){
	//Создаем меню
	super("lightselector", 
							List.IMPLICIT, 
							lightGradations,
							null);
	

	back = new Command("Назад", Command.BACK, 0);
	this.addCommand(back);
	
	this.setCommandListener(this);
	
	userInterface = inUserInterface;

}

public void commandAction(Command c, Displayable d) {
	if(c==back){
		userInterface.goToBackScreen();
		return;
	}
	
	int selectNumber = this.getSelectedIndex();
	String selectCommand = this.getString(selectNumber);
	userInterface.setLight(Integer.valueOf(selectCommand).intValue());
}
}