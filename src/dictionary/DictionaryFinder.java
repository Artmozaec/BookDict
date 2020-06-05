package dictionary;

import java.io.*;
import filetextreader.TextBuffer;
import filetextreader.TextIterator;
import filetextreader.TextUtilites;

public class DictionaryFinder{
private String dictionaryPatch;

DictionaryFinder(String patch){
	dictionaryPatch = patch;
}


//������� �� ������� ������� ���������� ������� ����� � ��������� findString ��� ������ ������� ����������� ���������� false
//���� ��� ������� �� findString �������� ���������� true
private boolean stringExistInCurrentPosition(TextIterator textIterator, byte[] findString){
	//System.out.println();
	for (int ch=0; ch<findString.length-1; ch++){
		//System.out.println("��������� ������ �����>>>"+TextUtilites.byteToCP1251(textIterator.getChar())+"<<< ������ ������� ������ >>>"+TextUtilites.byteToCP1251(findString[ch]));
		if (textIterator.getChar() != findString[ch]) return false;
		textIterator.nextChar();
	}
	return true;
}

//�� ������� ������� ���������� �������� � ����� ����� �����������, � ������ ������ ��� 10
private void findNextWordBegin(TextIterator textIterator){
	while (textIterator.getChar() != 10){
		textIterator.nextChar();
	}
	textIterator.nextChar();
}

//����� � �������
public int findWord(String word, int beginPosition, int areaLength){
	//����������� ������ � ������ ������
	byte[] wordBytes = null;
	try{
		wordBytes = word.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {}
	
	//������ ������ � ����� �������
	TextBuffer dictionaryBuffer = new TextBuffer(dictionaryPatch, areaLength*2);
	TextIterator textIterator = dictionaryBuffer.getTextIterator();
	textIterator.setPosition(beginPosition);
	int beginCurrentWord = 0;
	while (textIterator.getPosition()<(beginPosition+areaLength)){
		beginCurrentWord = textIterator.getPosition();
		
		//���������� ����������� �� �������� ����� � ������� ������, ���� ��� ����� ���������, �����!
		if (stringExistInCurrentPosition(textIterator, wordBytes)) return beginCurrentWord;
		
		//���� ��������� ������ �������
		findNextWordBegin(textIterator);
	}
	//�� ������ ����� �����
	return Dictionary.NOT_FOUND;
}

}