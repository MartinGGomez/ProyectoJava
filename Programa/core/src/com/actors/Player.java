package com.actors;

import static com.game.MainGame.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends Actor{
	
	public World world;
	public Body body;
	
	private Texture playerTexture;
	private TextureRegion region;

	private Viewport gameport;
	
	
	public Player(World world, Viewport gameport) {
		this.playerTexture = new Texture("player.png");
		this.world = world;
		this.gameport = gameport;
				
		this.region = new TextureRegion(playerTexture, 16, 908, 32, 52); // En el sprite sheet empieza en x = 16 y y = 908. Pj de 32x52
		setWidth(this.region.getRegionWidth());
		setHeight(this.region.getRegionHeight());
	
		setPosition((Gdx.graphics.getWidth()/2) - (getWidth() / 2), (Gdx.graphics.getHeight()/2) - (getHeight() / 2));
		
		definePlayerBody();

	}
	
	public void definePlayerBody() {
		BodyDef bdef = new BodyDef();
		bdef.position.set((Gdx.graphics.getWidth()/2) - (getWidth() / 2), (Gdx.graphics.getHeight()/2) - (getHeight() / 2));
		bdef.position.set(600 / PPM, 600 / PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		
		body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((getWidth() / 2) / PPM, (getHeight() / 2) / PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
	}
	
	@Override
	public void act(float delta) {
		// NO SE HACE ASI. ES UNA PRUEBA
		//
		//
//		if(Gdx.input.isKeyPressed(Keys.W)) {
//			setY(getY()+1);
//		}
//		if(Gdx.input.isKeyPressed(Keys.S)) {
//			setY(getY()-1);
//		}
//		if(Gdx.input.isKeyPressed(Keys.A)) {
//			setX(getX()-1);
//		}
//		if(Gdx.input.isKeyPressed(Keys.D)) {
//			setX(getX()+1);
//		}
		//
		//
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(region, (Gdx.graphics.getWidth()/2) - (getWidth() / 2), (Gdx.graphics.getHeight()/2) - (getHeight() / 2));
	}
	
	
	
}
