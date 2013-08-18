package com.gozdy.HookIT;



public class Hook extends DynamicGameObject {

	public Hook(float x, float y, float width, float height) {
		super(x, y, width, height);
		velocity.set(0,0);
		
		// TODO Auto-generated constructor stub
	}

	public void update(float delta)
	{
		 position.add(velocity.x * delta, velocity.y * delta);
		 bounds.setX(position.x);
		 bounds.setY(position.y);
	}
	

}
