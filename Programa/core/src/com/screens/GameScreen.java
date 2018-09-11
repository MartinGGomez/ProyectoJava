package com.screens;

import com.actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.MainGame;

public class GameScreen extends BaseScreen{

	private Stage stage;
	private Player player;
	private Texture playerTexture;
	
	public GameScreen (MainGame game) {
		super(game);
		playerTexture = new Texture("player.png");
	}
	
	@Override
	public void show() {
		stage = new Stage();
		player = new Player(playerTexture);
		
		stage.addActor(player);
		player.setPosition(50, 100);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.4f, 0.6f, 0.2f, 1f);
		stage.act();
		stage.draw();
		
	}
	
	@Override
	public void hide() {
		stage.dispose();
		playerTexture.dispose();
	}
}
