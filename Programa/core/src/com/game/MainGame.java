package com.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.screens.GameScreen;




public class MainGame extends Game {
	
	public GameScreen gameScreen;
	public SpriteBatch batch;
	public AssetManager assetManager;
	public BitmapFont font;
	
	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
		assetManager.finishLoading();
		createFont();
		batch = new SpriteBatch();
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);

	}

	private void createFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/KeepCalm.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 28;
        params.color = Color.RED;
        font = generator.generateFont(params);
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
