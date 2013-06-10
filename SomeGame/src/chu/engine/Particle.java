package chu.engine;



//Entity with a specific lifetime. It removes itself after its lifetime expires.
public class Particle extends Entity {
	
	private long lifetime;
	private long startTime;

	public Particle(Stage stage, int x, int y, int lifetime) {
		super(stage,x,y);
		this.lifetime = lifetime;
		startTime = System.nanoTime();
	}
	
	public void update() {
		super.update();
		if((System.nanoTime() - startTime)/1000000 > lifetime) {
			stage.removeEntity(this);
		}
	}

	@Override
	public boolean doInput() {
		return false;
	}

}
