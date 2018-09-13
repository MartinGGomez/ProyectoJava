package com.game;

import com.badlogic.gdx.Game;
import com.screens.BaseScreen;
import com.screens.GameScreen;
import com.screens.MiPantalla;


public class MainGame extends Game {

	public BaseScreen gameScreen;
	public MiPantalla screen;
	@Override
	public void create() {
		gameScreen = new GameScreen(this);
		screen = new MiPantalla(this);
		setScreen(screen);
				
	
	}

	@Override
	public void dispose() {

	}
	
	
}
