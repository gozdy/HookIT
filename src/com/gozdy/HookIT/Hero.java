package com.gozdy.HookIT;

import com.badlogic.gdx.math.MathUtils;



public class Hero extends DynamicGameObject {

    public static final float HERO_WIDTH = 2f;
    public static final float HERO_HEIGHT = 4f;
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
		hook = new Hook(x, y+bounds.height/3, 0.4f, 0.4f);	

	
	}
	
	  public void update(float delta)
	    {
		  
		 
		  if (state == HOOK_COOLDOWN)
		  {
			  hook.position.set(position.x, position.y+bounds.height/3);
		  }
		  
		  stateTime += delta;
		  hook.update(delta);
	    }
	  
	  public void throwHook()
	  {
		  state = HOOK_LAUNCHED;
		  stateTime = 0;
		 
		  
	  }
	  
	  public void getHook()
	  {
		  state = HOOK_COOLDOWN;
		  stateTime = 0;
		  hook.velocity.set(0,0);
		  
	  }
	  
	  

}
