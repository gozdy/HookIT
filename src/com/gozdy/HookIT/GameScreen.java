package com.gozdy.HookIT;

import java.util.Iterator;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.gozdy.HookIT.Assets;


public class GameScreen implements Screen {
	
	
	public static final float WORLD_WIDTH = 9;
	public static final float WORLD_HEIGHT = 16;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	int state;
	float stateTime;
	private FPSLogger logger;
	
	HookGame hgame;
	

	
	PointLight hookLight;


	
	OrthographicCamera camera;
	World world;
	RayHandler rayHandler;
	
	Array<Enemy> enemies;

	Objective objective;
	Iterator<Enemy> iter;
	
	Hero hero;
	float hookAngle = 0;
	Vector2 gravity = new Vector2(0,-9.8f);
	long lastEnemyTime;
	float hookTime = 4f;
	float distancehero = 0f;
	boolean won = false;
	
	
	
	
	   

	public GameScreen(final HookGame game) {
		
		Assets.load();
		hgame = game;
		logger = new FPSLogger();
		state = GAME_RUNNING;
		stateTime = 0f;
		// load the image and set camera
		

		Assets.playSprite.setSize(4, 1);
		Assets.playSprite.setPosition(WORLD_WIDTH/2-Assets.playSprite.getWidth()/2, WORLD_HEIGHT/2);
		

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		
		// SPAWN Hero and Enemies
		hero = new Hero(WORLD_WIDTH*0.5f-1f, 0f, 2f, 5.4f);
		enemies = new Array<Enemy>();
		spawnenemy();
		
		objective = new Objective(1, 1, 1, 1);
		
		Assets.hookSprite.setPosition(hero.hook.position.x, hero.hook.position.y);
		Assets.hookSprite.setOrigin(0.2f,0.0f);
		Assets.hookSprite.setSize(0.4f, 0.4f);
		
		Assets.ropeSprite.setPosition(hero.hook.position.x+0.15f, hero.hook.position.y);
		Assets.ropeSprite.setOrigin(0, 0);
		
		
		
// LIGHTING
		world = new World(gravity, false);
		
		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.setAmbientLight(0.9f);
	
//		new ConeLight(rayHandler, 100, Color.CYAN, 20f, WORLD_WIDTH/2, WORLD_HEIGHT, 270, 35);
		
		hookLight = new PointLight(rayHandler, 10, new Color(1, 1, 1, 0.3f), 0.5f, hero.hook.position.x+0.2f, hero.hook.position.y+0.2f);
		
	}

	
	private void spawnenemy() {
		Enemy enemy = new Enemy(0, 0,1f,1f);
		enemies.add(enemy);
		lastEnemyTime = TimeUtils.nanoTime();
	}


	@Override
	public void render(float delta) {
		
		switch (state) {
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_RUNNING:
			updateRunning(delta);
		}
		
		//SPAWN ENEMY AFTER 1 Second
		 
		
		//RENDERING 
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		hgame.batch.setProjectionMatrix(camera.combined);
		
		hgame.batch.begin();
		hgame.batch.draw(Assets.backgroundRegion, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
		
		
		for (int i = 0; i < Assets.candies.size; i++) {
			hgame.batch.draw(Assets.candies.get(i), 0+(i*1.5f), WORLD_HEIGHT-0.6f, 0.5f,0.5f);
			if (objective.candies[i] < 7) {
				hgame.batch.draw(Assets.number.get(objective.candies[i]), 0.5f+(i*1.5f), WORLD_HEIGHT-0.6f, 0.5f,0.5f);
			}
			
		}
		
		

		 for (Enemy enemy : enemies) {
			hgame.batch.draw(Assets.candies.get(enemy.candyType), enemy.position.x, enemy.position.y, enemy.bounds.width, enemy.bounds.height);
		 }
		 
		 Assets.hookSprite.setPosition(hero.hook.position.x, hero.hook.position.y);
		 Assets.ropeSprite.draw(hgame.batch);
		 Assets.hookSprite.draw(hgame.batch);
		hgame.batch.draw(Assets.heroimage, hero.position.x, hero.position.y, hero.bounds.width, hero.bounds.height);
		if (state == GAME_PAUSED) renderPaused();
		hgame.batch.end();
		
		rayHandler.updateAndRender();
		
		
		logger.log();
	}

	private void renderPaused() {
		Assets.playSprite.draw(hgame.batch);		
	}


	private void updateRunning(float delta) {
		// TODO Auto-generated method stub
		 
		  if(TimeUtils.nanoTime() - lastEnemyTime > 1000000000) 	 
	  		{
			  spawnenemy();
			  }
		
		// HANDLING INPUT
		
		  if (Gdx.input.isTouched() && stateTime>0.5f) {
			  if (hero.state == Hero.HOOK_COOLDOWN){
			  
	// Check touch position and angle
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            Vector2 touchPos2 = new Vector2(touchPos.x, touchPos.y);
            hookAngle = touchPos2.sub(hero.hook.position).angle();
            float ballspeed = 15f;
            
            hero.throwHook(hookAngle, ballspeed);
            			
            Assets.hookSprite.setRotation(hookAngle-90);
            Assets.ropeSprite.setRotation(hookAngle-90);
			  }
    }
		  
		  // Change rope size depending where the hook is
		  
		  distancehero = hero.hook.position.dst(hero.position.x, hero.position.y+hero.bounds.height/3)+0.1f;
		  Assets.ropeSprite.setSize(0.1f, distancehero);
		  Assets.ropeSprite.setV(0);
		  Assets.ropeSprite.setV2(distancehero);
		  
		  
		  
		//Manage return when enemy is hooked
		  for (Enemy enemy : enemies) {
			  if (hero.hook.bounds.overlaps(enemy.bounds) && enemy.state == Enemy.ALIVE && hero.state == Hero.HOOK_LAUNCHED ) {			
				  if (hero.enemyState == Hero.NOENEMY){
					  hookTime = hero.stateTime*2;
				  hero.hook.velocity.x = -hero.hook.velocity.x;
			 hero.hook.velocity.y = -hero.hook.velocity.y;
				  		}	
			 enemy.state = Enemy.HOOKED;
			
			 hero.enemyState = Hero.GOTENEMY;
			 objective.candies[enemy.candyType]++;
		  		}
		  }
		  
		  // Handle hooked enemies
		  for (Enemy enemy : enemies) {
			  if (enemy.state == Enemy.HOOKED){
			enemy.position.set(hero.hook.position);
					}
		  	}	
		  
		  
//RETURN HOOK AFTER time*2 since collition	  
		  
		  if (hero.state == Hero.HOOK_LAUNCHED && (hero.stateTime > hookTime || hero.hook.position.y < 0 )){
			  hero.getHook();
			  Assets.hookSprite.setRotation(0);
			  if(objective.checkWon()) state=GAME_PAUSED;
			  iter = enemies.iterator();
		      while(iter.hasNext()) {
		         Enemy enemy = iter.next();
		         if(enemy.state == Enemy.HOOKED ){
		        	 iter.remove();
		         Gdx.app.log("REMO", "REMOVED");
		         hookTime= 4f;
		        	 }
		      }
		  }
		  
		  // MANAGE RETURN WHEN NOT HOOKED
		  if (hero.hook.position.x+0.2f > WORLD_WIDTH || hero.hook.position.x-0.2f < 0 || hero.hook.position.y+0.2f > WORLD_HEIGHT){
			  hookTime = hero.stateTime*2;
			  hero.hook.velocity.x = -hero.hook.velocity.x;
				 hero.hook.velocity.y = -hero.hook.velocity.y;
				 hero.enemyState = Hero.GOTENEMY;
				  Gdx.app.log("hookTime", Float.toString(hookTime));	
		  }
		  
		 
		  
		  
		  //Remove enemies that fall under y < 0 or X>width
		  
		  iter = enemies.iterator();
	      while(iter.hasNext()) {
	         Enemy enemy = iter.next();
	         
	         if(enemy.position.y + enemy.bounds.height/2 < 0 || enemy.isOutOfBounds(WORLD_WIDTH, WORLD_HEIGHT)) 
	        	 {iter.remove();
	         Gdx.app.log("REMO", "REMOVED");
	        	 }
	      }
	
	      // Update positions of Hero and Enemies
		hero.update(delta);
		for (Enemy enemy : enemies) {
		enemy.update(delta);
		}
		
		hookLight.setPosition(hero.hook.position.x+0.2f, hero.hook.position.y+0.2f);
		
		stateTime += delta;
	}


	private void updatePaused() {
		// TODO Auto-generated method stub
		objective.reset();
		won = false;
		if(Gdx.input.isTouched()){
			Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            
            if(Assets.playSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)){
            	state = GAME_RUNNING;
            	stateTime = 0;
            	}
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
		Assets.enemyimage.dispose();
		Assets.heroimg.dispose();
		Assets.background.dispose();
		Assets.hookimage.dispose();
hgame.batch.dispose();
rayHandler.dispose();
Assets.ropeImg.dispose();
Assets.candy.dispose();
	}

}
