package filebrowser;

import java.io.*;

import java.util.*;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

class FileSystemWalker{

public static final String UP_DIRECTORY = "..";
public static final String MEGA_ROOT = "/";
public static final String SEP_STR = "/";
public static final char SEP = '/';

//Префикс обращения к устройству файловой системы
public static final String FS_PREFIX = "file://";

private String currentPatch;

/////////////////ИНТЕРФЕЙС///////////////////////////////////////////////////////////////////
FileSystemWalker(String beginPatch){
	if (beginPatch == null){ //Если начальный путь пустой, то устанавливаем самую вершину фс
		currentPatch=MEGA_ROOT;
	} else {	
		currentPatch=beginPatch.substring(FS_PREFIX.length(), beginPatch.length());
		//System.out.println("Конструктор FileBrowser >>>>" + currentPatch);
		traverseDirectory(UP_DIRECTORY);
	}
}


public void traverseDirectory(String folderName){
	//Выше MEGA_ROOT только бог
	if((currentPatch.equals(MEGA_ROOT)) && (folderName.equals(UP_DIRECTORY))) return;
	
	if (folderName.equals(UP_DIRECTORY)){ //Поднимаемся на одну директорию вверх
		//Находим последнее включение в строку символа разделителя
		int i=currentPatch.lastIndexOf(SEP,currentPatch.length()-2); //а -2 потому-что на самом деле нам нужен пред помледний разделитель!
		
		if (i!=-1){
			//Выбираем подстроку от начала до предпоследнего разделителя
			currentPatch=currentPatch.substring(0, i+1);
		} else {
			// -1 -это значит что разделитель нам не встретился
			currentPatch=MEGA_ROOT;
		}
	} else { //На одну директорию в глубь
		currentPatch=currentPatch+folderName;
	}

	//отображаем то-что получилось
	//showCurrDir();
}

public Vector getFolders(){
	//Получаем список всех файлов и директрорий
	Enumeration folderContent = getFolderContent(currentPatch);

	//Разделяем folderContent на 2 вектора файлы и папки
	Vector[] foldersFiles = splitFolderAndFiles(folderContent);
	
	//Возвращаем только папки
	return foldersFiles[1];
}

public Vector getFiles(){
	//Получаем список всех файлов и директрорий
	Enumeration folderContent = getFolderContent(currentPatch);

	//Разделяем folderContent на 2 вектора файлы и папки
	Vector[] foldersFiles = splitFolderAndFiles(folderContent);
	
	//Возвращаем только файлы
	return foldersFiles[0];
}

public String getCurrentPatch(){
	return currentPatch;
}
////////////////////////////////////////////////////////////////////////////





















//Возвращает по указанному пути patch - файлы и папки
private Enumeration getFolderContent(String patch){
	//System.out.println("getFolderContent " + patch);
	FileConnection currDir = null;
	Enumeration folderContent = null;
	//если мы на вершине иерархии получаем список логических дисков устройства
	if (MEGA_ROOT.equals(patch)){
		folderContent = FileSystemRegistry.listRoots();
	}else{//иначе получаем список по текущему пути	
		try{
			currDir=(FileConnection)Connector.open(FS_PREFIX+patch);
			folderContent=currDir.list();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		try{
			if (folderContent != null){
				currDir.close();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}
	return folderContent;
}

//разделяет содержимое на два вектора и возвращает результат в виде массива 
//0 - элемент файлы, 1 - элемент папки
private Vector[] splitFolderAndFiles(Enumeration content){
	Vector folders = new Vector();
	Vector files = new Vector();
	
	while(content.hasMoreElements()){
		String name = (String)content.nextElement();
		//System.out.println("splitFolderAndFiles тфьу = " + name);
		if (name.charAt(name.length()-1)==SEP){ 
			//Если имя заканчивается разделителем-это папка
			//System.out.println("папка = " + name);
			folders.addElement(name);
		}else{
			//Иначе файл
			//System.out.println("файл = " + name);
			files.addElement(name);
		}
	}
	
	//Возвращаем два вектора в одном массиве
	return new Vector[] {files, folders};
}



}