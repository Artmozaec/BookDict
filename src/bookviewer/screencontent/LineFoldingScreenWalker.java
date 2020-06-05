package screencontent;

import anchormover.AnchorMoverDirector;
import anchormover.AnchorMover;
import anchor.Anchor;

class LineFoldingScreenWalker{
private AnchorMoverDirector anchorMoverDirector;
private ContentArea contentArea;

LineFoldingScreenWalker(AnchorMoverDirector inAnchorMoverDirector, ContentArea inContentArea){
	anchorMoverDirector = inAnchorMoverDirector;
	contentArea = inContentArea;
}


public int moveDovnInPixelsFoldingString(Anchor anchor, int needVisota){
	//������� �����
	AnchorMover anchorMover = anchorMoverDirector.createAnchorMover(anchor);
	int movedVisota=0;
	//System.out.println("����� ������ movedVisota = "+movedVisota +" needVisota = "+needVisota);
	while (movedVisota<needVisota){
		//������� ����� � ����� �� ������ ��������
		if (!moveAnchorRight(anchorMover, contentArea.getShirina())){//���� �������� �� �������
			//System.out.println("moveDovnInPixelsFoldingString - �� ������� ������ ������! movedVisota = "+movedVisota);
			//������ �� �������� � ����� ���
			return movedVisota;
		}
		movedVisota += anchorMover.getCurrentStringVisota();
		//System.out.println("movedVisota = "+movedVisota +" needVisota = "+needVisota);
	}
	return movedVisota;
}

public int moveUpInPixelsFoldingString(Anchor anchor, int needVisota){
	//������� �����
	AnchorMover anchorMover = anchorMoverDirector.createAnchorMover(anchor);
	int movedVisota=0;
	while (movedVisota<needVisota){
		//������� ����� � ���� �� ������ ��������
		if (!moveAnchorLeft(anchorMover, contentArea.getShirina())){//���� �������� �� �������
			//������ �� �������� � ����� ����
			return movedVisota;
		}
		movedVisota += anchorMover.getCurrentStringVisota();
	}
	return movedVisota;
}

private boolean moveAnchorLeft(AnchorMover anchorMover, int delta){
	//������ �����
	Anchor anchor = anchorMover.getAnchor();
	//������� ����� �� ������ ������
	if (!anchor.moveLeft(delta)){
		//���� ����� ������ � ����� �������
		//��������� ��� ����� ������� �� �����������
		//int newPositionShirina =  anchor.getRightBound() - (delta - anchor.getPositionShirina());
		//����� �� �������
		int overrun = Math.abs(anchor.getLeftBound()-(anchor.getPositionShirina()-delta));
		
		//����� �������
		int newPositionShirina = anchor.getRightBound() - overrun;
		
		//��������� ������
		if (!anchorMover.pervString()){
			//���� ������ ��������� �� �������, ������������� � �������
			return false;
		}
		
		//������������� ������������� ����������� ������� �� ������
		anchor.setPositionShirina(newPositionShirina);
	}
	return true;
}

private boolean moveAnchorRight(AnchorMover anchorMover, int delta){
	//������ �����
	Anchor anchor = anchorMover.getAnchor();
	//������� ����� �� ������ ������
	if (!anchor.moveRight(delta)){
		//���� ����� ������ � ������ �������
		//��������� ��� ����� ������� �� �����������
		//����� �� �������
		int overrun =  (delta + anchor.getPositionShirina()) - anchor.getRightBound();
		//����� �������
		int newPositionShirina = anchor.getLeftBound()+overrun;
		//System.out.println("moveAnchorRight - ������ ���� - delta = "+delta);
		//System.out.println("moveAnchorRight - ������ ���� - anchor.getPositionShirina() = "+anchor.getPositionShirina());
		//System.out.println("moveAnchorRight - ������ ���� - newPositionShirina = "+newPositionShirina);
		
		//��������� ������
		if (!anchorMover.nextString()){
			//System.out.println("moveAnchorRight - ������ ���!!!!! ");
			//���� ������ ��������� �� �������, ������������� � �������
			return false;
		}
		
		//������������� ������������� ����������� ������� �� ������
		anchor.setPositionShirina(newPositionShirina);
	}
	return true;
}

}