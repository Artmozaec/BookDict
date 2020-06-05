package programdirection;

import com.nokia.mid.ui.DeviceControl;
public class Light extends Thread{

private static int volume = 100;

public Light(int newVolume){
	volume = newVolume;
	this.start();
}

public void run(){
	DeviceControl.setLights(0, volume);
}

public static int getLight(){
	return volume;
}

}