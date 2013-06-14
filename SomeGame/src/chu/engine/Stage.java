package chu.engine;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Stage {
	protected PriorityQueue<Entity> entities;
	private Stack<Entity> addStack;
	private Stack<Entity> removeStack;
	private int[][] terrainMap;
	
	public Stage() {
		entities = new PriorityQueue<>();
		addStack = new Stack<>();
		removeStack = new Stack<>();
		terrainMap = new int[40][40];
	}
	
	public PriorityQueue<Entity> getAllEntities() {
		PriorityQueue<Entity> ans = new PriorityQueue<>();
		for(Entity e : entities) {
			ans.add(e);
		}
		
		for(Entity e : addStack) {
			ans.add(e);
		}
		return ans;
	}
	
	/*
	public Entity getEntity(int i) {
		return entities.get(i);
	}
	*/
	
	public void addEntity(Entity e) {
		addStack.push(e);
	}
	
	public void addEntity(Entity e, int xx, int yy) {
		e.x = xx;
		e.y = yy;
		addStack.push(e);
	}
	
	
	public void removeEntity(Entity e) {
		//e.x = -100;
		//e.y = -100;
		e.flagForRemoval();
		if(e != null) removeStack.push(e);
	}
	
	public void update() {
		for(Entity e : entities) {
			e.update();
			e.doInput();
		}
		processAddStack();
		processRemoveStack();
	}

	public void render() {
		for(Entity e : entities) {
			e.render();
		}
	}
	
	public Entity instanceAt(int x, int y) {
		for(Entity e : entities) {
			if(e.x == x && e.y == y && !e.willBeRemoved()) return e;
		}
		return null;
	}
	
	public Entity[] allInstancesAt(int x, int y) {
		ArrayList<Entity> ans = new ArrayList<>();
		for(Entity e : entities) {
			if(e.x == x && e.y == y && !e.willBeRemoved()) ans.add(e);
		}
		
		for(Entity e : addStack) {
			if(e.x == x && e.y == y && !e.willBeRemoved()) ans.add(e);
		}
		
		Entity[] ret = new Entity[ans.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = ans.get(i);
		}
		return ret;
	}
	
	public Collideable[] collideableAt(int x, int y) {
		ArrayList<Collideable> ans = new ArrayList<>();
		for(Entity e : entities) {
			if(e instanceof Collideable && e.x == x && e.y == y && !e.willBeRemoved()) 
				ans.add((Collideable)e);
		}
		
		for(Entity e : addStack) {
			if(e instanceof Collideable && e.x == x && e.y == y && !e.willBeRemoved()) 
				ans.add((Collideable)e);
		}
		
		Collideable[] ret = new Collideable[ans.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = ans.get(i);
		}
		return ret;
	}
	
	public void processAddStack() {
		while(!addStack.isEmpty()) {
			Entity e = addStack.pop();
			entities.add(e);
		}
	}
	
	public boolean willBeRemoved(Entity e) {
		return removeStack.contains(e);
	}
	
	public void processRemoveStack() {
		while(!removeStack.isEmpty()) {
			Entity e = removeStack.pop();
			entities.remove(e);
			addStack.remove(e);		//Otherwise some weird shit happens and entities get stuck in limbo
		}
	}
	
	public void setTerrainMap(int x, int y, int b) {
		terrainMap[x/16][y/16] = b;
	}
	
	public int terrainAt(int x, int y) {
		return terrainMap[x/16][y/16];
	}
}
