package textselector;

public class Word{

private String contentString;

private int posX;
private int posY;

//���������� � ��������� ������� ���������� ������
private Word nextWord;
private Word pervWord;

Word(String inContentString, int inPosX, int inPosY){
	System.out.println("����������� word >>>> ������>>>"+inContentString+"<<< ������� X = "+inPosX+" ������� Y = "+inPosY);
	
	contentString = inContentString; 
	
	posX = inPosX;
	posY = inPosY;
	
	nextWord = null;
	pervWord = null;
}




public String getString(){
	return contentString;
}

public int getPosX(){
	return posX;
}

public int getPosY(){
	return posY;
}

//���������-�� ����� word �� ��-�� ����� ����� ��� � this �����?
public boolean inThisLine(Word word){
	if (word.getPosY() == posY) return true;
	return false;
}

//////////////////////��������� ������//////////////////////////
public void linkNextWord(Word word){
	if (nextWord != word){
		nextWord = word;
		if (nextWord != null) nextWord.linkPervWord(this);
	}
}

public void linkPervWord(Word word){
	if (pervWord != word){
		pervWord = word;
		if (pervWord != null) pervWord.linkNextWord(this);
	}
}

public Word getNextWord(){
	return nextWord;
}

public Word getPervWord(){
	return pervWord;
}

}