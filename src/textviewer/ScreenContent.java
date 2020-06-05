package textviewer;

import javax.microedition.lcdui.*;
import java.util.Vector;
import displaysize.DisplaySizeDirector;
import filetextreader.TextBuffer;
import filetextreader.TextIterator;
import filetextreader.TextUtilites;
import userinterface.UserInterface;

public class ScreenContent{

//����� ����������������� ��� ������
private Vector linesVector;

private TextIterator textIterator;

//����� ��� ������� ���������
private FontNavigator fontNavigator;



private int screenHeight;
private int screenWidth;

//����������� ����� ������������ �� ����� ��� �������� ������
private int screenLines;

//������� ������� � �����, � ���� ������� ����������� ������ ������
private int screenPointer;

//������� �������� ����� ��������� ����� ��� ������ �������� ������, �� ��������������� �������
private static final int FIND_PARAGRAPH_UP_LIMIT = 10000;

public ScreenContent(String filePatch, int filePosition, int bufferSize){
	fontNavigator = fontNavigator.getInstance();
	
	//����� ������� ������ ��� ���������� ����������� �� �� ������
	DisplaySizeDirector displaySize = DisplaySizeDirector.getInstance();
	screenHeight =displaySize.getScreenVisota();
	screenWidth = displaySize.getScreenShirina();
	
	//������������� ����������� ����� ������������ �� ������
	calculateScreenLines();
	
	linesVector = new Vector();
	
	screenPointer = filePosition;
	
	//������ ����� �����
	TextBuffer textBuffer = new TextBuffer(filePatch, bufferSize);

	if (textBuffer.fileIsExist()){
		//������ ����������������� �������
		textIterator = textBuffer.getTextIterator();

		textIterator.setPosition(screenPointer);
	
		//��������� ������ �����
		layOutTextToVector(textIterator, screenLines, linesVector);
	} else {
		UserInterface.getInstance().showErrorMessage("��� ������ �����");
	}
}

//������� ����� ������ ��� �������� ������
private void calculateScreenLines(){
	screenLines = screenHeight/fontNavigator.getFont().getHeight();
}


///////////////////////////////////////�������������� ������ - ����������� �����//////////////////////////////////
//������ ����� �� ������� �� ������� �������� textIterator 
//���� �� ���������� ���� ��� ���� �� ��������� linesAmount
//����� ������������ �� ������, ������ ������������ � layOutStrings
private void layOutTextToVector(TextIterator textIterator, int linesAmount, Vector layOutStrings){
	//������� ������
	layOutStrings.removeAllElements();
	String line;
	for(int ch=0; ch<linesAmount; ch++){
		line = splitOneLine(textIterator);
		if (textIterator.isEndString()){
			//textIterator.nextChar();
			//textIterator.nextChar();
			textIterator.skipNEL();
		}
		//System.out.println("layOutText 1 >>>> "+line);
		layOutStrings.addElement(line);
	}
}

//������ ����� �� ������� �� ������� �������� textIterator 
//���� �� ���������� ���� ��� ���� �� ��������� endTextFilePosition
//����� ������������ �� ������, ������� � ������� �������� ������ ������������ � positionsInFile
private Vector createVectorBeginPositionsLayoutStrings(TextIterator textIterator, int endTextFilePosition){
	Vector positionsInFile = new Vector();
	while(textIterator.getPosition()<endTextFilePosition){
		//���������� ������� � ������� ���������� ��� ������
		positionsInFile.addElement(new Integer(textIterator.getPosition()));
		splitOneLine(textIterator);
		if (textIterator.isEndString()){
			//textIterator.nextChar();
			//textIterator.nextChar();
			textIterator.skipNEL();
		}
		//System.out.println("layOutText 2 >>>> "+line+" >>>>>textIterator.getPosition() = "+textIterator.getPosition()+">>>> endTextFilePosition = "+endTextFilePosition);
	}
	return positionsInFile;
}

//��������� ������ ������ �� ������� ������� textIterator
//������ ������ �� ��������� ������ ������
//���� ����� ��������� ������, ����� �������  ������ ���� ��������
private String splitOneLine(TextIterator textIterator){
	String result = new String();
	int stringSize = 0;
	byte currentChar = 0;
	//���� ����������� ����� ������ � ���� �� ����� ������
	while((stringSize<screenWidth) && (!textIterator.isEndString()) && (!textIterator.isEndFile())){
		currentChar = textIterator.getChar();
		stringSize += fontNavigator.getCharWidth(currentChar);
		//���� ������ ������ �������� ������ ������ ��������� ����
		if (stringSize>screenWidth) break;
		
		result += TextUtilites.byteToCP1251(currentChar);
		
		//���� ����� ����� ��������� ����
		if (!textIterator.nextChar()) break;
	}
	
	//���� �� ������������ �� ������� ����� ������, ������ ������� ������ �������
	if (textIterator.isEndString()) return result;
	
	
	//���� ������� ������ �����, ��, � ����� �� ���������� ��������� ���� ��������
	//� ������� ������ �����
	//���� � ��� ��� ������ �� ��������� �� ������ ������, � ������ ������� ����� ������� �� ����
	//��������� ����� ���
	textIterator.pervChar();
	//System.out.println("splitOneLine --- ��������� ������ >>> "+TextUtilites.byteToCP1251(textIterator.getChar())+" code = "+textIterator.getChar());
	int charType = TextUtilites.classificateChar(textIterator.getChar());
	textIterator.nextChar();
	
	if ((charType == TextUtilites.RUSSIAN_CHAR) || (charType == TextUtilites.ENGLISH_CHAR)){
		
		//System.out.println("����� ������");
		return result.substring(0, result.length())+'-';
	} else {
		System.out.println("������");
	}
	return result;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////



private void findBeginParagraphFromBack(TextIterator textIterator){
	int counterChar = 0;
	while(!textIterator.isEndString()){
		//System.out.println("findBeginParagraphFromBack >>> "+ TextUtilites.byteToCP1251(textIterator.getChar())+ " code = "+textIterator.getChar());
		//���� ����� ������� �����
		if (!textIterator.pervChar()) return;
		counterChar++;
		if (counterChar>FIND_PARAGRAPH_UP_LIMIT) return;
	}
	
	//���������� 10+13
	//textIterator.nextChar();
	//textIterator.nextChar();
	textIterator.skipNEL();
}



public void jumpUpLines(int jumpLines){
	Vector positions = null;
	int oldPosition;
	int fillLines = 0; //������� ����������� �����
	
	//���� ������
	if (screenPointer == 1) return;
	
	while(fillLines<jumpLines){
		textIterator.setPosition(screenPointer);
		
		//���� �� ������� ������� ������ ������� (����� �� ������)
		System.out.println("screenUp cicle textIterator.getPosition() >>> "+textIterator.getPosition());
		oldPosition = textIterator.getPosition();
		findBeginParagraphFromBack(textIterator);
		screenPointer = textIterator.getPosition();
		
		//���� ��� ������� � �������� ������� ������ �� ���������� �� ������ ������ �������� �����
		//������-��� ��� ������ ������ ������ �� ������� � ��� ������ �� 10+13, � �� ����������� �� ���� ������� ������ ��� ���� �� ������
		if (screenPointer>=oldPosition){
			System.out.println("!!!!!!!!!!���������� �� ������ ������ �������� �����!!!!!!!!!!!!!");
			System.out.println("oldPosition = "+oldPosition+" screenPointer = "+screenPointer);
			fillLines++;
			screenPointer -= 3;
			continue;
		}
		
		//
		
		System.out.println("screenUp cicle >>> "+ TextUtilites.byteToCP1251(textIterator.getChar())+" code = "+textIterator.getChar());
		
		//�������� ������ (� ������ �����) ����������� ����� �� ��������� ������� �� ��������� ������� (oldPosition) 
		positions = createVectorBeginPositionsLayoutStrings(textIterator, oldPosition);
		System.out.println("����� ���������� positions.size() = "+positions.size());
		
		//���� ����� �� ������� �� �����, ����������
		fillLines += positions.size();
		
		//���������� 10+13
		screenPointer -= 3; //3 - ������-��� ������ �� ������� ���������, 13, � ����� 10
		if (screenPointer<0) break;
	}
	
	//����� ���������� ������ ��� ����
	if (fillLines>jumpLines){
		System.out.println("����������� ����� ������ ��� ������� ����� fillLines = "+fillLines+" jumpLines = "+jumpLines);
		//��� ����� ������� � ������� ���������� ������ ����� ������
		screenPointer = ((Integer)positions.elementAt(fillLines-jumpLines)).intValue();
	} else {//����� ��������� ����� ������� �� ����� �� ������ ��������
		System.out.println("����������� ����� ������ ��� ������� ����� fillLines = "+fillLines+" jumpLines = "+jumpLines);
		screenPointer = ((Integer)positions.elementAt(0)).intValue();
	}
	
	textIterator.setPosition(screenPointer);
	//� ���� ������� ��������� �����
	layOutTextToVector(textIterator, screenLines, linesVector);
}

public void jumpDownLines(int lines){
}

///////////////////���������//////////////////////////////
public void screenDown(){
	//���������� ���� ����� �����
	textIterator.setPosition(screenPointer);
	layOutTextToVector(textIterator, screenLines-1, linesVector);
	
	//������ ������� ������ �� ����� ����
	screenPointer = textIterator.getPosition();
	
	//��������� ����� �����
	layOutTextToVector(textIterator, screenLines, linesVector);
}

public void screenUp(){
	jumpUpLines(screenLines);
}

//�� ����� ����� �����-����
public void lineUp(){
	jumpUpLines(1);
}

public void lineDown(){
	System.out.println("LD");
	//���������� ���� �����
	textIterator.setPosition(screenPointer);
	splitOneLine(textIterator);
	if (textIterator.isEndString()){
			//textIterator.nextChar();
			//textIterator.nextChar();
			textIterator.skipNEL();
	}
	
	//������ ������� ������ �� ����� ����
	screenPointer = textIterator.getPosition();
	
	//��������� ����� �����
	layOutTextToVector(textIterator, screenLines, linesVector);
}


//////////////////////////////////////////////////////////

/////////////////�����////////////////////////////////////

public void fontIncrease(){
	fontNavigator.fontIncrease();
	
	//������������� ����������� ����� ������������ �� ������
	calculateScreenLines();
	
	//��������� ������ �����
	textIterator.setPosition(screenPointer);
	layOutTextToVector(textIterator, screenLines, linesVector);
}

public void fontDecrease(){
	fontNavigator.fontDecrease();
	
	//������������� ����������� ����� ������������ �� ������
	calculateScreenLines();
	
	//��������� ������ �����
	textIterator.setPosition(screenPointer);
	layOutTextToVector(textIterator, screenLines, linesVector);
}

public Font getCurrentFont(){
	return fontNavigator.getFont();
}

public int getLineHeight(){
	return fontNavigator.getFont().getHeight();
}


//���������� ������ �� ������ ������������ firstScreenLine
public String getLine(int lineNum){
	if (lineNum >= linesVector.size()) return null;
	return (String)linesVector.elementAt(lineNum);
}

public int getPositionInFile(){
	return screenPointer;
}
}