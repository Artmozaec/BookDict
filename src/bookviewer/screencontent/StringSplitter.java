package screencontent;

import javax.microedition.lcdui.*;
import rgbimage.RGBImage;
import anchormover.AnchorMover;
import bookpages.Page;
import anchor.Anchor;

class StringSplitter{


//”казатели области

private Anchor upAnchor;
private AnchorMover anchorMover;
private Page page;

StringSplitter(AnchorMover inAnchorMover, Page inPage){
	anchorMover = inAnchorMover;
	page = inPage;
	//System.out.println("¬ысота страницы = "+page.getVisota()+" ширина = "+page.getShirina()+"путь = "+page.getBookPatch());
	//—оздаЄм рассто€ние строки между верхним и нижним €корем
	//¬ерхний €корь дубликат основного перемещаемого - нижнего
	upAnchor = anchorMover.getAnchor().getDublicate();
	anchorMover.nextString();
}

//возвращает массив куска изображени€ длинной в текущее смещение высотой в строку
public RGBImage getString(){
	//Ќижний €корь
	Anchor downAnchor = anchorMover.getAnchor();
	//System.out.println("upAnchor = "+upAnchor);
	//System.out.println("downAnchor = "+downAnchor);
	//System.out.println("upAnchor.getLeftBound() = "+upAnchor.getLeftBound());
	//System.out.println("upAnchor.getPositionShirina() = "+upAnchor.getPositionShirina());
	if ((downAnchor.getPositionVisota()-upAnchor.getPositionVisota() == 0) || 
		(upAnchor.getRightBound() - upAnchor.getPositionShirina() == 0)){//якорь у самого правого кра€! Ѕудет создан 0-ой по ширине RGBImage!
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
	//ѕрижимаем €корь к левому краю
	anchorMover.getAnchor().alignToLeftBound();
	
	//«апоминаем предыдущее положение €кор€
	upAnchor = anchorMover.getAnchor().getDublicate();
	return anchorMover.nextString();
}

}