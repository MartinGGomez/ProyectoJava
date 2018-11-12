package com.actors;

import static com.constants.Constants.PPM;
import static com.constants.Constants.SPEED;

import com.actors.states.PlayerStates;
import com.ai.SteeringEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.constants.Constants;
import com.game.MainGame;
import com.screens.GameScreen;
import com.services.collision.userdata.UserData;

public class Enemy extends Character {

	public static String name = "Monstruo";
	public int health = 100;

	private float posX, posY;
	private int enemyIndex;

	public boolean preventMove;

	public Label enemyLabel;
	
	public SteeringEntity steeringEntity;

	public Enemy(MainGame game, World world, float posX, float posY, int enemyIndex) {
		super(game, world, name);
		super.texture = new Texture("monster.png");
		super.region = new TextureRegion(super.texture, 18, 0, 29, 55);
		
		this.posX = posX;
		this.posY = posY;
		this.enemyIndex = enemyIndex;
		

		defineEnemyBody();
		createAnimations();
		
		setBounds(body.getPosition().x, body.getPosition().y, 29 / PPM, 55 / PPM);
		setRegion(standingTextures[0]);
		
		steeringEntity = new SteeringEntity(this.body, 1f);
		
		Arrive<Vector2> arriveSB = new Arrive<Vector2>(steeringEntity, GameScreen.player.steeringEntity)
				.setTimeToTarget(0.1f)
				.setArrivalTolerance(0.5f)
				.setDecelerationRadius(0);
		
		steeringEntity.setBehaviour(arriveSB);

	}

	public int getEnemyIndex() {
		return enemyIndex;
	}

	public void defineEnemyBody() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(this.posX, this.posY);
		bdef.type = BodyDef.BodyType.DynamicBody;

		super.body = super.world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.region.getRegionWidth() / 2) / PPM, (this.region.getRegionHeight() / 4) / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = Constants.BIT_PLAYER;
		fdef.filter.maskBits = Constants.BIT_COLLISION | Constants.BIT_PLAYER;
		
		UserData userData = new UserData("Enemy", enemyIndex);
		

		super.body.createFixture(fdef).setUserData(userData);
	}

	public void update(float delta) {
		super.update(delta);
		steeringEntity.update(delta);

		if (preventMove) {
			MassData mass = new MassData();
			mass.mass = 100000;
			body.setMassData(mass);
		} else {
			body.resetMassData();
		}

		setPosition(body.getPosition().x - (this.region.getRegionWidth() / 2) / PPM,
				body.getPosition().y - (this.region.getRegionHeight() / 4) / PPM);

		body.setLinearVelocity(0, 0);
		if (Gdx.input.isKeyPressed(Keys.UP) || body.getLinearVelocity().y > 0) {
			body.setLinearVelocity(new Vector2(0, (float) (SPEED*0.55)));
			 states = PlayerStates.BACK;
			 direction = PlayerStates.BACK;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN) || body.getLinearVelocity().y < 0) {
			body.setLinearVelocity(new Vector2(0, -(float) (SPEED*0.55)));
			 states = PlayerStates.FRONT;
			 direction = PlayerStates.FRONT;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT) || body.getLinearVelocity().x < 0) {
			body.setLinearVelocity(new Vector2(-(float) (SPEED*0.55), 0));
			 states = PlayerStates.LEFT;
			 direction = PlayerStates.LEFT;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT) || body.getLinearVelocity().x > 0) {
			body.setLinearVelocity(new Vector2((float) (SPEED*0.55), 0));
			 states = PlayerStates.RIGHT;
			 direction = PlayerStates.RIGHT;
		}
		super.setRegion(getFrame(delta));

	}

	@Override
	public void draw(Batch batch) {
		super.draw(batch); // SE PUEDE PONER UN IF
	} // if (1==2) { no se dibuja } => Respawn??

	public void dispose() {
		world.destroyBody(body);
	}

	
	public void createAnimations() {
		// Animation
		direction = PlayerStates.FRONT;
		currentState = PlayerStates.FRONT;
		previousState = PlayerStates.FRONT;
		stateTimer = 0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		frames.add(new TextureRegion(super.texture, 292, 291, 44, 91));
		frames.add(new TextureRegion(super.texture, 337, 291, 44, 91));
		frames.add(new TextureRegion(super.texture, 384, 291, 44, 91));
		movingBack = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();
		frames.add(new TextureRegion(super.texture, 288, 99, 44, 91));
		frames.add(new TextureRegion(super.texture, 348, 99, 44, 91));
		frames.add(new TextureRegion(super.texture, 384, 99, 44, 91));
		movingLeft = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();
		frames.add(new TextureRegion(super.texture, 290, 3, 46, 91));
		frames.add(new TextureRegion(super.texture, 337, 3, 46, 91));
		frames.add(new TextureRegion(super.texture, 384, 3, 46, 91));
		movingFront = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();
		frames.add(new TextureRegion(super.texture, 295, 195, 46, 91));
		frames.add(new TextureRegion(super.texture, 345, 195, 46, 91));
		frames.add(new TextureRegion(super.texture, 387, 195, 46, 91));
		movingRight = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		standingTextures = new TextureRegion[4];
		standingTextures[0] = new TextureRegion(super.texture, 292, 291, 44, 91); // STANDING_BACK
		standingTextures[1] = new TextureRegion(super.texture, 290, 3, 46, 91); // STANDING_FRONT
		standingTextures[2] = new TextureRegion(super.texture, 295, 195, 46, 91); // STANDING_RIGHT
		standingTextures[3] = new TextureRegion(super.texture, 288, 99, 44, 91); // STANDING_LEFT

	}	
	
}
