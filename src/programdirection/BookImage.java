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
	//��� ��� ���� ���� ������ ����������� �� ������!!!
	notifyDestroyed(); //���������� ����������� ����������� � ����� ������
}

public void startApp(){
	//������ �� ����� ����� ����������!
	display = Display.getDisplay(this); 
	
	
	//������ ����� ����������
	ProgramDirection programDirection = new ProgramDirection(this);
}

}