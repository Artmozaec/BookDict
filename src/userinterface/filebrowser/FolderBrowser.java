package filebrowser;

import java.io.*;

import java.util.*;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class FolderBrowser extends List implements CommandListener{

private Command view;
private Command back;

private Image dirIcon;

private FileSystemWalker fileSystemWalker;

//Ссылка на обработчик событийк ласса
private FileBrowserListener listener;

//Массив имён директорий по наличию которых будет определятся является-ли выбираемая папка рабочим каталогом с картой
private String[] needFolders;

//Необходимые файлы книги в выбираемой директории
private final String needFileJPG = "001.jpg";
private final String needFileGIF = "001.gif";
private final String needFilePNG = "001.png";

//inListener - интерфейс обработчика выбора
public FolderBrowser(FileBrowserListener inListener, String beginPatch){
	//Создаём сам List
	super(FileSystemWalker.UP_DIRECTORY, List.IMPLICIT);
	
	fileSystemWalker = new FileSystemWalker(beginPatch);
	
	//команда назад
	back = new Command("назад", Command.BACK, 0);
	this.setSelectCommand(back);
	
	//Команда перехода
	view = new Command("View", Command.ITEM, 0);
	this.setSelectCommand(view);
	
	

	this.setCommandListener(this);

	
	listener=inListener;
	


	try{
		dirIcon=Image.createImage("/icons/dir.png");
	}catch(IOException e){
		dirIcon=null;
	}

	try{
		showCurrDir();
	}catch(SecurityException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
}


//Производит поиск в векторе content строки searchString
private boolean existName(Vector content, String searchString){
	String name;
	//System.out.println("existName content.size() = " + content.size());
	for (int ch=0; ch<content.size(); ch++){
		//Получаем имя директории и переводим его в нижний регистр
		name = ((String)content.elementAt(ch)).toLowerCase();
		//System.out.println("name = " + name);
		if (searchString.equals(name)) return true;
	}
	return false;
}

//метод проверяет наличие по текущему пути необходимых файлов
private boolean isTrueContentFolder(){
	//Получаем список файлов по текущему пути
	Vector content = fileSystemWalker.getFiles();
	
	//Есть ли в папке первая страница книги в формате jpg?
	if (existName(content, needFileJPG)) return true;
	
	//Может-таки есть в формате png?
	if (existName(content, needFilePNG)) return true;
	
	//Да нихрена там нет
	return false;
}


//проверяет наличие в текущей директории субдиректорий
private boolean folderIsNotEmpty(){
	//Получаем список директорий по текущему пути
	Vector content = fileSystemWalker.getFolders();
	
	if (content.size()>0) return true;
	return false;
}

public void commandAction(Command c, Displayable d){
	//Выбираем содержимое выбранного пункта
	List curr = (List)d;
	final String selectDir = curr.getString(curr.getSelectedIndex());
	if (c == back){//Назад, к предыдущему экрану
		listener.goToBackScreen();
		return;
	}
	new Thread (new Runnable(){
		public void run(){
			
			//Если выбранная папка строка подъёма на уровень выше
			if(selectDir.equals(FileSystemWalker.UP_DIRECTORY)){
				fileSystemWalker.traverseDirectory(selectDir);
			}else{ 
				//System.out.println("ПАПКА ");
				fileSystemWalker.traverseDirectory(selectDir);
				if (isTrueContentFolder()){ //Проверяем на наличие необходимых признаков папки с картой
					//передаём этот путь обработчику
					listener.patchChoosed(fileSystemWalker.FS_PREFIX+fileSystemWalker.getCurrentPatch());
				}
			}
			showCurrDir();
		}
	}).start();
}

private void showCurrDir(){
	//Удаляем все элементы текущего списка
	this.deleteAll();
	
	//Устанавливаем заголовок - текущий путь
	this.setTitle(fileSystemWalker.getCurrentPatch());
	
	//Если не самая вершина иерархии - (список логических дисков)
	if (!fileSystemWalker.MEGA_ROOT.equals(fileSystemWalker.getCurrentPatch())) this.append(FileSystemWalker.UP_DIRECTORY,dirIcon); //Добавляем команду выхода на уровень выше
	
	//получаем список папок
	Vector folders = fileSystemWalker.getFolders();
	
	//Создаём список папок
	for (int ch=0; ch<folders.size(); ch++){
		this.append((String)(folders.elementAt(ch)), dirIcon);
	}
}




}
