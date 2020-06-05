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

//������ �� ���������� �������� �����
private FileBrowserListener listener;

//������ ��� ���������� �� ������� ������� ����� ����������� ��������-�� ���������� ����� ������� ��������� � ������
private String[] needFolders;

//����������� ����� ����� � ���������� ����������
private final String needFileJPG = "001.jpg";
private final String needFileGIF = "001.gif";
private final String needFilePNG = "001.png";

//inListener - ��������� ����������� ������
public FolderBrowser(FileBrowserListener inListener, String beginPatch){
	//������ ��� List
	super(FileSystemWalker.UP_DIRECTORY, List.IMPLICIT);
	
	fileSystemWalker = new FileSystemWalker(beginPatch);
	
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


//���������� ����� � ������� content ������ searchString
private boolean existName(Vector content, String searchString){
	String name;
	//System.out.println("existName content.size() = " + content.size());
	for (int ch=0; ch<content.size(); ch++){
		//�������� ��� ���������� � ��������� ��� � ������ �������
		name = ((String)content.elementAt(ch)).toLowerCase();
		//System.out.println("name = " + name);
		if (searchString.equals(name)) return true;
	}
	return false;
}

//����� ��������� ������� �� �������� ���� ����������� ������
private boolean isTrueContentFolder(){
	//�������� ������ ������ �� �������� ����
	Vector content = fileSystemWalker.getFiles();
	
	//���� �� � ����� ������ �������� ����� � ������� jpg?
	if (existName(content, needFileJPG)) return true;
	
	//�����-���� ���� � ������� png?
	if (existName(content, needFilePNG)) return true;
	
	//�� ������� ��� ���
	return false;
}


//��������� ������� � ������� ���������� �������������
private boolean folderIsNotEmpty(){
	//�������� ������ ���������� �� �������� ����
	Vector content = fileSystemWalker.getFolders();
	
	if (content.size()>0) return true;
	return false;
}

public void commandAction(Command c, Displayable d){
	//�������� ���������� ���������� ������
	List curr = (List)d;
	final String selectDir = curr.getString(curr.getSelectedIndex());
	if (c == back){//�����, � ����������� ������
		listener.goToBackScreen();
		return;
	}
	new Thread (new Runnable(){
		public void run(){
			
			//���� ��������� ����� ������ ������� �� ������� ����
			if(selectDir.equals(FileSystemWalker.UP_DIRECTORY)){
				fileSystemWalker.traverseDirectory(selectDir);
			}else{ 
				//System.out.println("����� ");
				fileSystemWalker.traverseDirectory(selectDir);
				if (isTrueContentFolder()){ //��������� �� ������� ����������� ��������� ����� � ������
					//������� ���� ���� �����������
					listener.patchChoosed(fileSystemWalker.FS_PREFIX+fileSystemWalker.getCurrentPatch());
				}
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
}




}
