package bookpages;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;


//����� ������� ������-������� ����������� ��������� ������� ������ ��������� 
public class CheckName{

//���������� true ���� �� ���� ������������ � ��������� ������������� ���� ����
public static boolean checkFileExist(String filePatch){
	FileConnection fileConnect = null;
	boolean result;
	try {
		//System.out.println("�������� >>>" + filePatch);
		fileConnect = (FileConnection)Connector.open(filePatch);
		result = fileConnect.exists();
		fileConnect.close();
		//System.out.println(">>>>>checkFileExist<<<<<< result >>>"+ filePatch +"  >>>"+ result);
	}catch(IOException ioe){
		//System.out.println(">>>>>checkFileExist<<<<<<S FAILOM FIGNYA!!!");
		return false;
	}
	
	return result;
}


}