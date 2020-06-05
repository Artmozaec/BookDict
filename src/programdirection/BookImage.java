package programdirection;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;


public class BookImage extends MIDlet{


private Display display;



public Display getDisplay(){
	return display;
}

public void pauseApp() {
}

public void destroyApp(boolean dede){
	//¬от без этой хери мидлет закрыватьс€ не желает!!!
	notifyDestroyed(); //”ведомл€ет программное обеспечение о конце работы
}

public void startApp(){
	//ссылка на экран стала √ЋќЅјЋ№Ќќ…!
	display = Display.getDisplay(this); 
	
	
	//—оздаЄм класс управлени€
	ProgramDirection programDirection = new ProgramDirection(this);
}

}