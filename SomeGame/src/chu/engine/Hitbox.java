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

public abstract class Hitbox {
	
	protected Entity parent;
	protected int offsetX;
	protected int offsetY;
	
	public Hitbox(Entity p, int x, int y) {
		parent = p;
		offsetX = x;
		offsetY = y;
	}
	
	/**
	 * Does a point collision with the hitbox at the parent's location (plus offset)
	 * @param x: x coordinate to check
	 * @param y: y coordinate to check
	 * @return
	 */
	public abstract boolean pointCollision(int x, int y);
	
	/**
	 * Does a point collision with the hitbox at the given position (plus offset)
	 * @param x
	 * @param y
	 * @param x0: x coord to place the hitbox
	 * @param y0: y coord to place the hitbox
	 * @return
	 */
	public abstract boolean pointCollision(int x, int y, int x0, int y0);
	//self explanatory hopefully
	public abstract boolean lineCollision(int a, int b, int c, int d);
	public abstract boolean lineCollision(int a, int b, int c, int d, int x, int y);
	
	
	
}
