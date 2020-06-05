package screencontent;
import rgbimage.RGBImage;

abstract public class AbstractScreenContent{
/*
protected Anchor anchor;
protected ContentArea contentArea;


AbstractScreenContent(Anchor inAnchor, ContentArea inContentArea){
	anchor = inAnchor;
	contentArea = inContentArea;
}
*/

abstract public RGBImage getContent();

abstract public void screenUp();
abstract public void screenDown();
abstract public void screenLeft();
abstract public void screenRight();

abstract public void smallUp();
abstract public void smallDown();
abstract public void smallLeft();
abstract public void smallRight();

abstract public void alignToUpBound();
abstract public void alignToDownBound();
abstract public void alignToLeftBound();
abstract public void alignToRightBound();

abstract public boolean isRightOverrun();
abstract public boolean isDownOverrun();
}