package displaysize;

public class DisplaySizeDirector{
	private static DisplaySizeDirector displaySizeDirector = null;
	private static DisplaySize displaySize;
	
	private DisplaySizeDirector(){
		displaySize = new DisplaySize();
	}
	
	public static DisplaySizeDirector getInstance(){
		if (displaySizeDirector == null){
			displaySizeDirector = new DisplaySizeDirector();
		}
		return displaySizeDirector;
	}
	
	public int getScreenVisota(){
		return displaySize.getScreenVisota();
	}
	
	public int getScreenShirina(){
		return displaySize.getScreenShirina();
	}
}