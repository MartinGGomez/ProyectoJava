package com.actors;

import static com.game.MainGame.PPM;
import static com.game.MainGame.SPEED;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Enemy extends Actor{

	private World world;
	private Texture enemyTexture;
	private TextureRegion region;
	private Body body;
	private OrthographicCamera camera;
	
	public Enemy(World world, float posX, float posY) {
		this.world = world;
		this.camera = camera;
		this.enemyTexture = new Texture("goblin.png");
		
		this.region = new TextureRegion(enemyTexture, 18, 0, 29, 55);
		
		setWidth(this.region.getRegionWidth());
		setHeight(this.region.getRegionHeight());
		
		setPosition(posX + (getWidth() / 2) / PPM, posY + (getHeight() / 2) / PPM);
		
		defineEnemyBody();

	}
	
	
	public void defineEnemyBody() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());

		bdef.type = BodyDef.BodyType.DynamicBody;
		
		body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((getWidth() / 2) / PPM, (getHeight() / 2) / PPM);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("Enemigo");
	}
	@Override
	public void act(float delta) {
		body.setLinearVelocity(0, 0);
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			body.setLinearVelocity(new Vector2(0, SPEED));
//			states = PlayerStates.BACK;
//			direction = PlayerStates.BACK;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			body.setLinearVelocity(new Vector2(0, -SPEED));
//			states = PlayerStates.FRONT;
//			direction = PlayerStates.FRONT;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			body.setLinearVelocity(new Vector2(-SPEED, 0));
//			states = PlayerStates.LEFT;
//			direction = PlayerStates.LEFT;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			body.setLinearVelocity(new Vector2(SPEED, 0));
//			states = PlayerStates.RIGHT;
//			direction = PlayerStates.RIGHT;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(region, body.getPosition().x / PPM , body.getPosition().y / PPM);
	}
 
	public void dispose() {
		enemyTexture.dispose();
	}
	
	
}
