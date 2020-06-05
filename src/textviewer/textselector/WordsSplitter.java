package textselector;

import textviewer.ScreenContent;
import textviewer.FontNavigator;

class WordsSplitter{
ScreenContent screenContent;

int linePointer;

//������ ����� ���������� �����
int beginWord;

//������ ������� �����
String currentLine;

//������������ ����� �� �������� ��������, ������������� � ������ ����� �������
private int wordPosY;

//����������� ����� - ������� ����� �������� ���� ������������������ ���� - �����
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

//���� ����� ��������������� ������� �� beginPosition
private int findSeparator(int beginPosition){
	//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!findSeparator - ������ ������ beginPosition = "+beginPosition);
	for (int ch=beginPosition; ch<currentLine.length(); ch++){
		//System.out.println("findSeparator char >>>"+currentLine.charAt(ch)+"<<<ch = "+ch+" currentLine.length() = "+currentLine.length());
		if (isSeparator(currentLine.charAt(ch))) return ch;
	}
	return -1;
}

//���� ���� ���������� ������� �� beginPosition
private int findCharacter(int beginPosition){
	//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!findCharacter - ������ ������ beginPosition = "+beginPosition);
	for (int ch=beginPosition; ch<currentLine.length(); ch++){
		//System.out.println("findCharacter char >>>"+currentLine.charAt(ch)+"<<<ch = "+ch+" currentLine.length() = "+currentLine.length());
		if (!isSeparator(currentLine.charAt(ch))) return ch;
	}
	//System.out.println("�� ������� �����, ���������� -1");
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
		//������ ����� �� �������, ����� ������ ������
		if (beginWord == -1){
			//System.out.println("�� ������� �����, ������ ��������� ������");
			linePointer++;
			
			//������ ��������� ������
			currentLine = screenContent.getLine(linePointer);
			
			//������ ��� ������ ���������
			if (currentLine == null) return null;
			beginWord = 0;
			
			//����������� �� ����� ������, ���-�� ��������� �����
			wordPosY += fontNavigator.getFontHeight();
		} else break;
	}

	Word word = null;
	int endWord = findSeparator(beginWord);
	int wordPosX;
	String wordText;
	
	//����� ������� ����� �� �����������
	wordPosX = getSubstringWidth(currentLine, 0, beginWord);
	
	//����������� ����� �� ������, ������, ����� �� ������� ������� �� ����� ������
	if (endWord == -1){	
		//�� ��������� �� ����� ������
		wordText = currentLine.substring(beginWord, currentLine.length());
		//������������ ������ ��������� ������ ��� ��������� ������ �������
		beginWord = currentLine.length();
	} else {
		wordText = currentLine.substring(beginWord, endWord);
		beginWord = endWord+1;
	}
	
	//���������
	word = new Word(wordText, wordPosX, wordPosY);

	return word;
}

}