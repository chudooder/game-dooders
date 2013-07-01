package net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private float angle; // IN RADIANS.
	private Random random;
	private Weapon weapon;
	private Controller controller;
	public int centerX;
	public int centerY;
	private int frame;
	private int health;
	private int movespeed;
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
		renderDepth = Entity.RENDER_PRIORITY_PLAYER;
		// Is there a way to get the seed?
		random = new Random();
		if (c.getSeed() == 0) {
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
		movespeed = 2;
		team = t;
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
			dy = -movespeed;
		if ((Boolean) inputs.get(Input.DOWN))
			dy = movespeed;
		if ((Boolean) inputs.get(Input.LEFT))
			dx = -movespeed;
		if ((Boolean) inputs.get(Input.RIGHT))
			dx = movespeed;
		if ((Boolean) inputs.get(Input.RELOAD))
			weapon.reload();

		if (!stage.checkCollision(this, Block.class, dx, 0)) {
			x += dx;
		}

		if (!stage.checkCollision(this, Block.class, 0, dy)) {
			y += dy;
		}

		HashMap<Integer, Boolean> keys = Game.getKeys();
		for (int key : keys.keySet()) {
			if (key == Keyboard.KEY_F3 && keys.get(key)) {
				stage.addEntity(new Merc(stage, 320, 240, controller
						.getRecord(), Team.RED));
			}
		}

		centerX = x + 16;
		centerY = y + 16;

		if ((Boolean) inputs.get(Input.FIRE)) {
			weapon.fire();
		}

	}

	@Override
	public void render() {
		sprite.renderRotated(x, y, renderDepth, angle);
		if (controller instanceof NetworkController) {
			weapon.renderHUD();
			renderShadows();
		}


	}

	@Override
	public void doCollisionWith(Entity e) {
		if (e instanceof Block) {
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

		if (e instanceof Bullet) {
			if (team != ((Bullet) e).team) {
				takeDamage(((Bullet) e).getDamage());
			}
		}
	}

	public double raytrace(ArrayList<RectangleHitbox> walls,
			RectangleHitbox ignore, double rayAngle) {
		double rads = rayAngle;

		LineHitbox line = new LineHitbox(this, 16, 16,
				(int) (600 * Math.cos(rads)), (int) (600 * Math.sin(rads)));

		double best = 600;

		for (RectangleHitbox w : walls) {
			/*
			 * Do a sanity check first before we do some heavy computing.
			 * Basically, this compares the rayAngle between the merc and the
			 * wall (both the beginning and end points) with the rayAngle the
			 * merc is facing. If it's past a certain threshold, skip computing
			 * the intersection because it can't exist.
			 */
			if (w.equals(ignore))
				continue;
			float check1 = (float) Math.atan2(w.getY() - centerY, w.getX()
					- centerX);
			float check2 = (float) Math.atan2(w.getEndY() - centerY,
					w.getEndX() - centerX);
			double abs = Math.min(Math.abs(rayAngle - check1),
					Math.abs(rayAngle - check2));
			if (abs > 120 && abs < 240)
				continue;

			// Now get the distance
			float dist = (float) (600 * Hitbox.getIntersection(w, line, 0, 0));
			if (dist < best && dist > 0)
				best = dist;

		}
		// Renderer.drawLine(centerX, centerY, pointX, pointY, 1, Color.gray);

		return best;
	}

	public void renderShadows() {
		ArrayList<Block> blocks = new ArrayList<>();
		for (Entity e : stage.getAllEntities()) {
			if (e instanceof Block) {
				Block w = (Block) e;
				blocks.add(w);
			}
		}

		Comparator<Vertex> byAngle = new Comparator<Vertex>() {
			@Override
			public int compare(Vertex arg0, Vertex arg1) {
				double angle0 = Math.atan2(arg0.y - centerY, arg0.x - centerX);
				double angle1 = Math.atan2(arg1.y - centerY, arg1.x - centerX);
				if (angle0 > angle1)
					return 1;
				if (angle0 < angle1)
					return -1;

				double dist0 = Math.sqrt(Math.pow(arg0.y - centerY, 2)
						+ Math.pow(arg0.x - centerX, 2));
				double dist1 = Math.sqrt(Math.pow(arg1.y - centerY, 2)
						+ Math.pow(arg1.x - centerX, 2));
				if(dist0 > dist1)
					return 1;
				if(dist0 < dist1)
					return -1;
				return 0;
			}
		};

		// fuck angles
		Comparator<Vertex> special = new Comparator<Vertex>() {
			@Override
			public int compare(Vertex arg0, Vertex arg1) {
				double angle0 = Math.atan2(arg0.y - centerY, arg0.x - centerX);
				double angle1 = Math.atan2(arg1.y - centerY, arg1.x - centerX);
				if (angle0 * angle1 > 0) {
					if (angle0 > angle1)
						return 1;
					if (angle0 < angle1)
						return -1;
				} else {
					if (angle0 > angle1)
						return -1;
					if (angle0 < angle1)
						return 1;
				}

				return 0;
			}
		};

		for (Block block : blocks) {
			ArrayList<Vertex> vertices = new ArrayList<>();
			for (int i = block.x; i <= block.getEndX(); i += block.getWidth()) {
				for (int j = block.y; j <= block.getEndY(); j += block
						.getHeight()) {
					boolean add = true;
					if (i == block.x && j == block.y) {
						if (centerX <= i && centerY <= j)
							add = false;
					} else if (i == block.x && j == block.getEndY()) {
						if (centerX <= i && centerY >= j)
							add = false;
					} else if (i == block.getEndX() && j == block.getEndY()) {
						if (centerX >= i && centerY >= j)
							add = false;
					} else if (i == block.getEndX() && j == block.y) {
						if (centerX >= i && centerY <= j)
							add = false;
					}

					if (add)
						vertices.add(new Vertex(i, j));
				}
			}

			if (centerX > block.getEndX() && centerY > block.y
					&& centerY < block.getEndY()) {
				Collections.sort(vertices, special);
			} else {
				Collections.sort(vertices, byAngle);
			}
			Vertex prevOuter = null;
			for (int i = 0; i < vertices.size(); i++) {
				Vertex v = vertices.get(i);
				double angle = Math.atan2(v.y - centerY, v.x - centerX);
				Vertex outer = new Vertex((int) (v.x + 1000 * Math.cos(angle)),
						(int) (v.y + 1000 * Math.sin(angle)));
				Color color = new Color(25, 25, 25);
				if (i != vertices.size() - 1) {
					Renderer.drawTriangle(v.x, v.y, outer.x, outer.y,
							vertices.get(i + 1).x, vertices.get(i + 1).y,
							Entity.RENDER_PRIORITY_SHADOW, color);
				}
				if (i != 0) {
					Renderer.drawTriangle(v.x, v.y, outer.x, outer.y,
							prevOuter.x, prevOuter.y,
							Entity.RENDER_PRIORITY_SHADOW, color);
				}
				prevOuter = outer;
//				TimeLapse.guiFont.drawString(v.x, v.y, "" + i);
			}
		}

	}

	public float getAngle() {
		return angle;
	}

	public int getHealth() {
		return health;
	}

	public Random getRandom() {
		return random;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void takeDamage(int damage) {
		health -= damage;
		System.out.println(health);
		if (health <= 0)
			destroy();
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

	class Wall {
		int x0, y0, x1, y1;

		public Wall(int a, int b, int c, int d) {
			x0 = a;
			y0 = b;
			x1 = c;
			y1 = d;
		}
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}

}
