package chu.engine;

public class RectangleHitbox extends Hitbox {
	
	private int width;
	private int height;

	public RectangleHitbox(Entity p, int x, int y, int w, int h) {
		super(p, x, y);
		setWidth(w);
		height = h;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
}
