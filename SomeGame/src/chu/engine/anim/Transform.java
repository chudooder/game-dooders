package chu.engine.anim;

public class Transform {
	
	public float rotation;
	public float translateX;
	public float translateY;
	public float scaleX;
	public float scaleY;
	
	public Transform() {
		rotation = 0;
		translateX = 0;
		translateY = 0;
		scaleX = 1;
		scaleY = 1;
	}
	
	public void setRotation(float angle) {
		rotation = angle;
	}
	
	public void setTranslation(float x, float y) {
		translateX = x;
		translateY = y;
	}
	
	public void setScale(float x, float y) {
		scaleX = x;
		scaleY = y;
	}

}
