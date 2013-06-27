package net;

import java.util.*;

public class ControllerRecord implements Controller {
	private Map<Long,Map<Input,Object>> record;
	private long seed;
	public ControllerRecord(Map<Long,Map<Input,Object>> record, long seed){
		this.record = new HashMap<>(record);
		this.seed = seed;
	}
	@Override
	public Map<Input, Object> getInput(long frame) {
		return record.get(frame);
	}

	@Override
	public Controller getRecord() {
		return new ControllerRecord(record, seed);
	}
	@Override
	public void set(Merc m) {
				
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
