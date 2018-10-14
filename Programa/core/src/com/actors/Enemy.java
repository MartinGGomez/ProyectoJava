package com.actors;

import static com.constants.Constants.PPM;
import static com.constants.Constants.SPEED;

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
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.constants.Constants;
import com.services.collision.userdata.UserData;

public class Enemy extends Sprite {

	public int health = 100;

	//
	private World world;
	private Texture enemyTexture;
	private TextureRegion region;
	public Body body;
	private OrthographicCamera camera;

	private float posX, posY;
	private int enemyIndex;

	public boolean preventMove;

	public Enemy(World world, float posX, float posY, int enemyIndex) {
		this.world = world;
		this.enemyTexture = new Texture("goblin.png");
		this.posX = posX;
		this.posY = posY;
		this.enemyIndex = enemyIndex;

		this.region = new TextureRegion(enemyTexture, 18, 0, 29, 55);

		defineEnemyBody();

		setBounds(body.getPosition().x, body.getPosition().y, 29 / PPM, 55 / PPM);
		setRegion(enemyTexture);

	}

	public int getEnemyIndex() {
		return enemyIndex;
	}

	public void defineEnemyBody() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(this.posX, this.posY);

		bdef.type = BodyDef.BodyType.DynamicBody;
//		MassData mass = new MassData();
//		mass.mass = 100000;
		body = world.createBody(bdef);
//		body.setMassData(mass);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.region.getRegionWidth() / 2) / PPM, (this.region.getRegionHeight() / 4) / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = Constants.BIT_PLAYER;
		fdef.filter.maskBits = Constants.BIT_COLLISION | Constants.BIT_PLAYER;

		UserData userData = new UserData("Enemy", enemyIndex);

		body.createFixture(fdef).setUserData(userData);
	}

	public void update(float delta) {
		if(preventMove) {
			MassData mass = new MassData();
			mass.mass = 100000;
			body.setMassData(mass);
		} else {
			body.resetMassData();
		}
		
		setPosition(body.getPosition().x - (this.region.getRegionWidth() / 2) / PPM,
				body.getPosition().y - (this.region.getRegionHeight() / 4) / PPM);

		body.setLinearVelocity(0, 0);
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			body.setLinearVelocity(new Vector2(0, SPEED));
			// states = PlayerStates.BACK;
			// direction = PlayerStates.BACK;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			body.setLinearVelocity(new Vector2(0, -SPEED));
			// states = PlayerStates.FRONT;
			// direction = PlayerStates.FRONT;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			body.setLinearVelocity(new Vector2(-SPEED, 0));
			// states = PlayerStates.LEFT;
			// direction = PlayerStates.LEFT;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			body.setLinearVelocity(new Vector2(SPEED, 0));
			// states = PlayerStates.RIGHT;
			// direction = PlayerStates.RIGHT;
		}
		setRegion(this.region);

	}

	@Override
	public void draw(Batch batch) {
		super.draw(batch); // SE PUEDE PONER UN IF
	} // if (1==2) { no se dibuja } => Respawn??

	public void dispose() {
		enemyTexture.dispose();
		world.destroyBody(body);
	}

}