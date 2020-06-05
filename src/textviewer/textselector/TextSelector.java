package textselector;

import textviewer.ScreenContent;

public class TextSelector{

//Указатель на первое слово цепочки - списка
private Word beginChainWord;


//Левое и правое - крайние слова выделенного текста
//ссылки на граничные слова между которыми простирается выделение
//Связанный список это то что надо информация о тексте и позиции будет хранится в word 
//и у него будет предыдущее и следующее слово при этом весь экран разворачивается в сплошную линию
private Word leftBoundWord;
private Word rightBoundWord;

//Текущее слово для отдачи наружу
private Word currentOutWord;

public TextSelector(ScreenContent screenContent){
	//Строим цепочку слов
	createWordsChain(screenContent);
	
	//ВРЕМЕННО!!!!!!!!!!!!!!!
	leftBoundWord = beginChainWord;
	rightBoundWord = beginChainWord;
	
	currentOutWord = leftBoundWord;
}

private void createWordsChain(ScreenContent screenContent){
	
	//Создаём расщепитель строк на слова
	WordsSplitter wordsSplitter = new WordsSplitter(screenContent);

	beginChainWord = wordsSplitter.nextWord();
	Word currentWord = beginChainWord;
	while (currentWord != null){
		currentWord.linkNextWord(wordsSplitter.nextWord());
		currentWord = currentWord.getNextWord();
	}

/*/////////Отладочная проверочка	
	currentWord = beginChainWord;
	while (currentWord != null){
		System.out.println(">>"+currentWord.getString());
		currentWord = currentWord.getNextWord();
	}
*/
}

//Начиная от beginWord ищет наиболее близкое по горизонтальной позиции
//К позиции слова etalonWord
//Поиск ведётся в пределах одной линии-строки
private Word findInStringNearHorizontal(Word beginWord, Word etalonWord){
	Word currentWord = beginWord;
	Word endWord = findEndString(beginWord);
	Word nearWord = null;
	int currentDistance;
	int nearDistance = -1;
	while (currentWord != endWord.getNextWord()){
		//Узнаём разницу между текущем словом и эталонным
		currentDistance = Math.abs(etalonWord.getPosX()-currentWord.getPosX());
		
		//System.out.println("findInStringNearHorizontal >>>"+currentWord.getString());
		
		if ((currentDistance<nearDistance) || (nearDistance == -1)){
			nearDistance = currentDistance;
			nearWord = currentWord;
		}
		
		currentWord = currentWord.getNextWord();
	}
	return nearWord;
}



//Начиная от beginWord перемещается по словам в право, 
//отыскивая слово отличающееся по высоте от начального
//И возвращает предыдущее от этого слово
private Word findEndString(Word beginWord){
	Word currentWord = beginWord;
	int thisLinePosY = beginWord.getPosY();
	while(currentWord.getPosY() == thisLinePosY){
		if (currentWord.getNextWord() == null) return currentWord;
		currentWord = currentWord.getNextWord();	
	}
	return currentWord.getPervWord();
}


//Начиная от beginWord перемещается по словам в лево, 
//отыскивая слово отличающееся по высоте от начального
//И возвращает следующее от этого слово
private Word findBeginString(Word beginWord){
	Word currentWord = beginWord;
	int thisLinePosY = beginWord.getPosY();
	while(currentWord.getPosY() == thisLinePosY){
		if(currentWord.getPervWord() == null) return currentWord;
		currentWord = currentWord.getPervWord();
	}
	return currentWord.getNextWord();
}



///////////////НАВИГАЦИЯ/////////////////////////////
public void moveUp(){
	//С начала нужно найти начало этой строки
	Word beginWord = findBeginString(leftBoundWord);
	
	//Это была Первая строка
	if (beginWord.getPervWord() == null) return;
	
	//Начало строки пердыдущей
	beginWord = findBeginString(beginWord.getPervWord());
	
	//От начала предыдущей строки ищем ближайшее слово к leftBoundWord
	leftBoundWord = findInStringNearHorizontal(beginWord, leftBoundWord);
	rightBoundWord = leftBoundWord;
}

public void moveDown(){
	Word endWord = findEndString(leftBoundWord);
	//Это была последняя строка
	if (endWord.getNextWord() == null) return;
	
	//От начала следующей строки ищем ближайшее слово к leftBoundWord
	leftBoundWord = findInStringNearHorizontal(endWord.getNextWord(), leftBoundWord);
	rightBoundWord = leftBoundWord;
}

public void moveRight(){
	//Правая граница
	if (leftBoundWord.getNextWord() == null) return;
	
	leftBoundWord = leftBoundWord.getNextWord();
	rightBoundWord = leftBoundWord;
	//currentOutWord = leftBoundWord;
}

public void moveLeft(){
	//Левая граница
	if(leftBoundWord.getPervWord() == null) return;
	
	leftBoundWord = leftBoundWord.getPervWord();
	rightBoundWord = leftBoundWord;
	//currentOutWord = leftBoundWord;
}
/////////////////////////////////////////

/////////////////ИЗМЕНЕНиЕ РАЗМЕРА ОБЛАСТИ////////////////////////
public void expandArea(){
	if (rightBoundWord.getNextWord() == null) return;
	rightBoundWord = rightBoundWord.getNextWord();
}

public void contractionArea(){
	if (rightBoundWord == leftBoundWord) return;
	rightBoundWord = rightBoundWord.getPervWord();
}
/////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////

////////////ВЫДЕЛЕННЫЕ СЛОВА//////////////////////////
public void wordIteratorReset(){
	currentOutWord = leftBoundWord;
}

public Word nextWord(){
	if (currentOutWord == null) return null;
	
	Word word = currentOutWord;
	
	if (currentOutWord == rightBoundWord){
		currentOutWord = null;
	} else {
		currentOutWord = currentOutWord.getNextWord();
	}
	return word;
}

//Последнее слово-ли это в строке?
private boolean isEndWordInLine(Word word){
	if (word.getNextWord() == null) return true;
	if(word.getPosY() != word.getNextWord().getPosY()) return true;
	return false;
}

//Это слово перенос стоки? 
private boolean isLineBreak(Word word){
	String str = word.getString();
	if(str.charAt(str.length()-1) == '-') return true;
	return false;
}


public String getSelectedText(){
	String result = new String();
	wordIteratorReset();
	Word currentWord = nextWord();
	while (currentWord != null){
		//Если текщее слово оказалось последним в строке
		//Если это последнее слово оканчивается тире
		if ((isEndWordInLine(currentWord)) && (isLineBreak(currentWord))){
			String str = currentWord.getString();
			result += str.substring(0, str.length()-1);
		} else {
			result += currentWord.getString()+" ";
		}
		currentWord = nextWord();
	}
	return result;
}

}