package chu.engine;

public class RectangleHitbox extends Hitbox {
	
	private int width;
	private int height;

	public RectangleHitbox(int x, int y, int w, int h) {
		super(x, y);
		width = w;
		height = h;
	}

	@Override
	public boolean pointCollision(int x, int y) {
		return pointCollision(x, y, parent.x, parent.y);
	}

	@Override
	public boolean pointCollision(int x, int y, int x0, int y0) {
		if(x0+offsetX > x
				|| x > x0+offsetX+width
				|| y0+offsetY > y
				|| y > y0+offsetY+height) {
				return false;
		}
		return true;
	}

	@Override
	public boolean entityCollision(Entity other) {
		// TODO Auto-generated method stub
		return false;
	}

}
