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


//Начиная от текущей позиции сравнивает символы файла с символами findString как только символы различаются возвращает false
//Если все символы из findString сходятся возвращает true
private boolean stringExistInCurrentPosition(TextIterator textIterator, byte[] findString){
	//System.out.println();
	for (int ch=0; ch<findString.length-1; ch++){
		//System.out.println("Сравнение символ файла>>>"+TextUtilites.byteToCP1251(textIterator.getChar())+"<<< символ искомой строки >>>"+TextUtilites.byteToCP1251(findString[ch]));
		if (textIterator.getChar() != findString[ch]) return false;
		textIterator.nextChar();
	}
	return true;
}

//От текущей позиции перемещает итератор в место после разделителя, в данном случае код 10
private void findNextWordBegin(TextIterator textIterator){
	while (textIterator.getChar() != 10){
		textIterator.nextChar();
	}
	textIterator.nextChar();
}

//Поиск в словаре
public int findWord(String word, int beginPosition, int areaLength){
	//Перобразуем строку в массив байтов
	byte[] wordBytes = null;
	try{
		wordBytes = word.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {}
	
	//Создаём доступ к файлу словаря
	TextBuffer dictionaryBuffer = new TextBuffer(dictionaryPatch, areaLength*2);
	TextIterator textIterator = dictionaryBuffer.getTextIterator();
	textIterator.setPosition(beginPosition);
	int beginCurrentWord = 0;
	while (textIterator.getPosition()<(beginPosition+areaLength)){
		beginCurrentWord = textIterator.getPosition();
		
		//Сравниваем посимвольно от текущего места с искомым словом, если все буквы совпадают, нашли!
		if (stringExistInCurrentPosition(textIterator, wordBytes)) return beginCurrentWord;
		
		//Ищем следующую строку словаря
		findNextWordBegin(textIterator);
	}
	//Не найден такой слово
	return Dictionary.NOT_FOUND;
}

}