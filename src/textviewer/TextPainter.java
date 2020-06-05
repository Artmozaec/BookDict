package textviewer;
import javax.microedition.lcdui.*;
import displaysize.DisplaySizeDirector;
import textselector.TextSelector;
import textselector.Word;
import programdirection.ScreenManager;
import userinterface.UserInterface;

public class TextPainter extends Canvas{

//РЕЖИМЫ работы 
private static final int TEXT_READ_MODE = 0;
private static final int TEXT_SELECT_MODE = 1;
private int mode;



private static final int WHITE = 0xFFFFFF;
private static final int ORANGE = 0xFFA800;
private static final int BLACK = 0x000000;

//Сканкоды джойстика
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
	//Узнаём размеры экрана для правильной расстановки на нём текста
	DisplaySizeDirector displaySize = DisplaySizeDirector.getInstance();
	screenHeight =displaySize.getScreenVisota();
	screenWidth = displaySize.getScreenShirina();
	mode = TEXT_READ_MODE;
	textSelector = null;
}

private void paintTextToScreen(Graphics g){
	//Очищаем экран
	g.setColor(WHITE);
	g.fillRect(0,0,500,500);
	
	g.setColor(BLACK);
	//Изменяем теущий шрифт
	g.setFont(screenContent.getCurrentFont());
	//Высота строки
	int lineHeight = screenContent.getLineHeight();
	//Позиция вывода по вертикали
	int posY = 0;
	//Счётчик строк
	int stringCh = 0;
	
	String str;
	while(posY < screenHeight){
		str = screenContent.getLine(stringCh);
		//Если NULL, значит строки уже кончились
		if (str == null) return;
		
		g.drawString(str, 0, posY, g.TOP|g.LEFT);
		
		stringCh++;
		posY += lineHeight;
	}
	
	
}

//Отрисовка режима выделения
private void paintSelection(Graphics g){
	System.out.println("paintSelection");
	g.setColor(ORANGE);
	
	//Устанавливаем итератор в начало
	textSelector.wordIteratorReset();
	
	Word currentWord = textSelector.nextWord();
	while(currentWord != null){
		//System.out.println("paintSelection рисуем >>>"+currentWord.getString());
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
		case KEY_NUM8:{ //вниз экран
			screenContent.screenDown();
		} 
		break;
		
		case JOYSTICK_DOWN:{ //вниз линия
			screenContent.lineDown();
		} 
		break;
		
		case KEY_NUM2:{ //вверх экран
			screenContent.screenUp();
		} 
		break;
		
		case JOYSTICK_UP:{ //вверх линия
			screenContent.lineUp();
		} 
		break;
		
		case KEY_NUM9:{ //увеличить шрифт
			screenContent.fontIncrease();
		} 
		break;
		
		case KEY_NUM7:{ //уменьшить шрифт
			screenContent.fontDecrease();
		} 
		break;
		
		case KEY_NUM5:{ //Режим выделения текста
			mode = TEXT_SELECT_MODE;
			textSelector = new TextSelector(screenContent);
		}
		break;
		case LEFT_KEY:{ //Назад, к предыдущему экрану
			ScreenManager.getInstance().restoreSavedSvreen();
		}
		break;
		case KEY_STAR:{ //Список закладок
			UserInterface.getInstance().showBookmarksListEditor();
		}
		break;
		case JOYSTICK_CENTER:{//Вызов меню
			UserInterface.getInstance().showMenu();
		}
	}
}


private void keyPressedTextSelectMode(int keyCode){
	switch (keyCode){
		
		case KEY_NUM2:{ //Слово вверх
			textSelector.moveUp();
		} 
		break;
		
		case KEY_NUM8:{ //Слово в низ
			textSelector.moveDown();
		} 
		break;
		
		case KEY_NUM6:{ //Слово в право
			textSelector.moveRight();
		} 
		break;
		
		case KEY_NUM4:{ //Слово в лево
			textSelector.moveLeft();
		} 
		break;
		
		case KEY_NUM3:{ //Расширение области в право
			textSelector.expandArea();
		} 
		break;		
		
		case KEY_NUM1:{ //Сужение области
			textSelector.contractionArea();
		} 
		break;
		
		case KEY_NUM5:{ //Поиск в словаре
			textViewer.findWordInDictionary(textSelector.getSelectedText());
		} 
		break;
		
		case KEY_NUM0:{ //Диалог поиска в словаре
			UserInterface.getInstance().ShowFindTextDialog(textSelector.getSelectedText());
		} 
		break;
		
		case KEY_NUM7:{ //Выход из режима выделения
			mode = TEXT_READ_MODE;
			textSelector = null;
		} 
		break;
		
		case KEY_NUM9:{ //Выход из режима выделения
			mode = TEXT_READ_MODE;
			textSelector = null;
		} 
		break;
		
		case JOYSTICK_CENTER:{//Вызов меню
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