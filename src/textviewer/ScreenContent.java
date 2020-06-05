package textviewer;

import javax.microedition.lcdui.*;
import java.util.Vector;
import displaysize.DisplaySizeDirector;
import filetextreader.TextBuffer;
import filetextreader.TextIterator;
import filetextreader.TextUtilites;
import userinterface.UserInterface;

public class ScreenContent{

//линии отформатированные для экрана
private Vector linesVector;

private TextIterator textIterator;

//Шрифт для текущей отрисовки
private FontNavigator fontNavigator;



private int screenHeight;
private int screenWidth;

//Колличество строк помещающихся на экран для текущего шрифта
private int screenLines;

//Текущая позиция в файле, с этой позиции заполняется вектор линиий
private int screenPointer;

//Столько символов будет проиденно вверх для поиска переноса строки, до принудительного разрыва
private static final int FIND_PARAGRAPH_UP_LIMIT = 10000;

public ScreenContent(String filePatch, int filePosition, int bufferSize){
	fontNavigator = fontNavigator.getInstance();
	
	//Узнаём размеры экрана для правильной расстановки на нём текста
	DisplaySizeDirector displaySize = DisplaySizeDirector.getInstance();
	screenHeight =displaySize.getScreenVisota();
	screenWidth = displaySize.getScreenShirina();
	
	//Пересчитываем колличество строк помещающихся на экране
	calculateScreenLines();
	
	linesVector = new Vector();
	
	screenPointer = filePosition;
	
	//Создаём буфер файла
	TextBuffer textBuffer = new TextBuffer(filePatch, bufferSize);

	if (textBuffer.fileIsExist()){
		//Объект последовательного доступа
		textIterator = textBuffer.getTextIterator();

		textIterator.setPosition(screenPointer);
	
		//Заполняем вектор линий
		layOutTextToVector(textIterator, screenLines, linesVector);
	} else {
		UserInterface.getInstance().showErrorMessage("Нет такого файла");
	}
}

//Рассчёт линий экрана для текущего шрифта
private void calculateScreenLines(){
	screenLines = screenHeight/fontNavigator.getFont().getHeight();
}


///////////////////////////////////////ФОРМАТИРОВАНИЕ ТЕКСТА - РАСЩЕПИТЕЛЬ СТРОК//////////////////////////////////
//читает текст от позиции на которую указывет textIterator 
//пока не закончится файл или пока не достигнем linesAmount
//текст расщепляется на строки, строки записываются в layOutStrings
private void layOutTextToVector(TextIterator textIterator, int linesAmount, Vector layOutStrings){
	//Очищаем строки
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

//читает текст от позиции на которую указывет textIterator 
//пока не закончится файл или пока не достигнем endTextFilePosition
//текст расщепляется на строки, позиции с которых началась строка записываются в positionsInFile
private Vector createVectorBeginPositionsLayoutStrings(TextIterator textIterator, int endTextFilePosition){
	Vector positionsInFile = new Vector();
	while(textIterator.getPosition()<endTextFilePosition){
		//Записываем позицию с которой начинается эта строка
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

//Формирует строку текста от текущей позиции textIterator
//Строка текста не превышает размер экрана
//Если стрка кончается буквой, букву убираем  ставим знак переноса
private String splitOneLine(TextIterator textIterator){
	String result = new String();
	int stringSize = 0;
	byte currentChar = 0;
	//Пока нетдостигли конца экрана и пока не конец абзаца
	while((stringSize<screenWidth) && (!textIterator.isEndString()) && (!textIterator.isEndFile())){
		currentChar = textIterator.getChar();
		stringSize += fontNavigator.getCharWidth(currentChar);
		//Если размер строки превысил ширину экрана прерываем цикл
		if (stringSize>screenWidth) break;
		
		result += TextUtilites.byteToCP1251(currentChar);
		
		//Если конец файла прерываем цикл
		if (!textIterator.nextChar()) break;
	}
	
	//Если мы остановились по причине конца строки, делать перенос вообще ненужно
	if (textIterator.isEndString()) return result;
	
	
	//Если текущий символ буква, то, в место неё необходимо поставить знак переноса
	//И сделать отступ назад
	//Дело в том что сейчас мы находимся за чертой экрана, а символ который нужно заменит на тире
	//находится перед ним
	textIterator.pervChar();
	//System.out.println("splitOneLine --- Последний символ >>> "+TextUtilites.byteToCP1251(textIterator.getChar())+" code = "+textIterator.getChar());
	int charType = TextUtilites.classificateChar(textIterator.getChar());
	textIterator.nextChar();
	
	if ((charType == TextUtilites.RUSSIAN_CHAR) || (charType == TextUtilites.ENGLISH_CHAR)){
		
		//System.out.println("БУКВА ОДНАКО");
		return result.substring(0, result.length())+'-';
	} else {
		System.out.println("Символ");
	}
	return result;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////



private void findBeginParagraphFromBack(TextIterator textIterator){
	int counterChar = 0;
	while(!textIterator.isEndString()){
		//System.out.println("findBeginParagraphFromBack >>> "+ TextUtilites.byteToCP1251(textIterator.getChar())+ " code = "+textIterator.getChar());
		//Если левая граница файла
		if (!textIterator.pervChar()) return;
		counterChar++;
		if (counterChar>FIND_PARAGRAPH_UP_LIMIT) return;
	}
	
	//Пропускаем 10+13
	//textIterator.nextChar();
	//textIterator.nextChar();
	textIterator.skipNEL();
}



public void jumpUpLines(int jumpLines){
	Vector positions = null;
	int oldPosition;
	int fillLines = 0; //Счётчик заполненных строк
	
	//Выше некуда
	if (screenPointer == 1) return;
	
	while(fillLines<jumpLines){
		textIterator.setPosition(screenPointer);
		
		//Ищем от текущей позиции начало абзацца (вверх по тексту)
		System.out.println("screenUp cicle textIterator.getPosition() >>> "+textIterator.getPosition());
		oldPosition = textIterator.getPosition();
		findBeginParagraphFromBack(textIterator);
		screenPointer = textIterator.getPosition();
		
		//Если нас вернуло в исходную позицию значит мы наткнулись на идущие подряд переносы строк
		//Потому-что при поиске начала абзаца на смещает к его началу за 10+13, и мы оказываемся на одну позицию дальше чем были до поиска
		if (screenPointer>=oldPosition){
			System.out.println("!!!!!!!!!!Наткнулись на идущие подряд переносы строк!!!!!!!!!!!!!");
			System.out.println("oldPosition = "+oldPosition+" screenPointer = "+screenPointer);
			fillLines++;
			screenPointer -= 3;
			continue;
		}
		
		//
		
		System.out.println("screenUp cicle >>> "+ TextUtilites.byteToCP1251(textIterator.getChar())+" code = "+textIterator.getChar());
		
		//Получаем начала (в байтах файла) размещённых строк от найденной позиции до начальной позиции (oldPosition) 
		positions = createVectorBeginPositionsLayoutStrings(textIterator, oldPosition);
		System.out.println("Текст разместили positions.size() = "+positions.size());
		
		//Если строк не хватает на экран, прожолжаем
		fillLines += positions.size();
		
		//Пропускаем 10+13
		screenPointer -= 3; //3 - потому-что символ на который указывает, 13, а потом 10
		if (screenPointer<0) break;
	}
	
	//Строк прочитанно больше чем надо
	if (fillLines>jumpLines){
		System.out.println("Прочитанных линий больше чем вмещает экран fillLines = "+fillLines+" jumpLines = "+jumpLines);
		//нам нужна позиция с которой начинается первая линия экрана
		screenPointer = ((Integer)positions.elementAt(fillLines-jumpLines)).intValue();
	} else {//Такое случается когда выходим из цикла не прямым условием
		System.out.println("Прочитанных линий меньше чем вмещает экран fillLines = "+fillLines+" jumpLines = "+jumpLines);
		screenPointer = ((Integer)positions.elementAt(0)).intValue();
	}
	
	textIterator.setPosition(screenPointer);
	//С этой позиции заполняем экран
	layOutTextToVector(textIterator, screenLines, linesVector);
}

public void jumpDownLines(int lines){
}

///////////////////НАВИГАЦИЯ//////////////////////////////
public void screenDown(){
	//Пропускаем один экран строк
	textIterator.setPosition(screenPointer);
	layOutTextToVector(textIterator, screenLines-1, linesVector);
	
	//Текщая позиция теперь на экран ниже
	screenPointer = textIterator.getPosition();
	
	//Заполняем новый экран
	layOutTextToVector(textIterator, screenLines, linesVector);
}

public void screenUp(){
	jumpUpLines(screenLines);
}

//По одной линии вверх-вниз
public void lineUp(){
	jumpUpLines(1);
}

public void lineDown(){
	System.out.println("LD");
	//Пропускаем одну линию
	textIterator.setPosition(screenPointer);
	splitOneLine(textIterator);
	if (textIterator.isEndString()){
			//textIterator.nextChar();
			//textIterator.nextChar();
			textIterator.skipNEL();
	}
	
	//Текщая позиция теперь на линию ниже
	screenPointer = textIterator.getPosition();
	
	//Заполняем новый экран
	layOutTextToVector(textIterator, screenLines, linesVector);
}


//////////////////////////////////////////////////////////

/////////////////ШРИФТ////////////////////////////////////

public void fontIncrease(){
	fontNavigator.fontIncrease();
	
	//Пересчитываем колличество строк помещающихся на экране
	calculateScreenLines();
	
	//Обновляем вектор строк
	textIterator.setPosition(screenPointer);
	layOutTextToVector(textIterator, screenLines, linesVector);
}

public void fontDecrease(){
	fontNavigator.fontDecrease();
	
	//Пересчитываем колличество строк помещающихся на экране
	calculateScreenLines();
	
	//Обновляем вектор строк
	textIterator.setPosition(screenPointer);
	layOutTextToVector(textIterator, screenLines, linesVector);
}

public Font getCurrentFont(){
	return fontNavigator.getFont();
}

public int getLineHeight(){
	return fontNavigator.getFont().getHeight();
}


//Возвращает строку по номеру относительно firstScreenLine
public String getLine(int lineNum){
	if (lineNum >= linesVector.size()) return null;
	return (String)linesVector.elementAt(lineNum);
}

public int getPositionInFile(){
	return screenPointer;
}
}