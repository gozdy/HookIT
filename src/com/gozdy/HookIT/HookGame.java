package com.gozdy.HookIT;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HookGame extends Game {
	SpriteBatch batch;
	public BitmapFont font;
	
	
	
	
	@Override
	public void create() {		
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		
		
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fontjuego.fnt"), Gdx.files.internal("fontjuego.png"), false);
		this.setScreen(new MainMenuScreen(this));
//		
//		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		
//		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
//		
//		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		
		// LOAD ASSETES
		
		
	}

	@Override
	public void dispose() {
	batch.dispose();
	font.dispose();
		
	}

	@Override
	public void render() {		
	super.render();
//		
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		sprite.draw(batch);
//		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
