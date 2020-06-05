package textviewer;

import javax.microedition.lcdui.*;
import filetextreader.TextUtilites;

public class FontNavigator{
private Font currentFont;
int fontIndex;

private static FontNavigator instance = null;

public static FontNavigator getInstance(){
	if (instance == null){
		instance = new FontNavigator();
	}
	return instance;
}

private FontNavigator(){
	fontIndex = 1; //Средний шрифт
	selectFont(fontIndex);
}

private void selectFont(int index){
	switch(index){
		case 0:{
			currentFont = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		}
		break;
		case 1:{
			currentFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		}
		break;
		case 2:{
			currentFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
		}
	}
}

public void fontIncrease(){
	fontIndex++;
	if (fontIndex>2) fontIndex--;
	selectFont(fontIndex);
}

public void fontDecrease(){
	fontIndex--;
	if (fontIndex<0) fontIndex++;
	selectFont(fontIndex);
}


//////////////////////////////ЗАПРОСЫ К ШРИФТУ//////////////////////////////
public Font getFont(){
	return currentFont;
}

public int getFontHeight(){
	return currentFont.getHeight();
}

//Конвертирует значение byte в CP1251 а потом находит размер этого
public int getCharWidth(byte byteChar){
	return currentFont.charWidth(TextUtilites.byteToCP1251(byteChar));
}

public int getStringWidth(String str){
	return currentFont.stringWidth(str);
}
//////////////////////////////////////////////////////////////////////////
}