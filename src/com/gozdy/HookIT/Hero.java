package com.gozdy.HookIT;



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
		  //MOVERSE HORIZONTALMENTE SI EL HOOK_COOLDOWN
		  
	    // ACTUALIZAR STATE SI TIRA EL HOOK 
		  
		  if(position.x < 0)
	            position.x = GameScreen.WORLD_WIDTH;
		  
	        if(position.x > GameScreen.WORLD_WIDTH)
	            position.x = 0;
		  
		  stateTime += delta;
		  hook.update(delta);
	    }
	  
	  public void throwHook()
	  {
		  state = HOOK_LAUNCHED;
		  stateTime = 0;
		  velocity.set(0, 0);
	  }
	  
	  public void getHook()
	  {
		  state = HOOK_COOLDOWN;
		  stateTime = 0;
		  
	  }
	  
	  

}