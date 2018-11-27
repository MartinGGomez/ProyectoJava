package com.screens;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.MainGame;

public class ScreenCharge implements Screen {

	private MainGame game;
	
	private ScreenMenu menu;
	
	private Texture logo;
	private Sprite sLogo;
	
	private float alpha;
	
	private Color c;
	
	public Music start;
	
	public ScreenCharge(MainGame game  ) {
		this.game = game;
		menu = new ScreenMenu(game);
		start = Gdx.audio.newMusic(Gdx.files.getFileHandle("mp3/101.mp3", FileType.Internal));
		logo = new Texture("logoGame.png");
		sLogo = new Sprite(logo);
	}
	
	@Override
	public void show() {
				
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
	
		update(delta);
		
		game.batch.enableBlending();
		
		
		
		start.play();
		
		if (alpha>3){
			//start.stop();	
		}
	}

	private void update(float delta) {
	
		
		c = new Color(sLogo.getColor());
		
		if (alpha<1){

		game.batch.setColor(c.r,c.a,c.a,alpha);
		alpha+=0.007;
		
		}else{	
			alpha+=0.02;
			
			if (alpha>3){

				game.setScreen(menu);
				
			}
			
		}
		
			game.batch.begin();
			
			game.batch.draw(sLogo, (Gdx.graphics.getWidth()/2) - sLogo.getWidth()/2, (Gdx.graphics.getHeight()/2) - sLogo.getHeight()/2);
			
			game.batch.end();
			
		
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
		start.dispose();		
	}

}
