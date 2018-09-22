package com.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.screens.BaseScreen;
import com.screens.GameScreen;


public class MainGame extends Game {

	public static final float PPM = 100; // Pixels per meter. Box2d Scale
	public static final float SPEED = 1.2f; // Player speed;
	
	public GameScreen gameScreen;
	public SpriteBatch batch;
	
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
				
	
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}
	
	@Override
	public void render() {
		super.render();
	}
}
