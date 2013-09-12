package com.gozdy.HookIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Assets {
	
	 public static TextureRegion backgroundRegion;
	 public static TextureRegion heroimage;
	 public static TextureRegion ropeRegion;

	 public static Texture background;
	 public static Texture heroimg;
	 public static Texture ropeImg;
	 public static Texture hookimage;
	 public static Texture enemyimage;
	 public static Texture candy;
	 public static Texture playImage;
	 public static Texture numbers;
	
	 public static Sprite ropeSprite;
	 public static Sprite hookSprite;
	 public static Sprite playSprite;
	 
	 public static Array<TextureRegion> candies;
	 public static Array<TextureRegion> number;

	public static void load() 
	{
		background = new Texture(Gdx.files.internal("backgroundHookIT.png"));
		background.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
        
		hookimage = new Texture(Gdx.files.internal("HOOKRotado.png"));
		heroimg = new Texture(Gdx.files.internal("chef.png"));
		heroimage = new TextureRegion(heroimg, 75, 0, 95, 256);
		hookSprite = new Sprite(hookimage);
		
		candy = new Texture(Gdx.files.internal("candies.png"));
		candies = new Array<TextureRegion>();
		candies.add(new TextureRegion(candy, 0, 0, 64, 64)); //Rueda Azul
		candies.add(new TextureRegion(candy, 0, 64, 64, 64)); // Gotita de chocolate
		candies.add(new TextureRegion(candy, 64, 0, 64, 64)); // Paleta
		candies.add(new TextureRegion(candy, 64, 64, 64, 64)); // Caramelo violeta
		
		numbers = new Texture(Gdx.files.internal("numbers.png"));
		number = new Array<TextureRegion>();
		number.add(new TextureRegion(numbers, 0, 0, 64, 64)); //0
		number.add(new TextureRegion(numbers, 64, 0, 64, 64)); // 1
		number.add(new TextureRegion(numbers, 128, 0, 64, 64)); // 2
		number.add(new TextureRegion(numbers, 192, 0, 64, 64)); // 3
		number.add(new TextureRegion(numbers, 0, 64, 64, 64)); //4
		number.add(new TextureRegion(numbers, 64, 64, 64, 64)); // 5
		number.add(new TextureRegion(numbers, 128, 64, 64, 64)); // 6
		number.add(new TextureRegion(numbers, 192, 64, 64, 64)); // 7		
		
		ropeImg = new Texture(Gdx.files.internal("ropeVertical64.png"));
		ropeImg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		ropeRegion = new TextureRegion(ropeImg, 0, 0, 5, 5);
		ropeSprite = new Sprite(ropeRegion);
		
		playImage = new Texture(Gdx.files.internal("playGame.png"));
		playSprite = new Sprite(playImage);
	}

}
