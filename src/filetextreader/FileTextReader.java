package filetextreader;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;

class FileTextReader extends Thread{
byte[] bufferText;
String filePatch;
int ident;
int bufferSize;

FileTextReader(String patch, int size, int inIdent){
	//bufferText = new byte[size];
	bufferText = null;
	bufferSize = size;
	filePatch = patch;
	ident = inIdent;
	
	this.start();
	//Здесь системный вызывающий тред останавливается и ждёт завершения треда объекта this - то есть 
	//этого объекта

	
		try {
			this.join();
		} catch(InterruptedException e) {}	
}


public void run() {
	FileConnection fileConnect = null;
	InputStream fileInStream = null;
	//System.out.println("Nachali!!!! >>>>>>>>"+filePatch);
	int fileSize = 0;
	try {
		fileConnect = (FileConnection)Connector.open(filePatch);
			
		//Если размер файла меньше чем размер буфера и отступа
		fileSize = (int)fileConnect.fileSize();
		//Обрезаем размер буфера до нужной величины
		if (ident+bufferSize>fileSize){
			bufferSize = bufferSize - ((ident+bufferSize)-fileSize);
		}
		//System.out.println("bufferSize = "+bufferSize);
		bufferText = new byte[bufferSize];
		
		//System.out.println("fileSize = "+fileConnect.fileSize());
		
		//System.out.println("Otkrili soedinenie");
		fileInStream = fileConnect.openInputStream();
				
		//Пропускаем отступ ident
		fileInStream.skip(ident);
		
		//Читаем данные в массив
		fileInStream.read(bufferText);
		
		//Закрываем поток и соединение
		fileInStream.close();
		fileConnect.close();
		
		//Так, на всякий случай...
		fileInStream = null;
		fileConnect = null;
	} catch(IOException ioe) {
		//Нужный файл не-был найден!
		System.out.println("FileTextReader >>>> S FAILOM FIGNYA!!!");
		bufferText = null;
	}	
	//System.out.println("ZAKOCHILI!!!! >>>>>>>>"+filePatch);
}

public byte[] getText(){
	return bufferText;
}

public static int getFileSize(String patch){
	FileConnection fileConnect = null;
	int fileSize = 0;
	try {
		fileConnect = (FileConnection)Connector.open(patch);
		fileSize = (int)fileConnect.fileSize();
	} catch(IOException ioe) {
		//Нужный файл не-был найден!
		System.out.println("getFileSize >>>> S FAILOM FIGNYA!!!");
		return -100;
	}
	
	return fileSize;
}

}