package net;

import java.util.*;

import org.lwjgl.input.Keyboard;

import chu.engine.Entity;

public class PlayerController implements Controller {
	private Map<Input, Integer> controls;
	private Entity relative; // getMousePos() returns position relative to this
	private Map<Long, Map<Input, Object>> record;

	public PlayerController(){
		this(new HashMap<Input,Integer>());
		controls.put(Input.UP, Keyboard.KEY_W);
		controls.put(Input.DOWN, Keyboard.KEY_S);
		controls.put(Input.LEFT, Keyboard.KEY_A);
	}
	
	public PlayerController(Map<Input, Integer> controls) {
		this.controls = new HashMap<>(controls);
		this.record = new HashMap<>();
	}

	@Override
	public Map<Input, Object> getInput(long frame) {
		HashMap<Input, Object> input = new HashMap<>();
		for (Input i : Input.values()) {
			if(controls.containsKey(controls.get(i))){
				input.put(i, Keyboard.isKeyDown(controls.get(i)));
			} else {
				input.put(i, false);
			}
			
		}
		if (relative != null) {
			int[] mouse = new int[] { TimeLapse.getMouseX() - relative.x - 16,
					TimeLapse.getMouseY() - relative.y - 16 };
			input.put(Input.MOUSE, mouse);
		} else {
			input.put(Input.MOUSE, null);
		}
		record.put(frame, input);
		return input;
	}

	@Override
	public Controller getRecord() {
		return new ControllerRecord(record);
	}
	
	public void setRelative(Entity e){
		relative = e;
	}
}
