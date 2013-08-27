package com.gozdy.HookIT;

import com.badlogic.gdx.math.MathUtils;



public class Hero extends DynamicGameObject {


    public static final int HOOK_LAUNCHED = 0;
    public static final int HOOK_COOLDOWN = 1;
    public static final int GOTENEMY = 0;
    public static final int NOENEMY = 1;
	
    int state;
    int enemyState;
    float stateTime;
    int candyType;
    Hook hook;
    
	public Hero(float x, float y, float width, float height) {
		super(x, y, width, height);
		state = HOOK_COOLDOWN;
		enemyState = NOENEMY;
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
	  
	  public void throwHook(float angle, float ballspeed)
	  {
		  state = HOOK_LAUNCHED;
		  stateTime = 0;
		  hook.velocity.x = MathUtils.cosDeg(angle) * ballspeed;
			hook.velocity.y = MathUtils.sinDeg(angle) * ballspeed;
			
		 
		  
	  }
	  
	  public void getHook()
	  {
		  state = HOOK_COOLDOWN;
		  enemyState = NOENEMY;
		  stateTime = 0;
		  hook.velocity.set(0,0);
		  
	  }
	  
	  

}
