package screencontent;

import javax.microedition.lcdui.*;
import rgbimage.RGBImage;
import anchormover.AnchorMover;
import bookpages.Page;
import anchor.Anchor;

class StringSplitter{


//��������� �������

private Anchor upAnchor;
private AnchorMover anchorMover;
private Page page;

StringSplitter(AnchorMover inAnchorMover, Page inPage){
	anchorMover = inAnchorMover;
	page = inPage;
	//System.out.println("������ �������� = "+page.getVisota()+" ������ = "+page.getShirina()+"���� = "+page.getBookPatch());
	//������ ���������� ������ ����� ������� � ������ ������
	//������� ����� �������� ��������� ������������� - �������
	upAnchor = anchorMover.getAnchor().getDublicate();
	anchorMover.nextString();
}

//���������� ������ ����� ����������� ������� � ������� �������� ������� � ������
public RGBImage getString(){
	//������ �����
	Anchor downAnchor = anchorMover.getAnchor();
	//System.out.println("upAnchor = "+upAnchor);
	//System.out.println("downAnchor = "+downAnchor);
	//System.out.println("upAnchor.getLeftBound() = "+upAnchor.getLeftBound());
	//System.out.println("upAnchor.getPositionShirina() = "+upAnchor.getPositionShirina());
	if ((downAnchor.getPositionVisota()-upAnchor.getPositionVisota() == 0) || 
		(upAnchor.getRightBound() - upAnchor.getPositionShirina() == 0)){//����� � ������ ������� ����! ����� ������ 0-�� �� ������ RGBImage!
		return new RGBImage(1,1);
	}
	RGBImage string = page.getImageArea(
			upAnchor.getPositionVisota(), 
			upAnchor.getPositionShirina(),
			downAnchor.getPositionVisota()-upAnchor.getPositionVisota(), 
			upAnchor.getRightBound() - upAnchor.getPositionShirina()
	);

	return string;
}

public boolean nextString(){
	//��������� ����� � ������ ����
	anchorMover.getAnchor().alignToLeftBound();
	
	//���������� ���������� ��������� �����
	upAnchor = anchorMover.getAnchor().getDublicate();
	return anchorMover.nextString();
}

}