package com.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.screens.GameScreen;
import com.screens.ScreenCharge;

public class MainGame extends Game {
	
	public GameScreen gameScreen;
	public SpriteBatch batch;
	public AssetManager assetManager;
	public Stage stage;
	
	public ScreenCharge screenCharge;
	
	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
		assetManager.finishLoading();
		batch = new SpriteBatch();
		gameScreen = new GameScreen(this);
		screenCharge = new ScreenCharge(this);
		
	//setScreen(screenCharge);
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
