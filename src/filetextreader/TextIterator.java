package filetextreader;

import filetextreader.TextBuffer;

public class TextIterator{
TextBuffer textBuffer;
int pointer;
int fileSize;
//CR + LF
private final static byte CR = 13; //возврат каретки
private final static byte LF = 10; //подача строки


TextIterator(TextBuffer inTextBuffer){
	textBuffer = inTextBuffer;
	fileSize = textBuffer.getFileSize();
	pointer = 0;
}

public void setPosition(int newPosition){
	pointer = newPosition;
}

public byte getChar(){
	return textBuffer.getCharAt(pointer);
}

public boolean nextChar(){
	pointer++;
	if (pointer>fileSize){
		pointer--;
		return false;
	}
	return true;
}

public boolean pervChar(){
	pointer--;
	if (pointer<1){
		pointer++;
		return false;
	}
	return true;
}

//ѕропуск служебных символов новой строки
//13+10 или 13
public void skipNEL(){
	if (textBuffer.getCharAt(pointer) == CR){
		nextChar();
		nextChar();
	}
	if (textBuffer.getCharAt(pointer) == LF){
		nextChar();
	}
}

public int getPosition(){
	return pointer;
}

public boolean isEndString(){
	//if ((textBuffer.getCharAt(pointer-1) == CR) && (textBuffer.getCharAt(pointer) == LF)) return true;
	if ((textBuffer.getCharAt(pointer) == CR) && (textBuffer.getCharAt(pointer+1) == LF)) return true;
	if (textBuffer.getCharAt(pointer) == LF) return true;
	
	return false;
}

public boolean isEndFile(){
	if (pointer==fileSize) return true;
	return false;
}

public boolean isDigit(){
	if (TextUtilites.classificateChar(getChar()) == TextUtilites.DIGIT_CHAR) return true;
	return false;
}

public boolean isCharacter(){
	if ((TextUtilites.classificateChar(getChar()) == TextUtilites.ENGLISH_CHAR) || (TextUtilites.classificateChar(getChar()) == TextUtilites.RUSSIAN_CHAR)) return true;
	return false;
}

public boolean isSymbol(){
	if (TextUtilites.classificateChar(getChar()) == TextUtilites.SYMBOLS_CHAR) return true;
	return false;
}

}