package filetextreader;

public class TextUtilites{

//������ ��� ��������������
public static final String WIN1251_TO_UNICODE = "\u0402\u0403\u201a\u0453\u201e\u2026\u2020\u2021\u20ac\u2030\u0409\u2039\u040a\u040c\u040b\u040f\u0452\u2018\u2019\u201c\u201d\u2022\u2013\u2014\ufffd\u2122\u0459\u203a\u045a\u045c\u045b\u045f\u00a0\u040e\u045e\u0408\u00a4\u0490\u00a6\u00a7\u0401\u00a9\u0404\u00ab\u00ac\u00ad\u00ae\u0407\u00b0\u00b1\u0406\u0456\u0491\u00b5\u00b6\u00b7\u0451\u2116\u0454\u00bb\u0458\u0405\u0455\u0457\u0410\u0411\u0412\u0413\u0414\u0415\u0416\u0417\u0418\u0419\u041a\u041b\u041c\u041d\u041e\u041f\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042a\u042b\u042c\u042d\u042e\u042f\u0430\u0431\u0432\u0433\u0434\u0435\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044a\u044b\u044c\u044d\u044e\u044f";

public static final int DIGIT_CHAR = 0;
public static final int SYMBOLS_CHAR = 1;
public static final int RUSSIAN_CHAR = 2;
public static final int ENGLISH_CHAR = 3;


public static char byteToCP1251(byte ch) {
	char chr;
	chr = (char)(ch & 0x000000FF);
	return (chr < 128) ? chr : WIN1251_TO_UNICODE.charAt(chr-128);
}

public static int classificateChar(byte ch){
	char chr;
	chr = (char)(ch & 0x000000FF);
	if ((chr>=48) && (chr<=57)) return DIGIT_CHAR;
	if ((chr>=65) && (chr<=122)) return ENGLISH_CHAR;
	if ((chr>=192) && (chr<=255)) return RUSSIAN_CHAR;
	return SYMBOLS_CHAR;
}

}