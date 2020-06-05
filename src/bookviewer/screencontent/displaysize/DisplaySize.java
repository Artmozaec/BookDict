package displaysize;
import javax.microedition.lcdui.*;

public class DisplaySize extends Canvas{

private int screenVisota;
private int screenShirina;


public DisplaySize(){
	setFullScreenMode(true);
	screenVisota = getHeight();
	screenShirina = getWidth();
}

public void paint(Graphics g){
}


public int getScreenVisota(){
	return screenVisota;
}

public int getScreenShirina(){
	return screenShirina;
}

}