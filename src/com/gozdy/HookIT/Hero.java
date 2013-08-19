package com.gozdy.HookIT;

import com.badlogic.gdx.math.MathUtils;



public class Hero extends DynamicGameObject {

    public static final float HERO_WIDTH = 0.8f;
    public static final float HERO_HEIGHT = 0.8f;
    public static final int HOOK_LAUNCHED = 0;
    public static final int HOOK_COOLDOWN = 1;
	
    int state;
    float stateTime; 
    Hook hook;
    
	public Hero(float x, float y, float width, float height) {
		super(x, y, HERO_WIDTH, HERO_HEIGHT);
		state = HOOK_COOLDOWN;
		stateTime=0;
		velocity.set(0, 0);
		hook = new Hook(x, y, 0.2f, 0.2f);	

	
	}
	
	  public void update(float delta)
	    {
		  
		  position.add(velocity.x * delta, 0);
		  if (state == HOOK_COOLDOWN)
		  {
			  hook.position.set(position);
		  }
		  
//		  if(position.x < 0)
//	            position.x = GameScreen.WORLD_WIDTH;
//		  
//	        if(position.x > GameScreen.WORLD_WIDTH)
//	            position.x = 0;
		  
		  stateTime += delta;
		  hook.update(delta);
	    }
	  
	  public void throwHook(float angle, float hookSpeed)
	  {
		  state = HOOK_LAUNCHED;
		  stateTime = 0;
		  velocity.set(0, 0);
		  
		 hook.velocity.x = MathUtils.cosDeg(angle) * hookSpeed;
		 hook.velocity.y = MathUtils.sinDeg(angle) * hookSpeed;
	  }
	  
	  public void getHook()
	  {
		  state = HOOK_COOLDOWN;
		  stateTime = 0;
		  
	  }
	  
	  

}
