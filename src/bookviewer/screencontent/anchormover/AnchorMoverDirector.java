package anchormover;
import anchor.Anchor;

public class AnchorMoverDirector{
private int stringVisota;

public AnchorMoverDirector(int inStringVisota){
	stringVisota=inStringVisota;
}

/////////////////////¬€—Œ“¿_–¿«ƒ≈À»“≈Àﬂ_—“–Œ »/////////////////////////
public void addStringVisota(){
	
	stringVisota++;
	System.out.println("addStringVisota() = "+stringVisota);
}

public int getStringVisota(){
	return stringVisota;
}

public void subStringVisota(){
	stringVisota--;
	if (stringVisota<1) stringVisota=1;
}

public AnchorMover createAnchorMover(Anchor inAnchor){
	AnchorMover newAnchorMover = new AnchorMover(inAnchor, stringVisota);
	return newAnchorMover;
}

}