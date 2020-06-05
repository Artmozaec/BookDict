package textselector;

import textviewer.ScreenContent;
import textviewer.FontNavigator;

class WordsSplitter{
ScreenContent screenContent;

int linePointer;

//Первая буква следующего слова
int beginWord;

//Строка текущей линии
String currentLine;

//Расположение слова по экранной верикали, увеличиваестя с каждой новой строкой
private int wordPosY;

//Разделители строк - символы между которыми ищем последовательности букв - слова
private static final String SEPARATORS = " ,.()!?@#$%^&*+=|{}[];:\"<>";

private FontNavigator fontNavigator;

WordsSplitter(ScreenContent inScreenContent){
	screenContent = inScreenContent;
	linePointer = 0;
	beginWord = 0;
	wordPosY = 0;
	currentLine = screenContent.getLine(linePointer);
	fontNavigator = FontNavigator.getInstance();
}

private boolean isSeparator(char character){
	for(int ch=0; ch<SEPARATORS.length(); ch++){
		if (SEPARATORS.charAt(ch) == character) return true;
	}
	return false;
}

//Ведёт поиск зазделительного символа от beginPosition
private int findSeparator(int beginPosition){
	//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!findSeparator - начало поиска beginPosition = "+beginPosition);
	for (int ch=beginPosition; ch<currentLine.length(); ch++){
		//System.out.println("findSeparator char >>>"+currentLine.charAt(ch)+"<<<ch = "+ch+" currentLine.length() = "+currentLine.length());
		if (isSeparator(currentLine.charAt(ch))) return ch;
	}
	return -1;
}

//Ведёт поис текмтового символа от beginPosition
private int findCharacter(int beginPosition){
	//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!findCharacter - начало поиска beginPosition = "+beginPosition);
	for (int ch=beginPosition; ch<currentLine.length(); ch++){
		//System.out.println("findCharacter char >>>"+currentLine.charAt(ch)+"<<<ch = "+ch+" currentLine.length() = "+currentLine.length());
		if (!isSeparator(currentLine.charAt(ch))) return ch;
	}
	//System.out.println("Не найдена буква, возвращаем -1");
	return -1;
}

private int getSubstringWidth(String string, int begin, int end){
	string = string.substring(begin, end);
	return fontNavigator.getStringWidth(string);
}


public Word nextWord(){
	//System.out.println();
	//System.out.println();
	//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!nextWord()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	
	while(true){
		beginWord = findCharacter(beginWord);
		//Начало слова не найдено, новую строку читаем
		if (beginWord == -1){
			//System.out.println("Не найдена буква, Читаем следующую строку");
			linePointer++;
			
			//Читаем следующую строку
			currentLine = screenContent.getLine(linePointer);
			
			//Сигнал что строки кончились
			if (currentLine == null) return null;
			beginWord = 0;
			
			//Увеличиваем на выоту шрифта, как-бы следующая линия
			wordPosY += fontNavigator.getFontHeight();
		} else break;
	}

	Word word = null;
	int endWord = findSeparator(beginWord);
	int wordPosX;
	String wordText;
	
	//Узнаём позицию слова по горизонтали
	wordPosX = getSubstringWidth(currentLine, 0, beginWord);
	
	//Разделитель далее не найден, значит, слово от текущей позиции до конца строки
	if (endWord == -1){	
		//От указателя до конца строки
		wordText = currentLine.substring(beginWord, currentLine.length());
		//Обеспечиваем чтение следующей строки при следующем вызове функции
		beginWord = currentLine.length();
	} else {
		wordText = currentLine.substring(beginWord, endWord);
		beginWord = endWord+1;
	}
	
	//Результат
	word = new Word(wordText, wordPosX, wordPosY);

	return word;
}

}