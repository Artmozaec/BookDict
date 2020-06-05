package bookpages;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;


//Здесь собраны методы-утилиты позволяющие проверять наличие одного фрагмента 
public class CheckName{

//Возвращает true если по пути пререданному в параметре действительно есть файл
public static boolean checkFileExist(String filePatch){
	FileConnection fileConnect = null;
	boolean result;
	try {
		//System.out.println("ПРОВЕРКА >>>" + filePatch);
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