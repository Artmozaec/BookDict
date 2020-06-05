package dictionary;

public class Dictionary{
private String dirPatch;

//Константа обозначающая в место индекса если строка не найдена в словаре
public static final int NOT_FOUND = -1;

//Константы задающие имена файлов в директории словаря
private static final String DICTIONARY_FILE_NAME = "dict.txt";
private static final String DICTIONARY_INDEX_FILE_NAME = "index.txt";

//Содержит таблицу соответствий начальных букв алфавита и начал областей словаря 
//где слова начинающиеся на эти буквы расположенны
private DictionaryIndexes dictionaryIndexes;

//Оно ищет в словаре требуемое слово
private DictionaryFinder dictionaryFinder;

public Dictionary(String patch){
	dirPatch = patch;
	
	//Загружаем файл индексов
	dictionaryIndexes = new DictionaryIndexes(patch+DICTIONARY_INDEX_FILE_NAME);
	
	//Искатель в словаре
	dictionaryFinder = new DictionaryFinder(dirPatch+DICTIONARY_FILE_NAME);
}

//Возвращает позицию в файле с которой начинается текст искомого слова
//Или константу NOT_FOUND
public int findWord(String word){
	//Переводим слово в нижний регистр
	word = word.toLowerCase();
	
	String beginChars;
	
	if (word.length() > 2){ 
		beginChars = word.substring(0, 2);
	} else {
		beginChars = word.substring(0, 1)+"a";
	}
	
	System.out.println("find word >>>>>>"+word);
	System.out.println("find beginChars >>>>>>"+beginChars);
	//Начальная позиция поиска
	int beginFindPos = dictionaryIndexes.getBeginPosition(beginChars);
	System.out.println("beginFindPos >>>>>>"+beginFindPos);
	//Длинна области поиска
	int areaLength = dictionaryIndexes.getCharsDictonaryAreaLength(beginChars);
	System.out.println("areaLength >>>>>>"+areaLength);
	
	return dictionaryFinder.findWord(word, beginFindPos, areaLength);
	//return 0;
}

public String getDictionaryPatch(){
	return dirPatch+DICTIONARY_FILE_NAME;
}

}