package com.gozdy.HookIT;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	
 
	 public static final float WORLD_WIDTH = 9;
	    public static final float WORLD_HEIGHT = 16;
	
	HookGame hgame;
	Texture hookimage;
	Texture enemyimage;
	Texture heroimage;
	OrthographicCamera camera;
	Array<Enemy> enemies;
	Hero hero;
	float hookAngle = 0;
	Vector2 gravity = new Vector2(0,-10);
	long lastEnemyTime;
	float hookTime;
	
	
	
	    int state;

	public GameScreen(final HookGame game) {
		
		hgame = game;
		// load the image and set camera
		hookimage = new Texture(Gdx.files.internal("bobrgb888-32x32.png"));
		enemyimage = new Texture(Gdx.files.internal("bobargb8888.png"));
		heroimage = new Texture(Gdx.files.internal("avatar_pudge.png"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		
		// SPAWN Hero and Enemies
		hero = new Hero(WORLD_WIDTH*0.5f, WORLD_HEIGHT*0.1f, 1f, 1f);
		enemies = new Array<Enemy>();
		spawnenemy();
	}

	
	private void spawnenemy() {
		Enemy enemy = new Enemy(0, 0,1f,1f);
		enemies.add(enemy);
		lastEnemyTime = TimeUtils.nanoTime();
	}


	@Override
	public void render(float delta) {

		
		//RENDERING 
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		hgame.batch.setProjectionMatrix(camera.combined);
		
		hgame.batch.begin();
		 for (Enemy enemy : enemies) {
			hgame.batch.draw(enemyimage, enemy.position.x, enemy.position.y, enemy.bounds.width, enemy.bounds.height);
		 }
		hgame.batch.draw(heroimage, hero.position.x, hero.position.y, hero.bounds.width, hero.bounds.height);
		hgame.batch.draw(hookimage, hero.hook.position.x, hero.hook.position.y, hero.hook.bounds.width, hero.hook.bounds.height);
		hgame.batch.end();
		
		
		// HANDLING INPUT
		
		  if (Gdx.input.isTouched()) {
			  if (hero.state == Hero.HOOK_COOLDOWN)
			  {
			  	
	// Check touch position and angle
			  hero.hook.position.set(hero.position);
              Vector3 touchPos = new Vector3();
              touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
              camera.unproject(touchPos);
              Vector2 touchPos2 = new Vector2(touchPos.x, touchPos.y);
              hookAngle = touchPos2.sub(hero.hook.position).angle();
              float ballspeed = 15f;
				
				
				hero.throwHook(hookAngle, ballspeed);	
			  }
          
      }
		  
		  
		  //SPAWN ENEMY AFTER 1 Second
		  
		  if(TimeUtils.nanoTime() - lastEnemyTime > 1000000000) 	 
	  		{
			  spawnenemy();
			  }
		  
// RETURN HOOK AFTER 1.5 Seconds (Need to rework to go back to the hero instead of fixed time)		  
		  
		  if (hero.state == Hero.HOOK_LAUNCHED && (hero.stateTime > hookTime || (hero.hook.position.x > WORLD_WIDTH || hero.hook.position.y > WORLD_HEIGHT))){
			  hero.getHook();
			  Iterator<Enemy> iter = enemies.iterator();
		      while(iter.hasNext()) {
		         Enemy enemy = iter.next();
		         
		         if(enemy.state == Enemy.HOOKED)
		        	 {iter.remove();
		         Gdx.app.log("REMO", "REMOVED");
		        	 }
		      }
		  }
		  
		  
		  // Handle hooked enemies
		  for (Enemy enemy : enemies) {
			  if (enemy.state == Enemy.HOOKED){
			enemy.position.set(hero.hook.position);
					}
		  	}	
		  
		  //Manage return when enemy is hooked
		  for (Enemy enemy : enemies) {
			  if (hero.hook.bounds.overlaps(enemy.bounds) && enemy.state == Enemy.ALIVE && hero.state == Hero.HOOK_LAUNCHED)
		  {			
				  hookTime = hero.stateTime*2;
				  hero.hook.velocity.x = -hero.hook.velocity.x;
			 hero.hook.velocity.y = -hero.hook.velocity.y;
			 Gdx.app.log("hookangle", "OVERLAP");
			 enemy.state = Enemy.HOOKED;
		  		}
		  }
		  
		  // MANAGE RETURN WHEN NOT HOOKED
		  if (hero.hook.position.x == WORLD_WIDTH || hero.hook.position.x == 0 || hero.hook.position.y == WORLD_HEIGHT){
			  hero.hook.velocity.x = -hero.hook.velocity.x;
				 hero.hook.velocity.y = -hero.hook.velocity.y;
				 hookTime = hero.stateTime*2;
				 
		  }
		  
		  
		  if ( hero.stateTime*2 == hookTime && hero.state == Hero.HOOK_LAUNCHED){
			  hero.hook.velocity.set(0, 0);
		  }
		  
		 
		  
		  //Remove enemies that fall under y < 0
		  
		  Iterator<Enemy> iter = enemies.iterator();
	      while(iter.hasNext()) {
	         Enemy enemy = iter.next();
	         
	         if(enemy.position.y + 64 < 0) 
	        	 {iter.remove();
	         Gdx.app.log("REMO", "REMOVED");
	        	 }
	      }
	
	      // Update positions of Hero and Enemies
		hero.update(delta);
		for (Enemy enemy : enemies) {
		enemy.update(delta);
		}
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
enemyimage.dispose();
heroimage.dispose();
hgame.batch.dispose();
	}

}
