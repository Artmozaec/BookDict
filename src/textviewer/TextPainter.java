package textviewer;
import javax.microedition.lcdui.*;
import displaysize.DisplaySizeDirector;
import textselector.TextSelector;
import textselector.Word;
import programdirection.ScreenManager;
import userinterface.UserInterface;

public class TextPainter extends Canvas{

//������ ������ 
private static final int TEXT_READ_MODE = 0;
private static final int TEXT_SELECT_MODE = 1;
private int mode;



private static final int WHITE = 0xFFFFFF;
private static final int ORANGE = 0xFFA800;
private static final int BLACK = 0x000000;

//�������� ���������
private static final int JOYSTICK_UP = -1;
private static final int JOYSTICK_DOWN = -2;
private static final int JOYSTICK_LEFT = -3;
private static final int JOYSTICK_RIGHT = -4;
private static final int JOYSTICK_CENTER = -5;

private static final int RIGHT_KEY = -7;
private static final int LEFT_KEY = -6;


private int screenHeight;
private int screenWidth;

private TextSelector textSelector;
private TextViewer textViewer;

ScreenContent screenContent;


public TextPainter(ScreenContent inscreenContent, TextViewer inTextViewer){
	textViewer = inTextViewer;
	screenContent = inscreenContent;
	setFullScreenMode(true);
	//����� ������� ������ ��� ���������� ����������� �� �� ������
	DisplaySizeDirector displaySize = DisplaySizeDirector.getInstance();
	screenHeight =displaySize.getScreenVisota();
	screenWidth = displaySize.getScreenShirina();
	mode = TEXT_READ_MODE;
	textSelector = null;
}

private void paintTextToScreen(Graphics g){
	//������� �����
	g.setColor(WHITE);
	g.fillRect(0,0,500,500);
	
	g.setColor(BLACK);
	//�������� ������ �����
	g.setFont(screenContent.getCurrentFont());
	//������ ������
	int lineHeight = screenContent.getLineHeight();
	//������� ������ �� ���������
	int posY = 0;
	//������� �����
	int stringCh = 0;
	
	String str;
	while(posY < screenHeight){
		str = screenContent.getLine(stringCh);
		//���� NULL, ������ ������ ��� ���������
		if (str == null) return;
		
		g.drawString(str, 0, posY, g.TOP|g.LEFT);
		
		stringCh++;
		posY += lineHeight;
	}
	
	
}

//��������� ������ ���������
private void paintSelection(Graphics g){
	System.out.println("paintSelection");
	g.setColor(ORANGE);
	
	//������������� �������� � ������
	textSelector.wordIteratorReset();
	
	Word currentWord = textSelector.nextWord();
	while(currentWord != null){
		//System.out.println("paintSelection ������ >>>"+currentWord.getString());
		g.drawString(currentWord.getString(), currentWord.getPosX(), currentWord.getPosY(), g.TOP|g.LEFT);
		currentWord = textSelector.nextWord();
	}	
}

public void paint(Graphics g){
	paintTextToScreen(g);
	if (mode == TEXT_SELECT_MODE) paintSelection(g);
}

private void keyPressedTextReadMode(int keyCode){
	switch (keyCode){
		case KEY_NUM8:{ //���� �����
			screenContent.screenDown();
		} 
		break;
		
		case JOYSTICK_DOWN:{ //���� �����
			screenContent.lineDown();
		} 
		break;
		
		case KEY_NUM2:{ //����� �����
			screenContent.screenUp();
		} 
		break;
		
		case JOYSTICK_UP:{ //����� �����
			screenContent.lineUp();
		} 
		break;
		
		case KEY_NUM9:{ //��������� �����
			screenContent.fontIncrease();
		} 
		break;
		
		case KEY_NUM7:{ //��������� �����
			screenContent.fontDecrease();
		} 
		break;
		
		case KEY_NUM5:{ //����� ��������� ������
			mode = TEXT_SELECT_MODE;
			textSelector = new TextSelector(screenContent);
		}
		break;
		case LEFT_KEY:{ //�����, � ����������� ������
			ScreenManager.getInstance().restoreSavedSvreen();
		}
		break;
		case KEY_STAR:{ //������ ��������
			UserInterface.getInstance().showBookmarksListEditor();
		}
		break;
		case JOYSTICK_CENTER:{//����� ����
			UserInterface.getInstance().showMenu();
		}
	}
}


private void keyPressedTextSelectMode(int keyCode){
	switch (keyCode){
		
		case KEY_NUM2:{ //����� �����
			textSelector.moveUp();
		} 
		break;
		
		case KEY_NUM8:{ //����� � ���
			textSelector.moveDown();
		} 
		break;
		
		case KEY_NUM6:{ //����� � �����
			textSelector.moveRight();
		} 
		break;
		
		case KEY_NUM4:{ //����� � ����
			textSelector.moveLeft();
		} 
		break;
		
		case KEY_NUM3:{ //���������� ������� � �����
			textSelector.expandArea();
		} 
		break;		
		
		case KEY_NUM1:{ //������� �������
			textSelector.contractionArea();
		} 
		break;
		
		case KEY_NUM5:{ //����� � �������
			textViewer.findWordInDictionary(textSelector.getSelectedText());
		} 
		break;
		
		case KEY_NUM0:{ //������ ������ � �������
			UserInterface.getInstance().ShowFindTextDialog(textSelector.getSelectedText());
		} 
		break;
		
		case KEY_NUM7:{ //����� �� ������ ���������
			mode = TEXT_READ_MODE;
			textSelector = null;
		} 
		break;
		
		case KEY_NUM9:{ //����� �� ������ ���������
			mode = TEXT_READ_MODE;
			textSelector = null;
		} 
		break;
		
		case JOYSTICK_CENTER:{//����� ����
			UserInterface.getInstance().showMenu();
		}
	}
}


public void keyPressed(int keyCode) {
	switch (mode){
		case TEXT_READ_MODE:{
			keyPressedTextReadMode(keyCode);
		} 
		break;
		case TEXT_SELECT_MODE:{
			keyPressedTextSelectMode(keyCode);
		} 
		break;
	}
	repaint();
}


}