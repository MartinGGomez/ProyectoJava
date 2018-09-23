package com.actors;

import static com.game.MainGame.PPM;
import static com.game.MainGame.SPEED;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Sprite{

	private World world;
	private Texture enemyTexture;
	private TextureRegion region;
	private Body body;
	private OrthographicCamera camera;
	
	private float posX, posY;
	
	public Enemy(World world, float posX, float posY) {
		this.world = world;
		this.enemyTexture = new Texture("goblin.png");
		this.posX = posX;
		this.posY = posY;
		
		this.region = new TextureRegion(enemyTexture, 18, 0, 29, 55);
		
		defineEnemyBody();
		
		setBounds(body.getPosition().x , body.getPosition().y, 29 / PPM, 55 / PPM);
		setRegion(enemyTexture);

		
	}
	
	
	public void defineEnemyBody() {
		BodyDef bdef = new BodyDef();
		System.out.println("Position: " + this.posX + " x " + this.posY);
		bdef.position.set(this.posX, this.posY);

		bdef.type = BodyDef.BodyType.DynamicBody;
		
		body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.region.getRegionWidth() / 2) / PPM, (this.region.getRegionHeight() / 2) / PPM);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("Enemigo");
	}
	public void update(float delta) {
		setPosition(body.getPosition().x - (this.region.getRegionWidth() / 2) / PPM ,
				body.getPosition().y - (this.region.getRegionHeight() / 2) / PPM);
		
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
		setRegion(this.region);
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);				 // SE PUEDE PONER UN IF
	}									// if (1==2) { no se dibuja } => Respawn??
 
	public void dispose() {
		enemyTexture.dispose();
	}
	
	
}
