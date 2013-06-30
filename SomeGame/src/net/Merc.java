package net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.lwjgl.input.Keyboard;
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
import chu.engine.anim.Renderer;

public class Merc extends Entity implements Collideable {

	// Something

	public static Texture texture;
	private float angle;		//IN RADIANS.
	private Random random;
	private Weapon weapon;
	private Controller controller;
	public int centerX;
	public int centerY;
	private int frame;
	private int health;
	public Team team;

	static {
		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/guy.png"));
		} catch (IOException e) {
			System.err.println("Resource not found: guy.png");
		}
	}
	
	public Merc(TimeLapseStage s, int x, int y, Controller c, Team t) {
		super(s, x, y);
		sprite.addAnimation("LOOP", texture, 32, 32, 2, 1000);
		hitbox = new RectangleHitbox(this, 5, 5, 22, 22);
		renderPriority = Entity.RENDER_PRIORITY_PLAYER;
		//Is there a way to get the seed?
		random = new Random();
		if(c.getSeed() == 0) {
			long seed = Double.doubleToLongBits(Math.random());
			random.setSeed(seed);
			c.setSeed(seed);
		} else {
			random.setSeed(c.getSeed());
		}
		weapon = new Pistol(this);
		controller = c;
		c.set(this);
		health = 100;
		team = t;
		if(controller instanceof NetworkController) {
			stage.addEntity(new AmmoHUD(stage, 0, 0, this));
		}
	}
	
	public Merc(TimeLapseStage s, int x, int y, Controller c) {
		this(s, x, y, c, Team.BLUE);
	}
	
	public Merc(TimeLapseStage s, int x, int y) {
		this(s, x, y, new NetworkController());
	}

	@Override
	public void beginStep() {
		weapon.update();
		
		frame++;
		Map<Input, Object> inputs = controller.getInput(frame);
		if (inputs == null) {
			destroy();
			return;
		}
		if (inputs.get(Input.MOUSE) != null) {
			int[] loc = (int[]) inputs.get(Input.MOUSE);
			angle = (float) Math.atan2(loc[1], loc[0]);
		}

		int dy = 0;
		int dx = 0;
		if ((Boolean) inputs.get(Input.UP))
			dy = -2;
		if ((Boolean) inputs.get(Input.DOWN))
			dy = 2;
		if ((Boolean) inputs.get(Input.LEFT))
			dx = -2;
		if ((Boolean) inputs.get(Input.RIGHT))
			dx = 2;
		if ((Boolean) inputs.get(Input.RELOAD))
			weapon.reload();

		if (!stage.checkCollision(this, Wall.class, dx, 0)) {
			x += dx;
		}

		if (!stage.checkCollision(this, Wall.class, 0, dy)) {
			y += dy;
		}

		HashMap<Integer, Boolean> keys = Game.getKeys();
		for (int key : keys.keySet()) {
			if (key == Keyboard.KEY_F3 && keys.get(key)) {
				stage.addEntity(new Merc(stage,320,240,controller.getRecord(),Team.RED));
			}
			
			if (key == Keyboard.KEY_F4 && keys.get(key)) {
				controller = new PlayerController();
			}
		}

		centerX = x + 16;
		centerY = y + 16;
		
		if((Boolean) inputs.get(Input.FIRE)){
			weapon.fire();
		}
		


	}

	@Override
	public void endStep() {
		// Recording movement
		/*
		 * Always Record if(recording) { record.addFrame(x, y, angle); }
		 */
	}

	@Override
	public void render() {
		sprite.renderRotated(x, y, angle);
		//renderLightMap();

	}

	@Override
	public void doCollisionWith(Entity e) {
		if (e instanceof Wall) {
			int left = Math.abs(e.x - (x + 32));
			int right = Math.abs(e.x + 32 - x);
			int up = Math.abs(e.y - (y + 32));
			int down = Math.abs(e.y + 32 - y);

			int min = Math.min(Math.min(left, right), Math.min(up, down));
			if (min == left) {
				x = e.x - 32;
			} else if (min == right) {
				x = e.x + 32;
			} else if (min == up) {
				y = e.y - 32;
			} else if (min == down) {
				y = e.y + 32;
			}
		}
		
		if(e instanceof Bullet) {
			if(team != ((Bullet)e).team) {
				takeDamage(((Bullet)e).getDamage());
			}
		}
	}


	public float getAngle() {
		return angle;
	}

	public double raytrace(ArrayList<RectangleHitbox> walls, RectangleHitbox ignore, double rayAngle) {
		double rads = rayAngle;
		
		LineHitbox line = new LineHitbox(this, 16, 16, 
				(int)(600*Math.cos(rads)), 
				(int)(600*Math.sin(rads)));

		double best = 600;

		for(RectangleHitbox w : walls) {
			/* Do a sanity check first before we do some heavy computing.
			 * Basically, this compares the rayAngle between the merc and the wall
			 * (both the beginning and end points)
			 * with the rayAngle the merc is facing. If it's past a certain threshold,
			 * skip computing the intersection because it can't exist.
			 */
			if(w.equals(ignore)) continue;
			float check1 = (float) Math.atan2(w.getY() - centerY, w.getX() - centerX);
			float check2 = (float) Math.atan2(w.getEndY() - centerY, w.getEndX() - centerX);
			double abs = Math.min(Math.abs(rayAngle - check1), Math.abs(rayAngle - check2));
			if(abs > 120 && abs < 240) continue;
			
			//Now get the distance
			float dist = (float) (600 * Hitbox.getIntersection
					(w, line, 0, 0));
			if(dist < best && dist > 0) best = dist;

		}


		float pointX = centerX + (float) (best * Math.cos(rads));
		float pointY = centerY + (float) (best * Math.sin(rads));
		
		//Renderer.drawLine(centerX, centerY, pointX, pointY, 1, Color.gray);

		return best;
	}

	private void renderLightMap() {
		ArrayList<Vertex> vertices = new ArrayList<>();
		ArrayList<RectangleHitbox> walls = new ArrayList<>();
		ArrayList<Angle> angles = new ArrayList<>();
		
		for(Entity e : stage.getAllEntities()) {
			if(e instanceof Wall) {
				Wall w = (Wall)e;
				walls.add((RectangleHitbox)w.hitbox);
			}
		}

		
		//Get a list of angles to raytrace to, then raytrace and get the actual points
		for(RectangleHitbox wall : walls) {
			for(int i = wall.getX(); i <= wall.getEndX(); i+=wall.getWidth()) {
				for(int j = wall.getY(); j <= wall.getEndY(); j+=wall.getHeight()) {
					double r = Math.atan2(j - y - 16, i - x - 16);

					boolean ignore = false;

					if(i==wall.getX() && j==wall.getY() || i==wall.getEndX() && j==wall.getEndY()) {
						if(centerX<i && centerY>j || centerX>i && centerY<j) ignore = true;
					} else {
						if (centerX > i && centerY > j || centerX < i
								&& centerY < j)
							ignore = true;
					}
					angles.add(new Angle(r, wall, ignore, i, j));
				}
			}
		}
		Collections.sort(angles);

		for (Angle a : angles) {
			double r = a.angle;
			boolean ignore = a.ignore;
			RectangleHitbox wall = a.parent;
			int i = a.i;
			int j = a.j;
			
			double rads = r;

			if (ignore) {
				double dist = raytrace(walls, wall, r);
				Vertex v = new Vertex((int) (centerX + dist * Math.cos(rads)),
						(int) (centerY + dist * Math.sin(rads)));

				// Compare it to the previous vertex
				boolean sameWall = false;
				if (vertices.size() > 0) {
					Vertex prev = vertices.get(vertices.size() - 1);
					if (prev.x == v.x || prev.y == v.y) {
						sameWall = true;
					}
				}

				vertices.add(v);
				Renderer.drawSquare(v.x - 2, v.y - 2, 4, Color.red);

				Vertex vv = new Vertex(i, j);
				double realDist = Math.sqrt(Math.pow(vv.x - centerX, 2)
						+ Math.pow(vv.y - centerY, 2));
				if (realDist < dist) {
					if (sameWall) {
						vertices.add(vv);
					} else {
						vertices.add(vertices.size() - 1, vv);
					}
					Renderer.drawSquare(vv.x - 2, vv.y - 2, 4, Color.blue);
				}

			} else {
				double dist = raytrace(walls, null, r);
				Vertex v = new Vertex((int) (centerX + dist * Math.cos(rads)),
						(int) (centerY + dist * Math.sin(rads)));
				vertices.add(v);
				Renderer.drawSquare(v.x - 2, v.y - 2, 4, Color.green);
			}
		}

		if (vertices.size() > 0) {
			Renderer.drawLine(vertices.get(0).x, vertices.get(0).y, centerX,
					centerY, 1, Color.white);
			vertices.add(vertices.get(0));
		}

		for (int i = 0; i < vertices.size() - 1; i++) {

			// Renderer.drawTriangle(centerX, centerY, vertices.get(i).x,
			// vertices.get(i).y,
			// vertices.get(i+1).x, vertices.get(i+1).y, new Color(255, 255,
			// 255, 50));

			// Renderer.drawLine(vertices.get(i).x, vertices.get(i).y,
			// vertices.get(i+1).x, vertices.get(i+1).y, 1, Color.white);
		}
	}

	public int getHealth() {
		return health;
	}

	public void takeDamage(int damage) {
		health -= damage;
		System.out.println(health);
		if(health <= 0) destroy();
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
		RectangleHitbox parent;
		boolean ignore;
		int i;
		int j;

		
		public Angle(double a, RectangleHitbox wall, boolean b, int i, int j) {
			angle = a;
			parent = wall;
			ignore = b;
			this.i = i;
			this.j = j;
		}

		@Override
		public int compareTo(Angle arg0) {
			return (int) (angle - arg0.angle);
		}
	}

	public Random getRandom() {
		return random;
	}

	public Weapon getWeapon() {
		return weapon;
	}

}
