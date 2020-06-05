package onestoremanyrecords;

public class StoreRecord{

private byte[] recordData;

private int readCursor;
private int writeCursor;

//������� $$$
private static final byte[] DELIMITER = new byte[]{0x24, 0x24, 0x24};
private static final int RECORD_DATA_SIZE = 300;

public StoreRecord(){
	readCursor = 0;
	writeCursor = 0;
	
	recordData = new byte[RECORD_DATA_SIZE];
}

public StoreRecord(byte[] inRecordData){
	readCursor = 0;
	writeCursor = 0;
	recordData = inRecordData;
}

//�����������-�� ������� � ������� pos?
private boolean isDelimiter(int pos){
	if (pos+3>recordData.length) return false;
	if( 
	(recordData[pos] == DELIMITER[0]) &&
	(recordData[pos+1] == DELIMITER[1]) &&
	(recordData[pos+2] == DELIMITER[2])
	) return true;
	return false;
}

private void skipDelimiter(){
	if (readCursor+3>recordData.length) return;
	readCursor+=3;
}

//����� ���������� �����������
private void searchDelimiter(){
	while (readCursor<recordData.length){
		if (isDelimiter(readCursor)) return;
		readCursor++;
	}
}

public byte[] getData(){
	byte[] data = new byte[writeCursor];
	
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)	
	System.arraycopy(recordData, 0, data, 0, data.length);
	
	return data;
}


public byte[] getPart(){
	int beginPart = readCursor;
	searchDelimiter();
	
	byte[] part = new byte[readCursor-beginPart];
	
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)	
	System.arraycopy(recordData, beginPart, part, 0, part.length);
	
	//���������� �����������
	skipDelimiter();
	
	return part;
}

private void writeDelimiter(){
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)	
	System.arraycopy(DELIMITER, 0, recordData, writeCursor, DELIMITER.length);
	writeCursor+=DELIMITER.length;
}

public void putPart(byte[] inPart){
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)	
	System.arraycopy(inPart, 0, recordData, writeCursor, inPart.length);
	
	writeCursor+=inPart.length;
	writeDelimiter();
}

}