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

//������� ��������� � ���������� �������� �������
public static final String FS_PREFIX = "file://";

private String currentPatch;

/////////////////���������///////////////////////////////////////////////////////////////////
FileSystemWalker(String beginPatch){
	if (beginPatch == null){ //���� ��������� ���� ������, �� ������������� ����� ������� ��
		currentPatch=MEGA_ROOT;
	} else {	
		currentPatch=beginPatch.substring(FS_PREFIX.length(), beginPatch.length());
		//System.out.println("����������� FileBrowser >>>>" + currentPatch);
		traverseDirectory(UP_DIRECTORY);
	}
}


public void traverseDirectory(String folderName){
	//���� MEGA_ROOT ������ ���
	if((currentPatch.equals(MEGA_ROOT)) && (folderName.equals(UP_DIRECTORY))) return;
	
	if (folderName.equals(UP_DIRECTORY)){ //����������� �� ���� ���������� �����
		//������� ��������� ��������� � ������ ������� �����������
		int i=currentPatch.lastIndexOf(SEP,currentPatch.length()-2); //� -2 ������-��� �� ����� ���� ��� ����� ���� ��������� �����������!
		
		if (i!=-1){
			//�������� ��������� �� ������ �� �������������� �����������
			currentPatch=currentPatch.substring(0, i+1);
		} else {
			// -1 -��� ������ ��� ����������� ��� �� ����������
			currentPatch=MEGA_ROOT;
		}
	} else { //�� ���� ���������� � �����
		currentPatch=currentPatch+folderName;
	}

	//���������� ��-��� ����������
	//showCurrDir();
}

public Vector getFolders(){
	//�������� ������ ���� ������ � �����������
	Enumeration folderContent = getFolderContent(currentPatch);

	//��������� folderContent �� 2 ������� ����� � �����
	Vector[] foldersFiles = splitFolderAndFiles(folderContent);
	
	//���������� ������ �����
	return foldersFiles[1];
}

public Vector getFiles(){
	//�������� ������ ���� ������ � �����������
	Enumeration folderContent = getFolderContent(currentPatch);

	//��������� folderContent �� 2 ������� ����� � �����
	Vector[] foldersFiles = splitFolderAndFiles(folderContent);
	
	//���������� ������ �����
	return foldersFiles[0];
}

public String getCurrentPatch(){
	return currentPatch;
}
////////////////////////////////////////////////////////////////////////////





















//���������� �� ���������� ���� patch - ����� � �����
private Enumeration getFolderContent(String patch){
	//System.out.println("getFolderContent " + patch);
	FileConnection currDir = null;
	Enumeration folderContent = null;
	//���� �� �� ������� �������� �������� ������ ���������� ������ ����������
	if (MEGA_ROOT.equals(patch)){
		folderContent = FileSystemRegistry.listRoots();
	}else{//����� �������� ������ �� �������� ����	
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

//��������� ���������� �� ��� ������� � ���������� ��������� � ���� ������� 
//0 - ������� �����, 1 - ������� �����
private Vector[] splitFolderAndFiles(Enumeration content){
	Vector folders = new Vector();
	Vector files = new Vector();
	
	while(content.hasMoreElements()){
		String name = (String)content.nextElement();
		//System.out.println("splitFolderAndFiles ���� = " + name);
		if (name.charAt(name.length()-1)==SEP){ 
			//���� ��� ������������� ������������-��� �����
			//System.out.println("����� = " + name);
			folders.addElement(name);
		}else{
			//����� ����
			//System.out.println("���� = " + name);
			files.addElement(name);
		}
	}
	
	//���������� ��� ������� � ����� �������
	return new Vector[] {files, folders};
}



}