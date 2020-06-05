package textselector;

import textviewer.ScreenContent;

public class TextSelector{

//��������� �� ������ ����� ������� - ������
private Word beginChainWord;


//����� � ������ - ������� ����� ����������� ������
//������ �� ��������� ����� ����� �������� ������������ ���������
//��������� ������ ��� �� ��� ���� ���������� � ������ � ������� ����� �������� � word 
//� � ���� ����� ���������� � ��������� ����� ��� ���� ���� ����� ��������������� � �������� �����
private Word leftBoundWord;
private Word rightBoundWord;

//������� ����� ��� ������ ������
private Word currentOutWord;

public TextSelector(ScreenContent screenContent){
	//������ ������� ����
	createWordsChain(screenContent);
	
	//��������!!!!!!!!!!!!!!!
	leftBoundWord = beginChainWord;
	rightBoundWord = beginChainWord;
	
	currentOutWord = leftBoundWord;
}

private void createWordsChain(ScreenContent screenContent){
	
	//������ ����������� ����� �� �����
	WordsSplitter wordsSplitter = new WordsSplitter(screenContent);

	beginChainWord = wordsSplitter.nextWord();
	Word currentWord = beginChainWord;
	while (currentWord != null){
		currentWord.linkNextWord(wordsSplitter.nextWord());
		currentWord = currentWord.getNextWord();
	}

/*/////////���������� ����������	
	currentWord = beginChainWord;
	while (currentWord != null){
		System.out.println(">>"+currentWord.getString());
		currentWord = currentWord.getNextWord();
	}
*/
}

//������� �� beginWord ���� �������� ������� �� �������������� �������
//� ������� ����� etalonWord
//����� ������ � �������� ����� �����-������
private Word findInStringNearHorizontal(Word beginWord, Word etalonWord){
	Word currentWord = beginWord;
	Word endWord = findEndString(beginWord);
	Word nearWord = null;
	int currentDistance;
	int nearDistance = -1;
	while (currentWord != endWord.getNextWord()){
		//����� ������� ����� ������� ������ � ���������
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



//������� �� beginWord ������������ �� ������ � �����, 
//��������� ����� ������������ �� ������ �� ����������
//� ���������� ���������� �� ����� �����
private Word findEndString(Word beginWord){
	Word currentWord = beginWord;
	int thisLinePosY = beginWord.getPosY();
	while(currentWord.getPosY() == thisLinePosY){
		if (currentWord.getNextWord() == null) return currentWord;
		currentWord = currentWord.getNextWord();	
	}
	return currentWord.getPervWord();
}


//������� �� beginWord ������������ �� ������ � ����, 
//��������� ����� ������������ �� ������ �� ����������
//� ���������� ��������� �� ����� �����
private Word findBeginString(Word beginWord){
	Word currentWord = beginWord;
	int thisLinePosY = beginWord.getPosY();
	while(currentWord.getPosY() == thisLinePosY){
		if(currentWord.getPervWord() == null) return currentWord;
		currentWord = currentWord.getPervWord();
	}
	return currentWord.getNextWord();
}



///////////////���������/////////////////////////////
public void moveUp(){
	//� ������ ����� ����� ������ ���� ������
	Word beginWord = findBeginString(leftBoundWord);
	
	//��� ���� ������ ������
	if (beginWord.getPervWord() == null) return;
	
	//������ ������ ����������
	beginWord = findBeginString(beginWord.getPervWord());
	
	//�� ������ ���������� ������ ���� ��������� ����� � leftBoundWord
	leftBoundWord = findInStringNearHorizontal(beginWord, leftBoundWord);
	rightBoundWord = leftBoundWord;
}

public void moveDown(){
	Word endWord = findEndString(leftBoundWord);
	//��� ���� ��������� ������
	if (endWord.getNextWord() == null) return;
	
	//�� ������ ��������� ������ ���� ��������� ����� � leftBoundWord
	leftBoundWord = findInStringNearHorizontal(endWord.getNextWord(), leftBoundWord);
	rightBoundWord = leftBoundWord;
}

public void moveRight(){
	//������ �������
	if (leftBoundWord.getNextWord() == null) return;
	
	leftBoundWord = leftBoundWord.getNextWord();
	rightBoundWord = leftBoundWord;
	//currentOutWord = leftBoundWord;
}

public void moveLeft(){
	//����� �������
	if(leftBoundWord.getPervWord() == null) return;
	
	leftBoundWord = leftBoundWord.getPervWord();
	rightBoundWord = leftBoundWord;
	//currentOutWord = leftBoundWord;
}
/////////////////////////////////////////

/////////////////��������� ������� �������////////////////////////
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

////////////���������� �����//////////////////////////
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

//��������� �����-�� ��� � ������?
private boolean isEndWordInLine(Word word){
	if (word.getNextWord() == null) return true;
	if(word.getPosY() != word.getNextWord().getPosY()) return true;
	return false;
}

//��� ����� ������� �����? 
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
		//���� ������ ����� ��������� ��������� � ������
		//���� ��� ��������� ����� ������������ ����
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