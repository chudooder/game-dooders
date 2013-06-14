package chu.engine;

/**
 * Abstract class that determines how an object deals with collisions.
 * Entities without defined hitboxes cannot collide with other objects.
 * Entities with defined hitboxes will consult their hitbox for collision
 * detection. Child classes of Hitbox define how the entity checks for
 * collision.
 * @author Shawn
 *
 */

public class Hitbox {
	
	protected Entity parent;
	protected int offsetX;
	protected int offsetY;
	
	public Hitbox(Entity p, int x, int y) {
		parent = p;
		offsetX = x;
		offsetY = y;
	}
	
	public int getX() {
		return parent.x + offsetX;
	}
	
	public int getY() {
		return parent.y + offsetY;
	}
	
	public static boolean collisionExists(Entity e, Entity f) {
		Hitbox a = e.hitbox;
		Hitbox b = f.hitbox;
		if(a instanceof RectangleHitbox) {
			RectangleHitbox r1 = (RectangleHitbox)a;
			if(b instanceof RectangleHitbox) {
				RectangleHitbox r2 = (RectangleHitbox)b;
				return checkCollision(r1, r2);
			} else if(b instanceof LineHitbox) {
				LineHitbox l2 = (LineHitbox)b;
				return checkCollision(r1, l2);
			}
		} else if(a instanceof LineHitbox) {
			LineHitbox l1 = (LineHitbox)a;
			if(b instanceof RectangleHitbox) {
				RectangleHitbox r2 = (RectangleHitbox)b;
				return checkCollision(r2, l1);
			} else if(b instanceof LineHitbox) {
				LineHitbox l2 = (LineHitbox)b;
				return checkCollision(l1, l2);
			}
		}
		
		return false;
	}
	
	

	//@Override
	private static boolean checkCollision(RectangleHitbox rect, LineHitbox line) {
		int x = rect.getX();
		int y = rect.getY();
		int width = rect.getWidth();
		int height = rect.getHeight();
		
		int[][] corners = {
			{x, y},
			{x+width, y},
			{x+width, y+height},
			{x, y+height}
		};
		
		int x1 = line.getX();
		int x2 = line.getEndX();
		int y1 = line.getY();
		int y2 = line.getEndY();
		
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
	
	private static boolean checkCollision(RectangleHitbox a, RectangleHitbox b) {
		if(a.getX() > b.getX() + b.getWidth()) return false;
		if(a.getX() + a.getWidth() < b.getX()) return false;
		if(a.getY() > b.getY() + b.getHeight()) return false;
		if(a.getY() + a.getHeight() < b.getY()) return false;
		
		return true;
	}
	
	private static boolean checkCollision(LineHitbox a, LineHitbox b) {
		int first, second;
		int aX = a.getX();
		int aY = a.getY();
		int bX = a.getEndX();
		int bY = a.getEndY();
		int cX = b.getX();
		int cY = b.getY();
		int dX = b.getEndX();
		int dY = b.getEndY();
		
		first = aX*(bY-cY) + bX*(cY-aY) + cX*(aY-bY);
		second = aX*(bY-dY) + bX*(dY-aY) + dX*(aY-bY);
		
		if(first < 0 && second < 0) return false;
		if(first > 0 && second > 0) return false;
		return true;
		
	}

}
