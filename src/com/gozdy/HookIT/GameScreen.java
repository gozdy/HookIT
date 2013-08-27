package com.gozdy.HookIT;

import java.util.Iterator;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


public class GameScreen implements Screen {
	
 
	 public static final float WORLD_WIDTH = 9;
	    public static final float WORLD_HEIGHT = 16;
	
	HookGame hgame;
	
	TextureRegion backgroundRegion;
	TextureRegion heroimage;
	TextureRegion ropeRegion;

	
	Texture background;
	Texture heroimg;
	Texture ropeImg;
	Texture hookimage;
	Texture enemyimage;
	Texture candy;
	
	PointLight hookLight;
	
	Sprite ropeSprite;
	Sprite hookSprite;
	
	OrthographicCamera camera;
	World world;
	RayHandler rayHandler;
	
	Array<Enemy> enemies;
	Array<TextureRegion> candies;
	
	Hero hero;
	float hookAngle = 0;
	Vector2 gravity = new Vector2(0,-9.8f);
	long lastEnemyTime;
	float hookTime = 4f;
	float distancehero = 0f;
	
	
	
	
	    int state;

	public GameScreen(final HookGame game) {
		
		hgame = game;
		// load the image and set camera
		background = new Texture(Gdx.files.internal("backgroundHookIT.png"));
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
		hookimage = new Texture(Gdx.files.internal("HOOKRotado.png"));
		heroimg = new Texture(Gdx.files.internal("chef.png"));
		heroimage = new TextureRegion(heroimg, 75, 0, 95, 256);
		hookSprite = new Sprite(hookimage);
		
		candy = new Texture(Gdx.files.internal("candies.png"));

		
		candies = new Array<TextureRegion>();
		candies.add(new TextureRegion(candy, 0, 0, 64, 64));
		candies.add(new TextureRegion(candy, 0, 64, 64, 64));
		candies.add(new TextureRegion(candy, 64, 0, 64, 64));
		candies.add(new TextureRegion(candy, 64, 64, 64, 64));
		
		
		ropeImg = new Texture(Gdx.files.internal("ropeVertical64.png"));
		ropeImg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		ropeRegion = new TextureRegion(ropeImg, 0, 0, 5, 5);
		ropeSprite = new Sprite(ropeRegion);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		
		// SPAWN Hero and Enemies
		hero = new Hero(WORLD_WIDTH*0.5f-1f, 0f, 2f, 5.4f);
		enemies = new Array<Enemy>();
		spawnenemy();
		
		hookSprite.setPosition(hero.hook.position.x, hero.hook.position.y);
		hookSprite.setOrigin(0.2f,0.0f);
		hookSprite.setSize(0.4f, 0.4f);
		
		ropeSprite.setPosition(hero.hook.position.x+0.15f, hero.hook.position.y);
		ropeSprite.setOrigin(0, 0);
		
		
		world = new World(gravity, false);
		
		
		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.setAmbientLight(1);
	
//		new ConeLight(rayHandler, 100, Color.CYAN, 20f, WORLD_WIDTH/2, WORLD_HEIGHT, 270, 35);
		
		hookLight = new PointLight(rayHandler, 50, Color.WHITE, 1f, hero.hook.position.x+0.2f, hero.hook.position.y+0.2f);
		
	}

	
	private void spawnenemy() {
		Enemy enemy = new Enemy(0, 0,1f,1f);
		enemies.add(enemy);
		lastEnemyTime = TimeUtils.nanoTime();
	}


	@Override
	public void render(float delta) {
		
		//SPAWN ENEMY AFTER 1 Second
		  
		  if(TimeUtils.nanoTime() - lastEnemyTime > 1000000000) 	 
	  		{
			  spawnenemy();
			  }
		
		// HANDLING INPUT
		
		  if (Gdx.input.isTouched()) {
			  if (hero.state == Hero.HOOK_COOLDOWN){
			  	hero.throwHook();
	// Check touch position and angle
              Vector3 touchPos = new Vector3();
              touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
              camera.unproject(touchPos);
              Vector2 touchPos2 = new Vector2(touchPos.x, touchPos.y);
              hookAngle = touchPos2.sub(hero.hook.position).angle();
              float ballspeed = 15f;
              			hero.hook.velocity.x = MathUtils.cosDeg(hookAngle) * ballspeed;
              				hero.hook.velocity.y = MathUtils.sinDeg(hookAngle) * ballspeed;
              				
              				hookSprite.setRotation(hookAngle-90);
              				ropeSprite.setRotation(hookAngle-90);
			  }
      }
		  
		  // Change rope size depending where the hook is
		  
		  distancehero = hero.hook.position.dst(hero.position.x, hero.position.y+hero.bounds.height/3)+0.1f;
		  ropeSprite.setSize(0.1f, distancehero);
		  ropeSprite.setV(0);
		  ropeSprite.setV2(distancehero);
		  
		  
		  
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
		  		}
		  }
		  
// RETURN HOOK AFTER time*2 since collition	  
		  
		  if (hero.state == Hero.HOOK_LAUNCHED && (hero.stateTime > hookTime || hero.hook.position.y < 0 )){
			  hero.getHook();
			  hookSprite.setRotation(0);
			  Iterator<Enemy> iter = enemies.iterator();
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
		  if (hero.hook.position.x+0.1f > WORLD_WIDTH || hero.hook.position.x-0.1f < 0 || hero.hook.position.y+0.1f > WORLD_HEIGHT){
			  hookTime = hero.stateTime*2;
			  hero.hook.velocity.x = -hero.hook.velocity.x;
				 hero.hook.velocity.y = -hero.hook.velocity.y;
				 hero.enemyState = Hero.GOTENEMY;
				  Gdx.app.log("hookTime", Float.toString(hookTime));	
		  }
		  
		  // Handle hooked enemies
		  for (Enemy enemy : enemies) {
			  if (enemy.state == Enemy.HOOKED){
			enemy.position.set(hero.hook.position);
					}
		  	}	
		  
		  
		  
		  //Remove enemies that fall under y < 0 or X>width
		  
		  Iterator<Enemy> iter = enemies.iterator();
	      while(iter.hasNext()) {
	         Enemy enemy = iter.next();
	         
	         if(enemy.position.y + 64 < 0 || enemy.isOutOfBounds(WORLD_WIDTH, WORLD_HEIGHT)) 
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
		
		//RENDERING 
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		hgame.batch.setProjectionMatrix(camera.combined);
		
		hgame.batch.begin();
		hgame.batch.draw(backgroundRegion, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
		 for (Enemy enemy : enemies) {
			hgame.batch.draw(candies.get(enemy.candyType), enemy.position.x, enemy.position.y, enemy.bounds.width, enemy.bounds.height);
		 }
		 
		hookSprite.setPosition(hero.hook.position.x, hero.hook.position.y);
		ropeSprite.draw(hgame.batch);
		hookSprite.draw(hgame.batch);
		hgame.batch.draw(heroimage, hero.position.x, hero.position.y, hero.bounds.width, hero.bounds.height);
		hgame.batch.end();
		
		rayHandler.updateAndRender();
		
		
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
heroimg.dispose();
background.dispose();
hookimage.dispose();
hgame.batch.dispose();
rayHandler.dispose();

	}

}
