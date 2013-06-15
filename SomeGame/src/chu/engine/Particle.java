package chu.engine;

import net.TimeLapseStage;



//Entity with a specific lifetime. It removes itself after its lifetime expires.
public class Particle extends Entity {
	
	private long lifetime;
	private long startTime;

	public Particle(TimeLapseStage stage, int x, int y, int lifetime) {
		super(stage,x,y);
		this.lifetime = lifetime;
		startTime = System.nanoTime();
	}
	
	public void onStep() {
		super.onStep();
		if((System.nanoTime() - startTime)/1000000 > lifetime) {
			stage.removeEntity(this);
		}
	}

	@Override
	public void beginStep() {
		
	}

	@Override
	public void endStep() {
		
	}

}
