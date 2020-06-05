package textselector;

public class Word{

private String contentString;

private int posX;
private int posY;

//Пердыдущий и следующий элемент связанного списка
private Word nextWord;
private Word pervWord;

Word(String inContentString, int inPosX, int inPosY){
	System.out.println("Конструктор word >>>> строка>>>"+inContentString+"<<< Позиция X = "+inPosX+" Позиция Y = "+inPosY);
	
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

//Находится-ли слово word на то-же самой линии что и this слово?
public boolean inThisLine(Word word){
	if (word.getPosY() == posY) return true;
	return false;
}

//////////////////////Связаннай список//////////////////////////
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