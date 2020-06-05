package programdirection;

import java.util.*;
import javax.microedition.lcdui.*;

public class ScreenManager{

private static ScreenManager instance = null;

private Display display;

//��������� � ������� ���������� ������ �� ������� Displayable - ���������� ������
private Vector screens;

private ScreenManager(){
	screens = new Vector();
}

public static ScreenManager getInstance(){
	if (instance == null){
		instance = new ScreenManager();
	}
	return instance;
}

public void setDisplay(Display inDisplay){
	display = inDisplay;
}

//������� ��� ������
public void clear(){
	screens.removeAllElements();
}

//��������������� � ���������� ���������� ���������� ����
public void restoreSavedSvreen(){
	//���������� ����� ������� ������� �� ������
	if (screens.size() <= 1) return;
	//������� ������� �������
	screens.removeElementAt(screens.size()-1);
	display.setCurrent((Displayable)screens.elementAt(screens.size()-1));
	System.out.println("restoreSavedSvreen() size = "+screens.size());
}

public void showCurrent(Displayable content){
	display.setCurrent(content);
	screens.addElement(content);
	System.out.println("showCurrent size = "+screens.size());
}

}