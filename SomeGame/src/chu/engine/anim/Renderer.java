package chu.engine.anim;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Renderer {
	
	private static Camera camera;
	
	static {
		camera = new Camera(null, 0, 0);
	}
	
	/***
	 * Draws the given subtexture at the given coordinates.
	 * @param t			Texture to be drawn
	 * @param tx0		First texture x coord
	 * @param ty0		First texture y coord
	 * @param tx1		Second texture x coord
	 * @param ty1		Second texture y coord
	 * @param x0		First render x coord
	 * @param y0		First render y coord
	 * @param x1		Second render x coord
	 * @param y1		Second render y coord
	 */
	public static void render(Texture t, float tx0, float ty0, float tx1, float ty1, 
			float x0, float y0, float x1, float y1, float depth) {
		Color.white.bind();
		t.bind();
		
	    // draw quad
	    glBegin(GL_QUADS);
	    	glTexCoord2f(tx0,ty0);
	        glVertex3f(x0,y0,depth);
	        glTexCoord2f(tx1,ty0);
	        glVertex3f(x1,y0,depth);
	        glTexCoord2f(tx1,ty1);
	        glVertex3f(x1,y1,depth);
	        glTexCoord2f(tx0,ty1);
	        glVertex3f(x0,y1,depth);
	    glEnd();
	}
	
	public static void renderTransformed(Texture t, float tx0, float ty0, float tx1, float ty1, 
			float x0, float y0, float x1, float y1, float depth, Transform transform) {
		Color.white.bind();
		t.bind();
		
		glPushMatrix();
		glTranslatef(x0, y0, depth);
		glScalef(transform.scaleX, transform.scaleY, 0);
		glTranslatef(-x0, -y0, -depth);
		glTranslatef((x0+x1)/2, (y0+y1)/2, depth);
		glRotatef(transform.rotation/(float)Math.PI*180, 0, 0, 1);
		glTranslatef(-(x0+x1)/2,-(y0+y1)/2, -depth);
		
		
	    // draw quad
	    glBegin(GL_QUADS);
	    	glTexCoord2f(tx0,ty0);
	        glVertex3f(x0,y0,depth);
	        glTexCoord2f(tx1,ty0);
	        glVertex3f(x1,y0,depth);
	        glTexCoord2f(tx1,ty1);
	        glVertex3f(x1,y1,depth);
	        glTexCoord2f(tx0,ty1);
	        glVertex3f(x0,y1,depth);
	    glEnd();
	    
	    glPopMatrix();
	    
	}
	
	public static void drawSquare(float x, float y, float s, float depth, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glColor4f(c.r, c.g, c.b, c.a);
		
		//glLoadIdentity();
		glBegin(GL_QUADS);
			glVertex3f(x, y, depth);
			glVertex3f(x+s, y, depth);
			glVertex3f(x+s, y+s, depth);
			glVertex3f(x, y+s, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void drawLine(float x0, float y0, float x, float y, 
			float width, float depth, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);
		glColor4f(c.r, c.g, c.b, c.a);
		
		//glLoadIdentity();
		glBegin(GL_LINES);
			glVertex3f(x0,y0,depth);
			glVertex3f(x,y,depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		
	}
	
	public static void drawTriangle(float x0, float y0, float x, float y, 
			float x2, float y2, float depth, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glColor4f(c.r, c.g, c.b, c.a);
		glBegin(GL_TRIANGLES);
			glVertex3f(x0,y0,depth);
			glVertex3f(x,y,depth);
			glVertex3f(x2,y2,depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		
	}
	
	public static void setCamera(Camera c) {
		camera = c;
	}

	public static Camera getCamera() {
		return camera;
	}
	

}
