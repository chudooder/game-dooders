package chu.engine;

public class LineHitbox extends Hitbox {

	protected int dx;
	protected int dy;
	
	public LineHitbox(Entity p, int offsetX, int offsetY, int dx, int dy) {
		super(p, offsetX, offsetY);
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getEndX() {
		return parent.x + offsetX + dx;
	}
	
	public int getEndY() {
		return parent.y + offsetY + dy;
	}

}
