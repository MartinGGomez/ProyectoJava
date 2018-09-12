package com.game;

import com.badlogic.gdx.Game;
import com.screens.BaseScreen;
import com.screens.GameScreen;


public class MainGame extends Game {

	public BaseScreen gameScreen;
	@Override
	public void create() {
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
				
	
	}

	@Override
	public void dispose() {

	}
	
	
}
