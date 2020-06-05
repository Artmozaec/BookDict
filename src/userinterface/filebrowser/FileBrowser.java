package filebrowser;

import java.io.*;

import java.util.*;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class FileBrowser extends List implements CommandListener{
private Command view;
private Command back;

private Image dirIcon;
private Image fileIcon;

private FileSystemWalker fileSystemWalker;

//������ �� ���������� �������� �����
private FileBrowserListener listener;

public FileBrowser(FileBrowserListener inListener){
	super(FileSystemWalker.UP_DIRECTORY, List.IMPLICIT);
	fileSystemWalker = new FileSystemWalker(null);
		
	//������� �����
	back = new Command("�����", Command.BACK, 0);
	this.setSelectCommand(back);
	
	//������� ��������
	view = new Command("View", Command.ITEM, 0);
	this.setSelectCommand(view);
	
	
	this.setCommandListener(this);

	listener=inListener;
	
	try{
		dirIcon=Image.createImage("/icons/dir.png");
		fileIcon=Image.createImage("/icons/file.png");
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


public void commandAction(Command c, Displayable d){
	//�������� ���������� ���������� ������
	List curr = (List)d;
	final String selectItem = curr.getString(curr.getSelectedIndex());
	if (c == back){//�����, � ����������� ������
		listener.goToBackScreen();
		return;
	}
	new Thread (new Runnable(){
		public void run(){
			
			//���������� ����� - �����
			if (selectItem.charAt(selectItem.length()-1)==fileSystemWalker.SEP){
				fileSystemWalker.traverseDirectory(selectItem);
			} else {//������ ����
				listener.patchChoosed(fileSystemWalker.FS_PREFIX+fileSystemWalker.getCurrentPatch()+selectItem);
			}
			showCurrDir();
		}
	}).start();
}

private void showCurrDir(){
	//������� ��� �������� �������� ������
	this.deleteAll();
	
	//������������� ��������� - ������� ����
	this.setTitle(fileSystemWalker.getCurrentPatch());
	
	//���� �� ����� ������� �������� - (������ ���������� ������)
	if (!fileSystemWalker.MEGA_ROOT.equals(fileSystemWalker.getCurrentPatch())) this.append(FileSystemWalker.UP_DIRECTORY,dirIcon); //��������� ������� ������ �� ������� ����
	
	//�������� ������ �����
	Vector folders = fileSystemWalker.getFolders();
	
	//������ ������ �����
	for (int ch=0; ch<folders.size(); ch++){
		this.append((String)(folders.elementAt(ch)), dirIcon);
	}
	
	//�������� ������ ������
	Vector files = fileSystemWalker.getFiles();
	
	//������ ������ ������
	for (int ch=0; ch<files.size(); ch++){
		this.append((String)(files.elementAt(ch)), fileIcon);
	}
	
}

}