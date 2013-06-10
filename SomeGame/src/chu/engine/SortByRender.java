package chu.engine;

import java.util.Comparator;

public class SortByRender implements Comparator<Entity> {

	public SortByRender() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Entity arg0, Entity arg1) {
		return arg0.renderPriority - arg1.renderPriority;
	}

}
