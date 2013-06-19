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
			float x0, float y0, float x1, float y1) {
		Color.white.bind();
		t.bind();
		
	    // draw quad
	    glBegin(GL_QUADS);
	    	glTexCoord2f(tx0,ty0);
	        glVertex2f(x0,y0);
	        glTexCoord2f(tx1,ty0);
	        glVertex2f(x1,y0);
	        glTexCoord2f(tx1,ty1);
	        glVertex2f(x1,y1);
	        glTexCoord2f(tx0,ty1);
	        glVertex2f(x0,y1);
	    glEnd();
	}
	
	public static void renderTransformed(Texture t, float tx0, float ty0, float tx1, float ty1, 
			float x0, float y0, float x1, float y1, Transform transform) {
		Color.white.bind();
		t.bind();
		
		glPushMatrix();
		glTranslatef(x0, y0, 0);
		glScalef(transform.scaleX, transform.scaleY, 0);
		glTranslatef(-x0, -y0, 0);
		glTranslatef((x0+x1)/2, (y0+y1)/2, 0);
		glRotatef(transform.rotation, 0, 0, 1);
		glTranslatef(-(x0+x1)/2,-(y0+y1)/2, 0);
		
		
	    // draw quad
	    glBegin(GL_QUADS);
	    	glTexCoord2f(tx0,ty0);
	        glVertex2f(x0,y0);
	        glTexCoord2f(tx1,ty0);
	        glVertex2f(x1,y0);
	        glTexCoord2f(tx1,ty1);
	        glVertex2f(x1,y1);
	        glTexCoord2f(tx0,ty1);
	        glVertex2f(x0,y1);
	    glEnd();
	    
	    glPopMatrix();
	    
	}
	
	public static void drawSquare(float x, float y, float s, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glColor4f(c.r, c.g, c.b, c.a);
		
		//glLoadIdentity();
		glBegin(GL_QUADS);
			glVertex2f(x, y);
			glVertex2f(x+s, y);
			glVertex2f(x+s, y+s);
			glVertex2f(x, y+s);
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void drawLine(float x0, float y0, float x, float y, float width, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);
		glColor4f(c.r, c.g, c.b, c.a);
		
		//glLoadIdentity();
		glBegin(GL_LINES);
			glVertex2f(x0,y0);
			glVertex2f(x,y);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		
	}
	
	public static void drawTriangle(float x0, float y0, float x, float y, float x2, float y2, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glColor4f(c.r, c.g, c.b, c.a);
		glBegin(GL_TRIANGLES);
			glVertex2f(x0,y0);
			glVertex2f(x,y);
			glVertex2f(x2,y2);
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
