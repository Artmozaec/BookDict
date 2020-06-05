package anchormover;
import anchor.Anchor;

public class AnchorMover{
private Anchor anchor;
private int stringVisota;

AnchorMover(Anchor inAnchor, int inStringVisota){
	stringVisota = inStringVisota;
	anchor = inAnchor;
}

public boolean nextString(){
	return anchor.moveDown(stringVisota);
}


public boolean pervString(){
	return anchor.moveUp(stringVisota);
}

public Anchor getAnchor(){
	return anchor;
}

public int getCurrentStringVisota(){
	return stringVisota;
}

}