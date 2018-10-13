package com.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.screens.BaseScreen;
import com.screens.GameScreen;


public class MainGame extends Game {
	
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
