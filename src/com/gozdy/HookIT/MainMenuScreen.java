package com.gozdy.HookIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class MainMenuScreen implements Screen {

	 public static final float WORLD_WIDTH = 10;
	    public static final float WORLD_HEIGHT = 15;
	final HookGame game;
	OrthographicCamera camera;

	public MainMenuScreen(final HookGame hookGame) {
			game = hookGame;
			
			camera = new OrthographicCamera();
			camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			game.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			game.font.setScale(3f, 3f);
			
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		
		
		game.batch.begin();
//		game.font.scale(5);
		game.font.draw(game.batch, "HOOKGAMEEEE", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		
		
		game.batch.end();
		
		if(Gdx.input.isTouched()){
			game.setScreen(new GameScreen(game));
			dispose();
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

	}

}
