package anchor;

public class Anchor{
private int shirina;
private int visota;

//Ãğàíèöû ïåğåìåùåíèÿ ÿêîğÿ
private int leftBound;
private int rightBound;
private int upBound;
private int downBound;

Anchor(int inShirina, int inVisota){
	//Óñòàíîâêà íà÷àëüíîé ïîçèöèè ÿêîğÿ
	shirina = inShirina;
	visota = inVisota;
}

//////////////////////////////ÓÑÒÀÍÎÂÊÀ ÃĞÀÍÈÖ ÏÅĞÅÌÅÙÅÍÈß ßÊÎĞß////////////////////
public void setLeftBound(int newBound){
	leftBound = newBound;
}

public void setRightBound(int newBound){
	rightBound = newBound;
}

public void setUpBound(int newBound){
	upBound = newBound;
}

public void setDownBound(int newBound){
	downBound = newBound;
}
/////////////////////////////////////////////////////////////////////////////////////
//////////////////////////ÂÎÇÂĞÀÒ_ÇÍÀ×ÅÍÈÉ_ÃĞÀÍÈÖ/////////////////////////////////////
public int getLeftBound(){
	return leftBound;
}

public int getRightBound(){
	return rightBound;
}

public int getUpBound(){
	return upBound;
}

public int getDownBound(){
	return downBound;
}
/////////////////////////////////////////////////////////////////////////////////////


/////////////////////ÍÅÏÎÑĞÅÄÑÒÂÅÍÍÛÉ ÄÎÑÒÓÏ Ê ÏÎÇÈÖÈ ßÊÎĞß/////////////////////////
public void setPositionVisota(int newVisota){
	visota = newVisota;
}

public void setPositionShirina(int newShirina){
	shirina = newShirina;
}


public int getPositionShirina(){
	//System.out.println("getPositionShirina()   shirina = "+shirina);
	return shirina;
}

public int getPositionVisota(){
	//System.out.println("getPositionVisota()   visota = "+visota);
	return visota;
}
///////////////////////////////////////////////////////////////////////////////////



//////////////////////////ÄÂÈÆÅÍÈÅ ßÊÎĞß///////////////////////////////////////////////
public boolean moveUp(int delta){
	if (visota-delta<upBound){
		return false;
	}
	visota -= delta;
	return true;
}

public boolean moveDown(int delta){
	//System.out.println("moveDown anchor delta ="+delta+" visota = "+visota);
	if (visota+delta>downBound){
		return false;
	}
	visota += delta;
	//System.out.println("moveDown anchor delta visota = "+visota);
	return true;
}

public boolean moveLeft(int delta){
	if (shirina-delta<leftBound){ 
		return false;
	}
	shirina -= delta;
	return true;
}

public boolean moveRight(int delta){
	if (shirina+delta>rightBound){ 
		return false;
	}
	shirina += delta;
	return true;
}

///////////////////////////////////////////////////////////////////////////////////////
///////////////////////ÂÛĞÀÂÍÈÂÀÍÈÅ_ÏÎ_ÑÒÎĞÎÍÀÌ////////////////////////////////////////

public void alignToLeftBound(){
	shirina = leftBound;
}

public void alignToRightBound(){
	shirina = rightBound;
}

public void alignToUpBound(){
	visota = upBound;
}

public void alignToDownBound(){
	visota = downBound;
}
///////////////////////////////////////////////////////////////////////////////////////

public Anchor getDublicate(){
	Anchor dublicate = new Anchor(shirina, visota);
	//Êîïèğóåì ãğàíèöû
	dublicate.setLeftBound(leftBound);
	dublicate.setRightBound(rightBound);
	dublicate.setUpBound(upBound);
	dublicate.setDownBound(downBound);
	return dublicate;
}

}