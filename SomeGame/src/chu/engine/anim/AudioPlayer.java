package chu.engine.anim;

import org.newdawn.slick.openal.Audio;

public class AudioPlayer {

	static Camera camera;

	public static void setCamera(Camera c) {
		camera = c;
	}

	public static void playAudio(Audio audio, float pitch, float gain, float x,
			float y, float z, float fade) {
		audio.playAsSoundEffect(pitch, gain, false, (x - camera.getX()) / fade,
				(y - camera.getY()) / fade, z);
	}
}
