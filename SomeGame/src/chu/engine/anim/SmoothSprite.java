package chu.engine.anim;

import org.newdawn.slick.opengl.Texture;

public class SmoothSprite extends Sprite {
	/**
	 * Sprite that moves in a smooth path to the render destination.
	 */
	private double renderx = -1;
	private double rendery = -1;
	private double speed;
	
	public SmoothSprite(double s) {
		super();
		renderx = -1;
		rendery = -1;
		speed = s;
	}
	
	public SmoothSprite(double s, int rx, int ry) {
		this(s);
		renderx = rx;
		rendery = ry;
	}
	
	public void render(int x, int y) {
		if(renderx == -1) renderx = x;
		if(rendery == -1) rendery = y;
		
		if(renderx != x) {
			//double dx = (x - renderx)/(speed*Rougelike.getDelta()/1000000.0);
			double dx = (x - renderx)/(speed);
			renderx += dx;
		}
		
		if(rendery != y) {
			//double dy = (y- rendery)/(speed*Rougelike.getDelta()/1000000.0);
			double dy = (y- rendery)/(speed);
			rendery += dy;
		}
		
		if(currentAnimation == null) return;
		
		int width = currentAnimation.getWidth();
		int height = currentAnimation.getHeight();
		int fakelength = currentAnimation.getImageWidth()/width;
		float x0 = (float)(currentAnimation.getFrame())/(float)(fakelength);
		float x1 = (float)(currentAnimation.getFrame()+1)/(float)(fakelength);
		Texture texture = currentAnimation.getTexture();
		
		int rx = (int)Math.round(renderx);
		int ry = (int)Math.round(rendery);
		
		Renderer.render(texture, x0, 0, x1, 1, rx, ry, rx+width, ry+height);
	}
	
	public void renderRotated(int x, int y, float angle) {
		if(renderx == -1) renderx = x;
		if(rendery == -1) rendery = y;
		
		if(renderx != x) {
			double dx = (x - renderx)/speed;
			renderx += dx;
		}
		
		if(rendery != y) {
			double dy = (y- rendery)/speed;
			rendery += dy;
		}
		
		int rx = (int)Math.round(renderx);
		int ry = (int)Math.round(rendery);
		
		super.renderRotated(rx, ry, angle);
	}
}
