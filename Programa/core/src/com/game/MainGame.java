package com.game;

import com.badlogic.gdx.Game;
import com.screens.BaseScreen;
import com.screens.MiPantalla;
import com.screens.TestScreen;


public class MainGame extends Game {

	public static final float PPM = 100; // Pixels per meter. Box2d Scale
	public static final float SPEED = 2f; // Player speed;
	
	public BaseScreen gameScreen;
	public MiPantalla screen;
	@Override
	public void create() {
//		gameScreen = new GameScreen(this);
//		screen = new MiPantalla(this);
		TestScreen screen = new TestScreen(this);
		setScreen(screen);
				
	
	}

	@Override
	public void dispose() {

	}
	
	
}
