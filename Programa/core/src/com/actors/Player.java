package com.actors;

import static com.constants.Constants.PPM;
import static com.constants.Constants.SPEED;

import com.actors.states.PlayerStates;
import com.attacks.Attack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.constants.Constants;
import com.game.MainGame;
import com.screens.GameScreen;
import com.screens.Hud;
import com.services.collision.userdata.UserData;

public class Player extends Sprite {

	// Player properties
	public int health = 100;
	public int maxHealth = 100;
	public int mana = 700;
	public int maxMana = 700;
	public int energy = 500;
	public int maxEnergy = 500;
	public int minArmorDef = 20;
	public int maxArmorDef = 25;
	public int minHelmetDef = 10;
	public int maxHelmetDef = 15;
	public int minShieldDef = 2;
	public int maxShieldDef = 5;
	public int minAttackDamage = 50;
	public int maxAttackDamage = 70;
	
	public boolean isBeingAttacked = false;
	public Attack attack;
	public Attack selectedAttack;
	
	
	public World world;
	public Body body;
	private MainGame game;

	private Texture playerTexture;
	private TextureRegion region;
	
	
	
	// Scene2d
	private Label playerLabel;
	public Actor actor;
	public String name;

	// Animations
	public PlayerStates states;
	public PlayerStates direction;
	private Animation<TextureRegion> movingRight;
	private Animation<TextureRegion> movingLeft;
	private Animation<TextureRegion> movingBack;
	private Animation<TextureRegion> movingFront;
	private TextureRegion[] standingTextures;
	private float stateTimer;
	private PlayerStates currentState;
	private PlayerStates previousState;
	//
	
	public Player(MainGame game, World world, String name) {
		this.playerTexture = new Texture("player.png");
		this.world = world;
		this.game = game;
		this.name = name;
		
		this.region = new TextureRegion(playerTexture, 0, 0, 32, 48); // En el sprite sheet empieza en x = 16 y y = 908.
																		// Pj de 32x52
		definePlayerBody();
		createAnimations();

		setBounds(body.getPosition().x, body.getPosition().y, 32 / PPM, 48 / PPM);
		setRegion(standingTextures[0]);
			
	}

	public void definePlayerBody() {

		BodyDef bdef = new BodyDef();
		bdef.position.set(Hud.HUD_HALF_WIDTH / PPM, Hud.HUD_HALF_HEIGHT / PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;

		body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.region.getRegionWidth() / 2) / PPM, (this.region.getRegionHeight() / 4) / PPM);
		fdef.filter.categoryBits = Constants.BIT_PLAYER;
		fdef.filter.maskBits = Constants.BIT_COLLISION | Constants.BIT_PLAYER;
		fdef.shape = shape;

		UserData userData = new UserData("Player", 1);

		body.createFixture(fdef).setUserData(userData);

	}
	
	public void defineStageElements() {
		// SCENE2D STAGE
		playerLabel = new Label(this.name, GameScreen.hud.skin, "little-font", Color.WHITE);
		playerLabel.setPosition((body.getPosition().x * PPM) - (this.region.getRegionWidth() / 2) - 22 , (body.getPosition().y * PPM)- (this.region.getRegionHeight() / 2) - 6);
		playerLabel.setSize(80, 12);
		playerLabel.setAlignment(Align.center);
		this.game.stage.addActor(playerLabel);
		
//		actor = new Actor();
//		actor.setSize(getRegionWidth(), getRegionHeight());
//		actor.setPosition((this.body.getPosition().x * PPM) - 12, (this.body.getPosition().y * PPM)-17);
//		actor.addListener(new ClickListener() {
//			 @Override
//			    public void clicked(InputEvent event, float x, float y) {
//			        GameScreen.hud.printMessage(name);
//			 }
//		});
//		this.game.stage.addActor(actor);	
	}

	public void update(float delta) {

		setPosition(body.getPosition().x - (this.region.getRegionWidth() / 2) / PPM,
				body.getPosition().y - (this.region.getRegionHeight() / 4) / PPM);
		

		body.setLinearVelocity(0, 0);
		if (Gdx.input.isKeyPressed(Keys.W)) {
			body.setLinearVelocity(new Vector2(0, SPEED));
			states = PlayerStates.BACK;
			direction = PlayerStates.BACK;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			body.setLinearVelocity(new Vector2(0, -SPEED));
			states = PlayerStates.FRONT;
			direction = PlayerStates.FRONT;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			body.setLinearVelocity(new Vector2(-SPEED, 0));
			states = PlayerStates.LEFT;
			direction = PlayerStates.LEFT;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			body.setLinearVelocity(new Vector2(SPEED, 0));
			states = PlayerStates.RIGHT;
			direction = PlayerStates.RIGHT;
		}
		setRegion(getFrame(delta));
	}

	public TextureRegion getFrame(float delta) {
		currentState = getState();

		TextureRegion textureRegion;

 		switch (currentState) {
		case FRONT:
			textureRegion = movingFront.getKeyFrame(stateTimer, true);
			break;
		case BACK:
			textureRegion = movingBack.getKeyFrame(stateTimer, true);
			break;
		case RIGHT:
			textureRegion = movingRight.getKeyFrame(stateTimer, true);
			break;
		case LEFT:
			textureRegion = movingLeft.getKeyFrame(stateTimer, true);
			break;
		case STANDING_BACK:
			textureRegion = standingTextures[0];
			break;
		case STANDING_FRONT:
			textureRegion = standingTextures[1];
			break;
		case STANDING_RIGHT:
			textureRegion = standingTextures[2];
			break;
		case STANDING_LEFT:
			textureRegion = standingTextures[3];
			break;
		default:
			textureRegion = movingFront.getKeyFrame(stateTimer, true);
			break;
		}

		stateTimer = currentState == previousState ? stateTimer + delta : 0;

		previousState = currentState;

		return textureRegion;
	}

	public PlayerStates getState() {
		if (body.getLinearVelocity().x > 0 || direction.equals(PlayerStates.RIGHT)) {
			return body.getLinearVelocity().x == 0 ? PlayerStates.STANDING_RIGHT : PlayerStates.RIGHT;
		} else if (body.getLinearVelocity().x < 0 || direction.equals(PlayerStates.LEFT)) {
			return body.getLinearVelocity().x == 0 ? PlayerStates.STANDING_LEFT : PlayerStates.LEFT;
		} else if (body.getLinearVelocity().y > 0 || direction.equals(PlayerStates.BACK)) {
			return body.getLinearVelocity().y == 0 ? PlayerStates.STANDING_BACK : PlayerStates.BACK;
		} else if (body.getLinearVelocity().y < 0 || direction.equals(PlayerStates.FRONT)) {
			return body.getLinearVelocity().y == 0 ? PlayerStates.STANDING_FRONT : PlayerStates.FRONT;
		}
		return PlayerStates.FRONT;
	}

	public void createAnimations() {
		// Animation
		direction = PlayerStates.FRONT;
		currentState = PlayerStates.FRONT;
		previousState = PlayerStates.FRONT;
		stateTimer = 0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(this.playerTexture, i * 64, 0, 32, 48));
		}
		movingBack = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(this.playerTexture, i * 64, 63, 32, 48));
		}
		movingLeft = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(this.playerTexture, i * 64, 125, 32, 48));

		}
		movingFront = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(this.playerTexture, i * 64, 191, 32, 48));
		}
		movingRight = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();

		standingTextures = new TextureRegion[4];
		standingTextures[0] = new TextureRegion(this.playerTexture, 0, 0, 32, 48); // STANDING_BACK
		standingTextures[1] = new TextureRegion(this.playerTexture, 0, 125, 32, 48); // STANDING_FRONT
		standingTextures[2] = new TextureRegion(this.playerTexture, 0, 191, 32, 48); // STANDING_RIGHT
		standingTextures[3] = new TextureRegion(this.playerTexture, 0, 63, 32, 48); // STANDING_LEFT

	}

	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}

	public void dispose() {
		playerTexture.dispose();
		world.destroyBody(body);
	}

	public void attack(Enemy enemy, float delta) {
		GameScreen.hud.printMessage("Atacaste a " + enemy.name);
		mana -= 10;
		GameScreen.hud.updateStats(this);
		enemy.attack = new Attack();
		enemy.isBeingAttacked = true;
//		enemy.health -= 1;
	}

}
