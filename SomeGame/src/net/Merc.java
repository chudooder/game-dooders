package net;

import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Collideable;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Hitbox;
import chu.engine.LineHitbox;
import chu.engine.RectangleHitbox;
import chu.engine.Stage;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

public class Merc extends Entity implements Collideable {
	
	//Something
	
	public static Texture texture;
	private float angle;
	private MercRecord record;
	private boolean recording;
	public int centerX;
	public int centerY;
	
	static {
		try {
			texture = TextureLoader.getTexture("PNG", 
						ResourceLoader.getResourceAsStream("res/guy.png"));
		} catch(IOException e) {
			System.err.println("Resource not found: guy.png");
		}
	}

	public Merc(TimeLapseStage s, int x, int y) {
		super(s, x, y);
		sprite.addAnimation("LOOP", texture);
		record = new MercRecord(this);
		hitbox = new RectangleHitbox(this, 4, 4, 24, 24);
		renderPriority = Entity.RENDER_PRIORITY_PLAYER;
	}

	@Override
	public void beginStep() {
		
		int mX = TimeLapse.getMouseX() - x - 16;
		int mY = TimeLapse.getMouseY() - y - 16;
		angle = (float) Math.toDegrees(Math.atan2(mY, mX));
		if(mY < 0) angle += 360;
		
		
		int dy = 0;
		int dx = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) dy = -2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) dy = 2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) dx = -2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) dx = 2;
		
		if(!stage.checkCollision(this, Wall.class, dx, 0)) {
			x += dx;
		}
		
		if(!stage.checkCollision(this, Wall.class, 0, dy)) {
			y += dy;
		}

		
		HashMap<Integer, Boolean> keys = Game.getKeys();
		for(int key : keys.keySet()) {
			if(key == Keyboard.KEY_F1 && keys.get(key)) { 
				recording = true;
				System.out.println("Started recording.");
			}
			
			if(key == Keyboard.KEY_F2 && keys.get(key)) {
				recording = false;
				System.out.println("Stopped recording.");
			}
			
			if(key == Keyboard.KEY_F3 && keys.get(key)) {
				record.playback();
				System.out.println("Playing back record of length "+record.length()+" frames.");
			}
			
			if(key == Keyboard.KEY_F4 && keys.get(key)) { 
				record.clear();
				System.out.println("Record cleared.");
			}
		}
		
		centerX = x + 16;
		centerY = y + 16;
	}
	
	@Override
	public void endStep() {
		//Recording movement
		if(recording) {
			record.addFrame(x, y, angle);
		}
	}
	
	@Override
	public void render() {
		sprite.renderRotated(x, y, angle);
		renderLightMap();
	}

	@Override
	public void doCollisionWith(Entity e) {
		if(e instanceof Wall) {
			int left = Math.abs(e.x - (x+32));
			int right = Math.abs(e.x + 32 - x);
			int up = Math.abs(e.y - (y+32));
			int down = Math.abs(e.y + 32 - y);
			
			int min = Math.min(Math.min(left, right), Math.min(up, down));
			if(min == left) {
				x = e.x - 32;
			} else if(min == right) {
				x = e.x + 32;
			} else if(min == up) {
				y = e.y - 32;
			} else if(min == down) {
				y = e.y + 32;
			}
		}
	}

	public float getAngle() {
		return angle;
	}

	private double raytrace(ArrayList<Wall> walls, Wall ignore, double rayAngle) {
		double rads = rayAngle/180.0*Math.PI;
		
		LineHitbox line = new LineHitbox(this, 16, 16, 
				(int)(600*Math.cos(rads)), 
				(int)(600*Math.sin(rads)));
		
		double best = 600;
		for(Wall w : walls) {
			/* Do a sanity check first before we do some heavy computing.
			 * Basically, this compares the rayAngle between the merc and the wall
			 * (both the beginning and end points)
			 * with the rayAngle the merc is facing. If it's past a certain threshold,
			 * skip computing the intersection because it can't exist.
			 */
			
			if(w.equals(ignore)) continue;
			float check1 = (float) Math.toDegrees(Math.atan2(w.y - centerY, w.x - centerX));
			float check2 = (float) Math.toDegrees(
					Math.atan2(w.y+w.getHeight() - centerY, w.x+w.getWidth() - centerX));
			double abs = Math.min(Math.abs(rayAngle - check1), Math.abs(rayAngle - check2));
			if(abs > 120 && abs < 240) continue;
			
			//Now get the distance
			float dist = (float) (600 * Hitbox.getIntersection
					((RectangleHitbox)w.hitbox, line, 0, 0));
			if(dist < best && dist > 0) best = dist;
		}
		
		float pointX = centerX + (float)(best*Math.cos(rads));
		float pointY = centerY + (float)(best*Math.sin(rads));
		Renderer.drawLine(centerX, centerY, pointX, pointY, 1, Color.gray);
		return best;
	}
	
	private void renderLightMap() {
		ArrayList<Vertex> vertices = new ArrayList<>();
		ArrayList<Wall> walls = new ArrayList<>();
		ArrayList<Angle> angles = new ArrayList<>();
		
		for(Entity e : stage.getAllEntities()) {
			if(e instanceof Wall) {
				Wall w = (Wall)e;
				walls.add(w);
			}
		}
		
		//Get a list of angles to raytrace to, then raytrace and get the actual points
		for(Wall wall : walls) {
			for(int i = wall.x; i <= wall.getEndX(); i+=wall.getWidth()) {
				for(int j = wall.y; j <= wall.getEndY(); j+=wall.getHeight()) {
					double r = Math.toDegrees(Math.atan2(j - y - 16, i - x - 16));
					boolean ignore = false;
					if(i==wall.x && j==wall.y || i==wall.getEndX() && j==wall.getEndY()) {
						if(centerX<i && centerY>j || centerX>i && centerY<j) ignore = true;
					} else {
						if(centerX>i && centerY>j || centerX<i && centerY<j) ignore = true;
					}
					angles.add(new Angle(r, wall, ignore, i, j));
				}
			}
		}
		Collections.sort(angles);
		
		for(Angle a : angles) {
			double r = a.angle;
			boolean ignore = a.ignore;
			Wall wall = a.parent;
			int i = a.i;
			int j = a.j;
			
			double rads = r/180.0*Math.PI;
			if(ignore) {
				double dist = raytrace(walls, wall, r);
				Vertex v = new Vertex(
						(int)(centerX + dist*Math.cos(rads)), 
						(int)(centerY + dist*Math.sin(rads)));

				//Compare it to the previous vertex
				boolean sameWall = false;
				if(vertices.size() > 0) {
					Vertex prev = vertices.get(vertices.size()-1);
					if(prev.x == v.x || prev.y == v.y) {
						sameWall = true;
					}
				}
				
				vertices.add(v);
				Renderer.drawSquare(v.x-2, v.y-2, 4, Color.red);
				
				Vertex vv = new Vertex(i,j);
				double realDist = Math.sqrt(Math.pow(vv.x - centerX, 2)+Math.pow(vv.y - centerY, 2));
				if(realDist < dist) {
					if(sameWall) {
						vertices.add(vv);
					} else {
						vertices.add(vertices.size()-1, vv);
					}
					Renderer.drawSquare(vv.x-2, vv.y-2, 4, Color.blue);
				}
				
			} else {
				double dist = raytrace(walls, null, r);
				Vertex v = new Vertex(
						(int)(centerX + dist*Math.cos(rads)), 
						(int)(centerY + dist*Math.sin(rads)));
				vertices.add(v);
				Renderer.drawSquare(v.x-2, v.y-2, 4, Color.green);
			}
		}
		
		if(vertices.size() > 0) {
			Renderer.drawLine(vertices.get(0).x, vertices.get(0).y,
					centerX, centerY, 1, Color.white);
			vertices.add(vertices.get(0));
		}

		for(int i=0; i<vertices.size()-1; i++) {
			
//			Renderer.drawTriangle(centerX, centerY, vertices.get(i).x, vertices.get(i).y, 
//					vertices.get(i+1).x, vertices.get(i+1).y, new Color(255, 255, 255, 50));
			

			
//			Renderer.drawLine(vertices.get(i).x, vertices.get(i).y,
//					vertices.get(i+1).x, vertices.get(i+1).y, 1, Color.white);
		}
	}
	
	class Vertex {
		int x;
		int y;
		public Vertex(int d, int e) {
			this.x = d;
			this.y = e;
		}
	}
	
	class Angle implements Comparable<Angle> {
		double angle;
		Wall parent;
		boolean ignore;
		int i;
		int j;
		public Angle(double a, Wall w, boolean b, int i, int j) {
			angle = a;
			parent = w;
			ignore = b;
			this.i = i;
			this.j = j;
		}
		@Override
		public int compareTo(Angle arg0) {
			return (int)(angle - arg0.angle);
		}
	}

}
