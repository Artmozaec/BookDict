package onestoremanyrecords;

import javax.microedition.rms.*;
import java.util.Vector;

public class OneStoryManyRecords{

//Имя хранилища
public String recordStoreName;

//Индикатор ошибки чтения хранилища
private boolean errorStore;

public OneStoryManyRecords(String inStoreName){
	recordStoreName = inStoreName;
	errorStore = false;
}

public boolean isOk(){
	return !errorStore;
}

private boolean existRecordName(String[] stores){
	for(int ch=0; ch<stores.length; ch++){
		//System.out.println("store = "+stores[ch]);
		if (recordStoreName.equals(stores[ch])) return true;
	}
	return false;
}

//Определяет есть-ли хранилище для чтения из него данных
private boolean recordStoreCheck(){
	String[] stores;

	//Получить список хранилищ
	stores = RecordStore.listRecordStores();
	
	//Оно иногда возвращало именно это!
	if (stores == null) return false;

	//Есть ли в списке нужное нам хранилище?
	if (!existRecordName(stores)) return false;

	return true;
}


public Vector restoreData(){
	Vector storeRecords = new Vector();
	
	//Проверяем, присутствует-ли нужное хранилище?
	if (!recordStoreCheck()){
		//System.out.println("нет хранилища");
		errorStore = true;
		return storeRecords;
	}
	
	StoreRecord currentRecord;
	RecordStore recordStore;
	try{
		//Открыть хранилище
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
		//System.out.println("otkrili hranilishe recordStore = "+recordStore);
		//Прочитать из него данные
		//System.out.println("Записей в хранилище = "+recordStore.getNumRecords());
		for (int ch=1; ch<=recordStore.getNumRecords(); ch++){
			currentRecord = new StoreRecord(recordStore.getRecord(ch));
			storeRecords.addElement(currentRecord);
			//System.out.println("$$$$$$$$$$$$$$$$restoreData()$$$$$$$$$$$$$$$$$="+ch);
		}
		//Закрыть хранилище
		recordStore.closeRecordStore();
		//System.out.println("zakryli hranilishe ");
	} catch (RecordStoreException rse){
		errorStore = true;
	}
	
	return storeRecords;
}

private void deleteStore(){
	try{
		//Удалить хранилище
		RecordStore.deleteRecordStore(recordStoreName);
	} catch( RecordStoreNotFoundException e ){
		// нет такого хранилища
		//System.out.println("deleteStore() >>>> netHranilisha");
	} catch( RecordStoreException e ){
		// хранилище открыто
		//System.out.println("deleteStore() >>>> hranilishe otkrito");
	}
}


public void saveData(Vector records){
	RecordStore recordStore;
	//Удаляем старое хранилище
	deleteStore();
	try{
		//Создать новое
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
		StoreRecord currentRecord;
		for (int ch=0; ch<records.size(); ch++){
			currentRecord = (StoreRecord)records.elementAt(ch);
			byte[] rec = currentRecord.getData();
			recordStore.addRecord(rec, 0, rec.length);
			//System.out.println("$$$$$$$$$$$$$$$$saveData()$$$$$$$$$$$$$$$$$="+ch);
		}
		
		//Закрыть хранилище
		recordStore.closeRecordStore();
		
	} catch (RecordStoreException rse){
		errorStore = true;
	}
}



}