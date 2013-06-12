package chu.engine;

public class RectangleHitbox extends Hitbox {
	
	private int width;
	private int height;

	public RectangleHitbox(Entity p, int x, int y, int w, int h) {
		super(p, x, y);
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
	public boolean lineCollision(int x1, int y1, int x2, int y2) {
		return lineCollision(x1, y1, x2, y2, parent.x, parent.y);
	}

	@Override
	public boolean lineCollision(int x1, int y1, int x2, int y2, int x0, int y0) {
		int x = x0 + offsetX;
		int y = y0 + offsetY;
		
		int[][] corners = {
			{x, y},
			{x+width, y},
			{x+width, y+height},
			{x, y+height}
		};
		
		int prev = 0;
		boolean done = true;
		for(int i=0; i<4; i++) {
			//It's math magic.
			int func = (y2-y1)*corners[i][0] + (x1-x2)*corners[i][1] + (x2*y1-x1*y2);
			if(func * prev < 0 || func == 0) {
				done = false;
			}
			prev = func;
		}
		
		if(done == true) {
			return false;
		}
		
		//There MIGHT be an intersection at this point. Keep checking.
		if(x1 > x+width && x2 > x+width) return false;
		if(x1 < x && x2 < x) return false;
		if(y1 < y && y2 < y) return false;
		if(y1 > y+height && y2 > y+height) return false;
		return true;
		
	}
	
}
