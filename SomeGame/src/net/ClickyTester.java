package net;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import chu.engine.Entity;

public class ClickyTester extends Entity {

	public ClickyTester(TimeLapseStage stage, int x, int y) {
		super(stage, x, y);
	}

	boolean leftClicked = false;
	boolean rightClicked = false;
	
	public void beginStep() {
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			if(Mouse.isButtonDown(0) && !leftClicked) {
				leftClicked = true;
				stage.addEntity(new Wall(stage, TimeLapse.getMouseX(), 
						TimeLapse.getMouseY()));
			} else if(!Mouse.isButtonDown(0) && leftClicked) {
				leftClicked = false;
			}
			
			if(Mouse.isButtonDown(1) && !rightClicked) {
				rightClicked = true;
				stage.addEntity(new Wall(stage, TimeLapse.getMouseX()/32*32, 
						TimeLapse.getMouseY()/32*32));
			} else if(!Mouse.isButtonDown(1) && rightClicked) {
				rightClicked = false;
			}
		}
	}

	@Override
	public void endStep() {
		
	}

}
