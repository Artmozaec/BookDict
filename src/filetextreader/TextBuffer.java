package filetextreader;

import bookpages.CheckName;

public class TextBuffer{

int bufferSize;
int positionInFile;

String filePatch;

//������ ����� �� ���� filePatch
int fileSize;

//�������� ������� ���� ������
byte[] buffer;

public TextBuffer(String patch, int size){
	buffer = null;
	filePatch = patch;
	bufferSize = size;
	positionInFile = 0;
	//������ ������ ��������� �����
	fileSize = 0;
	if (fileIsExist()) fileSize = FileTextReader.getFileSize(patch);
	//��� ����� ����������! ���� � ��� ��� �� ��������� ��������� ���� E:\\\\ � ��� ������ = -100
	if (fileSize<0) fileSize = 0;
}

public boolean fileIsExist(){
	return CheckName.checkFileExist(filePatch);
}

private void jumpTo(int requiredPos){
	//���� ������������� ������� ������ ������ ����� ���������� �������������� ��������
	if (requiredPos>fileSize){
		throw new ArrayIndexOutOfBoundsException("!jump to! requiredPos = "+requiredPos+" fileSize = "+fileSize);
	}
	//�������� ��������� ���� ����� ������� ���-�� ��������� ����� ���� ��������� ����� ����
	positionInFile = requiredPos - (bufferSize/2);
	
	//System.out.println("jumpTo - requiredPos = "+requiredPos+" positionInFile = "+positionInFile);
	if (positionInFile<0) positionInFile = 0;
	
	FileTextReader reader = new FileTextReader(filePatch, bufferSize, positionInFile);
	buffer = reader.getText();
}

public byte getCharAt(int charPos){
	if (charPos<1) return 0;
	charPos--;//��� ������-��� ���� ������ � ����� ���������� � 1, � ������ ���������� � 0
	//���� ������������� ������� ����� �� ��������� ����� 
	//��� ��� ������ ��������� buffer == null
	if((buffer == null) || (charPos<positionInFile) || (charPos>=(positionInFile+buffer.length))){
		//��������� ������ � �� �������
		jumpTo(charPos);
	}
	
	//����� ����� ��������, ��� ����� ���, ����� jumpTo ������� buffer null
	if (buffer == null) return 0;
	
	//System.out.println("charPos-positionInFile = "+(charPos-positionInFile));
	return buffer[charPos-positionInFile];
}

public int getFileSize(){
	return fileSize;
}

public TextIterator getTextIterator(){
	return new TextIterator(this);
}

}