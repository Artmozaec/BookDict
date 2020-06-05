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
	//создаем мувер
	AnchorMover anchorMover = anchorMoverDirector.createAnchorMover(anchor);
	int movedVisota=0;
	//System.out.println("перед циклом movedVisota = "+movedVisota +" needVisota = "+needVisota);
	while (movedVisota<needVisota){
		//смещаем якорь в право на ширину контента
		if (!moveAnchorRight(anchorMover, contentArea.getShirina())){//Если смещение не удалось
			//System.out.println("moveDovnInPixelsFoldingString - НЕ УДАЛОСЬ ПРОЙТИ ДАЛЬШЕ! movedVisota = "+movedVisota);
			//Значит мы уперлись в самый низ
			return movedVisota;
		}
		movedVisota += anchorMover.getCurrentStringVisota();
		//System.out.println("movedVisota = "+movedVisota +" needVisota = "+needVisota);
	}
	return movedVisota;
}

public int moveUpInPixelsFoldingString(Anchor anchor, int needVisota){
	//создаем мувер
	AnchorMover anchorMover = anchorMoverDirector.createAnchorMover(anchor);
	int movedVisota=0;
	while (movedVisota<needVisota){
		//смещаем якорь в лево на ширину контента
		if (!moveAnchorLeft(anchorMover, contentArea.getShirina())){//Если смещение не удалось
			//Значит мы уперлись в самый верх
			return movedVisota;
		}
		movedVisota += anchorMover.getCurrentStringVisota();
	}
	return movedVisota;
}

private boolean moveAnchorLeft(AnchorMover anchorMover, int delta){
	//Достаём якорь
	Anchor anchor = anchorMover.getAnchor();
	//Семщаем якорь на ширину дельта
	if (!anchor.moveLeft(delta)){
		//Если якорь уперся в левую границу
		//Вычисляем его новую позицию по горизонтали
		//int newPositionShirina =  anchor.getRightBound() - (delta - anchor.getPositionShirina());
		//Выход за границы
		int overrun = Math.abs(anchor.getLeftBound()-(anchor.getPositionShirina()-delta));
		
		//Новая позиция
		int newPositionShirina = anchor.getRightBound() - overrun;
		
		//Переводим строку
		if (!anchorMover.pervString()){
			//Если строку перевести не удалось, сигнализируем о неудаче
			return false;
		}
		
		//Принудительно устанавливаем вычисленную позицию по ширине
		anchor.setPositionShirina(newPositionShirina);
	}
	return true;
}

private boolean moveAnchorRight(AnchorMover anchorMover, int delta){
	//Достаём якорь
	Anchor anchor = anchorMover.getAnchor();
	//Семщаем якорь на ширину дельта
	if (!anchor.moveRight(delta)){
		//Если якорь уперся в правую границу
		//Вычисляем его новую позицию по горизонтали
		//Выход за границы
		int overrun =  (delta + anchor.getPositionShirina()) - anchor.getRightBound();
		//Новая позиция
		int newPositionShirina = anchor.getLeftBound()+overrun;
		//System.out.println("moveAnchorRight - строка вниз - delta = "+delta);
		//System.out.println("moveAnchorRight - строка вниз - anchor.getPositionShirina() = "+anchor.getPositionShirina());
		//System.out.println("moveAnchorRight - строка вниз - newPositionShirina = "+newPositionShirina);
		
		//Переводим строку
		if (!anchorMover.nextString()){
			//System.out.println("moveAnchorRight - ДАЛЬШЕ НЕТ!!!!! ");
			//Если строку перевести не удалось, сигнализируем о неудаче
			return false;
		}
		
		//Принудительно устанавливаем вычисленную позицию по ширине
		anchor.setPositionShirina(newPositionShirina);
	}
	return true;
}

}