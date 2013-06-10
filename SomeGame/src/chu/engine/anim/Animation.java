package chu.engine.anim;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;

public class Animation {
	private Texture texture;
	private int width;
	private int height;
	private int length;
	private int currentFrame;
	private int counter;
	private int speed;			//Time for each frame in milliseconds
	
	public Animation(Texture t) {
		texture = t;
		width = t.getImageWidth();
		height = t.getImageHeight();
		length = 1;
		speed = 99999;
	}
	
	public Animation(Texture t, int width, int height, int frames, int speed) {
		this(t);
		this.width = width;
		this.height = height;
		this.length = frames;
		this.speed = speed;
	}
	
	public int getLength() { return length; }
	public int getFrame() { return currentFrame; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getImageWidth() { return texture.getImageWidth(); }
	public Texture getTexture() { return texture; }
	
	
	public void update() {
		counter += Game.getDelta();
		if(counter/1000000 > speed) {
			increment();
			counter = 0;
		}
	}
	
	public void increment() {
		currentFrame += 1;
		if(currentFrame >= length) done();
	}
	
	public void done() {
		currentFrame = 0;
	}

	public void setFrame(int i) {
		this.currentFrame = i;
	}
	

}
