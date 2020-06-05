package screencontent;

import bookpages.Page;
import javax.microedition.lcdui.Image;
import rgbimage.RGBImage;
import anchor.Anchor;
import userinterface.UserInterface;

public class FlatScreenContent extends AbstractScreenContent{
private Page page;
private Anchor anchor;
private ContentArea contentArea;

FlatScreenContent(Page inPage, Anchor inAnchor, ContentArea inContentArea){
	page = inPage;
	contentArea = inContentArea; 
	anchor = inAnchor;
	
	//��������� �� ����� �� �������
	if (isRightOverrun()) alignToRightBound();
	if (isDownOverrun()) alignToDownBound();
	
	//����������� ������������ �������� ����� �� ������� ��������
	setAnchorBoundsInPageSize();
}

private void setAnchorBoundsInPageSize(){
	anchor.setLeftBound(0);
	anchor.setRightBound(page.getShirina());
	anchor.setUpBound(0);
	anchor.setDownBound(page.getVisota());
}

/////////////////////////////////////////�������� ��������////////////////////////////////
//����������� ������� �������� �� ������ ���� ��������
public void alignToLeftBound(){
	anchor.setPositionShirina(0);
}

//����������� ������� �������� �� ������� ���� ��������
public void alignToRightBound(){
	anchor.setPositionShirina(page.getShirina()-contentArea.getShirina());
}

//����������� ������� �������� �� �������� ���� ��������
public void alignToUpBound(){
	anchor.setPositionVisota(0);
}

//����������� ������� �������� �� ������� ���� ��������
public void alignToDownBound(){
	anchor.setPositionVisota(page.getVisota()-contentArea.getVisota());
}
//////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////�������� �����///////////////////////////////////
private void moveAnchorUp(int delta){	
	//������� �����
	//���� ��� �������� ����� - ����� �� �������
	if (!anchor.moveUp(delta)){
		//������� ���� ������ �� ����� ��������
		alignToUpBound();
	}
}

private void moveAnchorDown(int delta){

	
	//������� ����
	anchor.moveDown(delta);
	
	//���� ��� �������� ���� �� ����� �� �������	
	if (anchor.getPositionVisota()+contentArea.getVisota() > page.getVisota()){
		//������� ��� ������ �� ���� ��������
		alignToDownBound();
	}
}

private void moveAnchorLeft(int delta){
	//������� � ����
	//���� ��� �������� ����� �� ����� �� �������	
	if (!anchor.moveLeft(delta)){
		alignToLeftBound();
	}
}

private void moveAnchorRight(int delta){
	//������� � ����
	anchor.moveRight(delta);
	
	//���� ��� �������� ����� �� ����� �� �������	
	if (anchor.getPositionShirina()+contentArea.getShirina() > page.getShirina()){
		alignToRightBound();
	}
}
/////////////////////////////////////////////////////////////////////////////////////////


///////////////////////////////////////�������� ��������////////////////////////////////////
public void screenDown(){
	//���� ����� �������� ������ �������, ������������ ������� �� �������� ������
	if (anchor.getPositionVisota()+contentArea.getVisota() == page.getVisota()){
		//System.out.println("screenDown() >>>>>>>Next page ");
		page.pageDown();
		setAnchorBoundsInPageSize();
		alignToUpBound();

		//���� ������ ������� ������ ������� ��������, ����� ������� ���!
		while ((contentArea.getShirina() > page.getShirina()) || (contentArea.getVisota() > page.getVisota())){
			//�����������
			UserInterface ui = UserInterface.getInstance();
			ui.showInfoMessage("���������� �������� ������");
			//������� � ���
			contentArea.zoomIn();
		}

		//���� ������� �������� ���������� � ������ ����� ��������� ��������������� ������� ������� ����������������
		if (anchor.getPositionShirina()+contentArea.getShirina() > page.getShirina()){
			alignToRightBound();
		}
		

		
		
		return;
	}
	
	int delta = contentArea.getVisota()-contentArea.getSmallMoveDelta();
	
	//���� �������� �� ���������� - ����������� ������� �� ������� ����
	moveAnchorDown(delta);
}


public void screenUp(){
	//���� ����� �������� ������� �������, ������������ ������� �� ���������� ��������
	if (anchor.getPositionVisota()==0){
		page.pageUp();
		setAnchorBoundsInPageSize();
		
		//���� ������ ������� ������ ������� ��������, ����� ������� ���!
		while ((contentArea.getShirina() > page.getShirina()) || (contentArea.getVisota() > page.getVisota())){
			//�����������
			UserInterface ui = UserInterface.getInstance();
			ui.showInfoMessage("��������� �������� ������");
			//������� � ���
			contentArea.zoomIn();
		}

		//���� ����� ����� �� ������� � �����
		if (anchor.getPositionShirina()+contentArea.getShirina() > page.getShirina()){
			alignToRightBound();
		}
		

		
		alignToDownBound();
		return;
	}
	
	int delta = contentArea.getVisota()-contentArea.getSmallMoveDelta();
	moveAnchorUp(delta);
}

public void screenRight(){
	int delta = contentArea.getShirina()-contentArea.getSmallMoveDelta();
	moveAnchorRight(delta);
}

public void screenLeft(){
	int delta = contentArea.getShirina()-contentArea.getSmallMoveDelta();
	moveAnchorLeft(delta);
}
//////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////����� �������� ��������////////////////////////////////////
public void smallUp(){
	moveAnchorUp(contentArea.getSmallMoveDelta());
}

public void smallDown(){
	moveAnchorDown(contentArea.getSmallMoveDelta());
}

public void smallLeft(){
	moveAnchorLeft(contentArea.getSmallMoveDelta());
}

public void smallRight(){
	moveAnchorRight(contentArea.getSmallMoveDelta());
}
////////////////////////////////////////////////////////////////////////////////////////

//����� RGB ������ ����������� ������
public RGBImage getContent(){
//���� ������ ������� ������ ������� ��������, ����� ������� ���!
	while ((contentArea.getShirina() > page.getShirina()) || (contentArea.getVisota() > page.getVisota())){
	//�����������
		UserInterface ui = UserInterface.getInstance();
		ui.showInfoMessage("��-��-��! ���� ������");
		//������� � ���
		contentArea.zoomIn();
	}
	RGBImage image = null;
	try{
	image=page.getImageArea(
		anchor.getPositionVisota(), 
		anchor.getPositionShirina(),
		contentArea.getVisota(),
		contentArea.getShirina()
	);
	} catch(OutOfMemoryError e){
			UserInterface ui = UserInterface.getInstance();
			ui.showErrorMessage("������, ��� ������");
			//������� � ���
			contentArea.zoomIn();
			
			image=page.getImageArea(
				anchor.getPositionVisota(), 
				anchor.getPositionShirina(),
				contentArea.getVisota(),
				contentArea.getShirina()
			);
	}
	return image;
}






//////////////////////////////////////////////������� �����///////////////////////////////////////////

//������ � �����
public boolean isRightOverrun(){
	//���� ����� ������� ������� ����� �� ������ ���� ������ ������ ������ ��� ������ ��������
	if ((anchor.getPositionShirina()+contentArea.getShirina())>page.getShirina()) return true;
	return false;
}

//������ � ����
public boolean isDownOverrun(){
	//���� ����� ������� ������� ����� �� ������ ���� ������ ������ ������ ��� ������ ��������
	if ((anchor.getPositionVisota()+contentArea.getVisota())>page.getVisota()) return true;
	return false;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////



}