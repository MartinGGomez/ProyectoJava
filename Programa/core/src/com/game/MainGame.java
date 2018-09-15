package com.game;

import com.badlogic.gdx.Game;
import com.screens.BaseScreen;
import com.screens.GameScreen;


public class MainGame extends Game {

	public static final float PPM = 100; // Pixels per meter. Box2d Scale
	public static final float SPEED = 1.2f; // Player speed;
	
	public BaseScreen gameScreen;
	
	
	@Override
	public void create() {
		gameScreen = new GameScreen(this);
//		screen = new MiPantalla(this);
		setScreen(gameScreen);
				
	
	}

	@Override
	public void dispose() {

	}
	
	
}
