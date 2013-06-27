package net;

import java.util.Map;
import java.util.Set;

public interface Controller {
	Map<Input, Object> getInput(long frame);
	Controller getRecord();
	long getSeed();
	void setSeed(long s);
	void set(Merc m);
}
