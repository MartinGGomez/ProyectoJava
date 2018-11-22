package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.MainGame;

public class ScreenMenu implements Screen, InputProcessor{

	 private MainGame game;

	
	 private Texture menu;
	
	 private ProgressBar barMenu;
	 
	 private int cont =0;
	 
	
	public ScreenMenu(MainGame game) {
		
		Gdx.input.setInputProcessor(this);
		this.game = game;
		menu = new Texture("menuPrincipal.png");
		

	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
	
		update(delta);		
		
		
		
		//System.out.println(cont);
		
	}

	private void update(float delta) {
//		cont++;
//		if (cont==120){
//			game.setScreen(game.gameScreen);
//		}
//		
		game.batch.begin();
		game.batch.draw(menu, 0, 0, 800, 600);
		game.batch.end();
		
	}
	

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		System.out.println(screenX);
		System.out.println(screenY);
		
		if (((screenX > 724) && (screenY > 530 )) && ((screenX < 765) && (screenY < 585))){
			game.setScreen(game.gameScreen);
		}
		
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	

}
