package chu.engine;

import java.util.Comparator;

public class SortByRender implements Comparator<Entity> {

	public SortByRender() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Entity arg0, Entity arg1) {
		if(arg0.renderDepth == arg1.renderDepth){ 
			return arg0.hashCode()-arg1.hashCode();
		}
		return (int)(arg0.renderDepth - arg1.renderDepth);
	}

}
