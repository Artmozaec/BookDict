package onestoremanyrecords;

import javax.microedition.rms.*;
import java.util.Vector;

public class OneStoryManyRecords{

//��� ���������
public String recordStoreName;

//��������� ������ ������ ���������
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

//���������� ����-�� ��������� ��� ������ �� ���� ������
private boolean recordStoreCheck(){
	String[] stores;

	//�������� ������ ��������
	stores = RecordStore.listRecordStores();
	
	//��� ������ ���������� ������ ���!
	if (stores == null) return false;

	//���� �� � ������ ������ ��� ���������?
	if (!existRecordName(stores)) return false;

	return true;
}


public Vector restoreData(){
	Vector storeRecords = new Vector();
	
	//���������, ������������-�� ������ ���������?
	if (!recordStoreCheck()){
		//System.out.println("��� ���������");
		errorStore = true;
		return storeRecords;
	}
	
	StoreRecord currentRecord;
	RecordStore recordStore;
	try{
		//������� ���������
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
		//System.out.println("otkrili hranilishe recordStore = "+recordStore);
		//��������� �� ���� ������
		//System.out.println("������� � ��������� = "+recordStore.getNumRecords());
		for (int ch=1; ch<=recordStore.getNumRecords(); ch++){
			currentRecord = new StoreRecord(recordStore.getRecord(ch));
			storeRecords.addElement(currentRecord);
			//System.out.println("$$$$$$$$$$$$$$$$restoreData()$$$$$$$$$$$$$$$$$="+ch);
		}
		//������� ���������
		recordStore.closeRecordStore();
		//System.out.println("zakryli hranilishe ");
	} catch (RecordStoreException rse){
		errorStore = true;
	}
	
	return storeRecords;
}

private void deleteStore(){
	try{
		//������� ���������
		RecordStore.deleteRecordStore(recordStoreName);
	} catch( RecordStoreNotFoundException e ){
		// ��� ������ ���������
		//System.out.println("deleteStore() >>>> netHranilisha");
	} catch( RecordStoreException e ){
		// ��������� �������
		//System.out.println("deleteStore() >>>> hranilishe otkrito");
	}
}


public void saveData(Vector records){
	RecordStore recordStore;
	//������� ������ ���������
	deleteStore();
	try{
		//������� �����
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
		StoreRecord currentRecord;
		for (int ch=0; ch<records.size(); ch++){
			currentRecord = (StoreRecord)records.elementAt(ch);
			byte[] rec = currentRecord.getData();
			recordStore.addRecord(rec, 0, rec.length);
			//System.out.println("$$$$$$$$$$$$$$$$saveData()$$$$$$$$$$$$$$$$$="+ch);
		}
		
		//������� ���������
		recordStore.closeRecordStore();
		
	} catch (RecordStoreException rse){
		errorStore = true;
	}
}



}