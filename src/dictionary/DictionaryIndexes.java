package dictionary;

import java.util.Vector;
import filetextreader.TextBuffer;
import filetextreader.TextIterator;
import filetextreader.TextUtilites;
import java.io.*;

class DictionaryIndexes{
private String indexFilePatch;

//�������� ��������� ����� ���� � ���������� �������
private Vector beginCharsVector;
//�������� ������� ����� ������� � ������� ���������� ������������������ ���� ������������ � ������� beginChars
private Vector fileIndexesFromBeginCharsVector;

private static final int SIZE_BUFFER_INDEX_FILE_READER = 7000;

DictionaryIndexes(String patch){
	indexFilePatch = patch;
	
	beginCharsVector = new Vector();
	fileIndexesFromBeginCharsVector = new Vector();
	
	System.out.println("����������� DictionaryIndexes "+patch);
	//������ ����, ��������� �������
	readDictionaryIndexes();
}

private int checkAndCreateInt(byte[] digitChar){
	//���� ������ ������� ���� ������ ��
	String digits;
	int result;
	if (digitChar.length == 0){
		return -1;
	}

	//������� ������ 
	digits = new String(digitChar);
	
	//�������� �������
	digits = digits.trim();
	
	//System.out.println("������� ����� >>>>>>>>>>>>>>>>"+digits+"<<<<<<<<<<<<<<<<<<<");
	try{
		result = Integer.parseInt(digits);
	}catch(NumberFormatException ne){
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!������_PARSE_INT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		result = -1;
	}
	
	return result;
}

private int findPositionFromBeginChars(TextIterator textIterator){
	byte[] digitChars = new byte[10];
	
	int ch = 0;
	while(textIterator.isDigit()){
		//System.out.println("findPositionFromBeginChars >>>"+TextUtilites.byteToCP1251(textIterator.getChar())+"<<<");
		digitChars[ch] = textIterator.getChar();
		textIterator.nextChar();
		ch++;
	}
	return checkAndCreateInt(digitChars);
}

private String findBeginChars(TextIterator textIterator){
	byte[] beginChars = new byte[2];
	
	int ch = 0;
	while ((textIterator.isCharacter()) || (textIterator.isSymbol())){
		//System.out.println("findBeginChars >>>"+TextUtilites.byteToCP1251(textIterator.getChar())+"<<<");
		beginChars[ch] = textIterator.getChar();
		textIterator.nextChar();
		ch++;
	}
	
	String result = null;
	try{
		result = new String(beginChars, "windows-1251");
	} catch (UnsupportedEncodingException ue) {System.out.println("OSHIBKA_KODIROVKA");}
	
	return result;
}

private void readDictionaryIndexes(){
	//�������� ������ ����� ��������
	TextBuffer indexFileReader = new TextBuffer(indexFilePatch, SIZE_BUFFER_INDEX_FILE_READER);
	
	//����� ����� �������� ������� ������ ���, �������
	if (!indexFileReader.fileIsExist()) return;
	TextIterator textIterator = indexFileReader.getTextIterator();
	
	int position;
	String chars;
	textIterator.setPosition(1);
	while(!textIterator.isEndFile()){
		//������� ��������� �������
		chars = findBeginChars(textIterator);
		//System.out.println("beginChars = "+chars);
		beginCharsVector.addElement(chars);
		//������� �������, ��������� � ������
		position = findPositionFromBeginChars(textIterator);
		fileIndexesFromBeginCharsVector.addElement(new Integer(position));
		//System.out.println("position = "+position);
		
		//���������� 10+13
		//textIterator.nextChar();
		//textIterator.nextChar();
		textIterator.skipNEL();
	}
}

private int findPositionBeginCharsInVector(String beginChars){
	for (int ch=0; ch<beginCharsVector.size(); ch++){
		if (beginChars.equals(beginCharsVector.elementAt(ch))){ 
			return ch;
		}
	}
	return Dictionary.NOT_FOUND;
}


private int findPositionFromBeginChars(String beginChars){
	for (int ch=0; ch<beginCharsVector.size(); ch++){
		if (beginChars.equals(beginCharsVector.elementAt(ch))){ 
			return ((Integer)fileIndexesFromBeginCharsVector.elementAt(ch)).intValue();
		}
	}
	return Dictionary.NOT_FOUND;
}

public int getBeginPosition(String beginChars){
	//���� ������� ��������� ���� � ������� ��������� ����
	int posInVector = findPositionBeginCharsInVector(beginChars);
	
	if (posInVector == Dictionary.NOT_FOUND) return Dictionary.NOT_FOUND;
	
	return ((Integer)fileIndexesFromBeginCharsVector.elementAt(posInVector-1)).intValue();
}

public int getCharsDictonaryAreaLength(String beginChars){
	int posInVector = findPositionBeginCharsInVector(beginChars);
	int areaBeginPosition = ((Integer)fileIndexesFromBeginCharsVector.elementAt(posInVector-1)).intValue();
	
	if (posInVector+1 >= fileIndexesFromBeginCharsVector.size()){
		return -1;//������� �� ����� �����
	}
	
	int areaEndPosition = ((Integer)fileIndexesFromBeginCharsVector.elementAt(posInVector)).intValue()-1;
	
	return areaEndPosition-areaBeginPosition;
}

}