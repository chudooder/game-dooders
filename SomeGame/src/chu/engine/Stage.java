package chu.engine;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeSet;

public class Stage {
	protected TreeSet<Entity> entities;
	private Stack<Entity> addStack;
	private Stack<Entity> removeStack;
	
	public Stage() {
		entities = new TreeSet<>(new SortByUpdate());
		addStack = new Stack<>();
		removeStack = new Stack<>();
	}
	
	public TreeSet<Entity> getAllEntities() {
		TreeSet<Entity> ans = new TreeSet<>();
		return entities;
	}
	
	public void addEntity(Entity e) {
		addStack.push(e);
	}
	
	
	public void removeEntity(Entity e) {
		e.flagForRemoval();
		if(e != null) removeStack.push(e);
	}
	
	public void update() {
		for(Entity e : entities) {
			e.onStep();
			e.beginStep();
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
	
}
