package net;

import java.util.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import chu.engine.Entity;

public class PlayerController implements Controller {
	private Map<Input, Integer> controls;
	private Entity relative; // getMousePos() returns position relative to this
	private Map<Long, Map<Input, Object>> record;
	private long seed;

	public PlayerController(){
		this(new HashMap<Input,Integer>());
		controls.put(Input.UP, Keyboard.KEY_W);
		controls.put(Input.DOWN, Keyboard.KEY_S);
		controls.put(Input.LEFT, Keyboard.KEY_A);
		controls.put(Input.RIGHT, Keyboard.KEY_D);
		controls.put(Input.RELOAD, Keyboard.KEY_R);
	}
	
	public PlayerController(Map<Input, Integer> controls) {
		this.controls = new HashMap<>(controls);
		this.record = new HashMap<>();
	}

	@Override
	public Map<Input, Object> getInput(long frame) {
		HashMap<Input, Object> input = new HashMap<>();
		for (Input i : Input.values()) {
			if(controls.containsKey(i)){
				input.put(i, Keyboard.isKeyDown(controls.get(i)));
			} else {
				input.put(i, false);
			}
		}
		
		if(Mouse.isButtonDown(0)) {
			input.put(Input.FIRE, true);
		} else {
			input.put(Input.FIRE, false);
		}
		
		if (relative != null) {
			int[] mouse = new int[] { TimeLapse.getMouseX() - relative.x - 16,
					TimeLapse.getMouseY() - relative.y - 16 };
			input.put(Input.MOUSE, mouse);
		} else {
			input.put(Input.MOUSE, null);
		}
		

		record.put(frame, input);
		//System.out.println(input);
		return input;
	}

	@Override
	public Controller getRecord() {
		return new ControllerRecord(record, seed);
	}
	
	public void set(Merc e){
		relative = e;
	}

	@Override
	public long getSeed() {
		return seed;
	}

	@Override
	public void setSeed(long s) {
		seed = s;
	}
}
