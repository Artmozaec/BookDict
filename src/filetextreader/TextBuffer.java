package filetextreader;

import bookpages.CheckName;

public class TextBuffer{

int bufferSize;
int positionInFile;

String filePatch;

//Размер файла по пути filePatch
int fileSize;

//Содержит текущий кадр данных
byte[] buffer;

public TextBuffer(String patch, int size){
	buffer = null;
	filePatch = patch;
	bufferSize = size;
	positionInFile = 0;
	//Узнаем размер читаемого файла
	fileSize = 0;
	if (fileIsExist()) fileSize = FileTextReader.getFileSize(patch);
	//Это нужно переделать! Дело в том что по умолчанию передаётся путь E:\\\\ А его размер = -100
	if (fileSize<0) fileSize = 0;
}

public boolean fileIsExist(){
	return CheckName.checkFileExist(filePatch);
}

private void jumpTo(int requiredPos){
	//Если запрашиваемая область дальше границ файла генерируем исключительную ситуацию
	if (requiredPos>fileSize){
		throw new ArrayIndexOutOfBoundsException("!jump to! requiredPos = "+requiredPos+" fileSize = "+fileSize);
	}
	//Выбираем положение окна таким образом что-бы требуемая буква была посредине этого окна
	positionInFile = requiredPos - (bufferSize/2);
	
	//System.out.println("jumpTo - requiredPos = "+requiredPos+" positionInFile = "+positionInFile);
	if (positionInFile<0) positionInFile = 0;
	
	FileTextReader reader = new FileTextReader(filePatch, bufferSize, positionInFile);
	buffer = reader.getText();
}

public byte getCharAt(int charPos){
	if (charPos<1) return 0;
	charPos--;//Это потому-что счёт байтов в файле начинается с 1, а массив начинается с 0
	//Если запрашиваемая позиция лежит за пределами кадра 
	//или это первое обращение buffer == null
	if((buffer == null) || (charPos<positionInFile) || (charPos>=(positionInFile+buffer.length))){
		//Соверщаем скачок в ту область
		jumpTo(charPos);
	}
	
	//Такое может случится, что файла нет, тогда jumpTo сделает buffer null
	if (buffer == null) return 0;
	
	//System.out.println("charPos-positionInFile = "+(charPos-positionInFile));
	return buffer[charPos-positionInFile];
}

public int getFileSize(){
	return fileSize;
}

public TextIterator getTextIterator(){
	return new TextIterator(this);
}

}