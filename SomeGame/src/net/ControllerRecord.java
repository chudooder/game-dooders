package net;

import java.util.*;

public class ControllerRecord implements Controller {
	private Map<Long,Map<Input,Object>> record;
	public ControllerRecord(Map<Long,Map<Input,Object>> record){
		this.record = new HashMap<>(record);
	}
	@Override
	public Map<Input, Object> getInput(long frame) {
		return record.get(frame);
	}

	@Override
	public Controller getRecord() {
		return new ControllerRecord(record);
	}
	@Override
	public void set(Merc m) {
				
	}

}
