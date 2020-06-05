package dictionary;

public class Dictionary{
private String dirPatch;

//��������� ������������ � ����� ������� ���� ������ �� ������� � �������
public static final int NOT_FOUND = -1;

//��������� �������� ����� ������ � ���������� �������
private static final String DICTIONARY_FILE_NAME = "dict.txt";
private static final String DICTIONARY_INDEX_FILE_NAME = "index.txt";

//�������� ������� ������������ ��������� ���� �������� � ����� �������� ������� 
//��� ����� ������������ �� ��� ����� ������������
private DictionaryIndexes dictionaryIndexes;

//��� ���� � ������� ��������� �����
private DictionaryFinder dictionaryFinder;

public Dictionary(String patch){
	dirPatch = patch;
	
	//��������� ���� ��������
	dictionaryIndexes = new DictionaryIndexes(patch+DICTIONARY_INDEX_FILE_NAME);
	
	//�������� � �������
	dictionaryFinder = new DictionaryFinder(dirPatch+DICTIONARY_FILE_NAME);
}

//���������� ������� � ����� � ������� ���������� ����� �������� �����
//��� ��������� NOT_FOUND
public int findWord(String word){
	//��������� ����� � ������ �������
	word = word.toLowerCase();
	
	String beginChars;
	
	if (word.length() > 2){ 
		beginChars = word.substring(0, 2);
	} else {
		beginChars = word.substring(0, 1)+"a";
	}
	
	System.out.println("find word >>>>>>"+word);
	System.out.println("find beginChars >>>>>>"+beginChars);
	//��������� ������� ������
	int beginFindPos = dictionaryIndexes.getBeginPosition(beginChars);
	System.out.println("beginFindPos >>>>>>"+beginFindPos);
	//������ ������� ������
	int areaLength = dictionaryIndexes.getCharsDictonaryAreaLength(beginChars);
	System.out.println("areaLength >>>>>>"+areaLength);
	
	return dictionaryFinder.findWord(word, beginFindPos, areaLength);
	//return 0;
}

public String getDictionaryPatch(){
	return dirPatch+DICTIONARY_FILE_NAME;
}

}