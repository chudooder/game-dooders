package net;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import chu.engine.Entity;

public class NetworkController implements Controller {
	private Map<Input, Integer> controls;
	private Entity relative; // getMousePos() returns position relative to this
	private Map<Long, Map<Input, Object>> record;
	private HashMap<Input, Object> previousFrame;
	private boolean controllable;
	private long seed;
	private Weapon weapon;
	private int startX;
	private int startY;
	private Team team;

	public NetworkController(boolean c) {
		this(new HashMap<Input, Integer>(), c);
		controllable = c;
		controls.put(Input.UP, Keyboard.KEY_W);
		controls.put(Input.DOWN, Keyboard.KEY_S);
		controls.put(Input.LEFT, Keyboard.KEY_A);
		controls.put(Input.RIGHT, Keyboard.KEY_D);
		controls.put(Input.RELOAD, Keyboard.KEY_R);
	}

	public NetworkController(Map<Input, Integer> controls, boolean c) {
		this.controllable = c;
		this.controls = new HashMap<>(controls);
		this.record = new HashMap<>();
		this.previousFrame = new HashMap<>();
		previousFrame.put(Input.UP, false);
		previousFrame.put(Input.DOWN, false);
		previousFrame.put(Input.LEFT, false);
		previousFrame.put(Input.RIGHT, false);
		previousFrame.put(Input.RELOAD, false);
		previousFrame.put(Input.FIRE, false);
		previousFrame.put(Input.MOUSE, new int[] {0, -10});
	}

	@Override
	public Map<Input, Object> getInput(long frame) {
		// Serialize input and send it to the server
		HashMap<Input, Object> input = new HashMap<>();
		if(controllable && relative.stage.roundStarted){
			byte[] msg = new byte[14];
			msg[0] = 0;
			msg[1] = 0;
			int index = 2;
			for (Input i : Input.values()) {
				if (controls.containsKey(i)) {
					if (Keyboard.isKeyDown(controls.get(i)))
						msg[index] = 1;
					else
						msg[index] = 0;
				} else {
					msg[index] = 0;
				}
				index++;
			}

			if (Mouse.isButtonDown(0)) {
				msg[7] = 1;
			} else {
				msg[7] = 0;
			}

			/*
			 * if anyone asks
			 * it's fucking magic
			 */
			
			if (relative != null) {
				// 8 and 9 make up a 16bit rep. of the relative mouse x coord
				short mouseX = (short) (TimeLapse.getMouseX() - relative.x - 16);
				msg[8] = (byte) (mouseX & 0xFF); // first 8 bits
				msg[9] = (byte) ((mouseX >> 8) & 0xFF); // last 8 bits
				// 10 and 11 make up a 16bit rep. of the relative mouse y coord
				short mouseY = (short) (TimeLapse.getMouseY() - relative.y - 16);
				msg[10] = (byte) (mouseY & 0xFF); // first 8 bits
				msg[11] = (byte) ((mouseY >> 8) & 0xFF); // last 8 bits

			} else {
				msg[8] = 0;
				msg[9] = 0;
				msg[10] = 0;
				msg[11] = 0;
			}
			TimeLapse.getClient().sendMessage(msg);
		}

		// Get server response
		{
			ArrayList<byte[]> messages = TimeLapse.getServerMessages();
			boolean gotMessage = false;
			for(int k=0; k<messages.size(); k++) {
				byte[] line = messages.get(k);
				if (line[1] == 0) {
					int index = 2;
					gotMessage = true;
					for (Input i : Input.values()) {
						if (controls.containsKey(i)) {
							input.put(i, line[index] == 1);
						} else {
							input.put(i, false);
						}
						index++;
					}

					if (line[7] == 1) {
						input.put(Input.FIRE, true);
					} else {
						input.put(Input.FIRE, false);
					}

					if (relative != null) {
						ByteBuffer mouseX = ByteBuffer.wrap(new byte[] {
								line[9], line[8] });
						ByteBuffer mouseY = ByteBuffer.wrap(new byte[] {
								line[11], line[10] });
						int[] mouse = new int[] { mouseX.getShort(), mouseY.getShort() };
						input.put(Input.MOUSE, mouse);
					} else {
						input.put(Input.MOUSE, null);
					}
					previousFrame = input;
				}
			}
			
			if(!gotMessage) {
				input = previousFrame;
			}
		}
		record.put(frame, input);
		return input;
	}

	@Override
	public Controller getRecord() {
		return new ControllerRecord(record, seed, weapon, startX, startY, team);
	}

	@Override
	public long getSeed() {
		return seed;
	}

	@Override
	public void setSeed(long s) {
		seed = s;
	}

	@Override
	public void set(Merc m) {
		relative = m;
		weapon = m.getWeapon();
		startX = m.x;
		startY = m.y;
		team = m.team;
	}

	@Override
	public Weapon getWeapon() {
		return weapon;
	}
	
	@Override
	public int getStartX() {
		return startX;
	}
	
	@Override
	public int getStartY() {
		return startY;
	}

	@Override
	public Team getTeam() {
		return team;
	}

}
