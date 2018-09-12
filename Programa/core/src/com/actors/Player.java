package com.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor{
	
	private Texture playerTexture;
	
	private TextureRegion region;

	public Player(Texture texture) {
		this.playerTexture = texture;
		
		this.region = new TextureRegion(playerTexture, 16, 908, 32, 52); // En el sprite sheet empieza en x = 16 y y = 908. Pj de 32x52
		setWidth(this.region.getRegionWidth());
		setHeight(this.region.getRegionHeight());
	}
	
	@Override
	public void act(float delta) {
		// NO SE HACE ASI. ES UNA PRUEBA
		//
		//
		if(Gdx.input.isKeyPressed(Keys.W)) {
			setY(getY()+0.5f);
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			setY(getY()-0.5f);
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			setX(getX()-0.5f);
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			setX(getX()+0.5f);
		}
		//
		//
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(region, getX(), getY());
	}
	
	
	
}
