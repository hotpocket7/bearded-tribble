package game.sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip;
	
	public Sound(String path) {
		try {
			clip = Applet.newAudioClip(getClass().getResource(path));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
