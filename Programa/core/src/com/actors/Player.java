package com.actors;

import static com.constants.Constants.PPM;
import static com.constants.Constants.SPEED;

import com.actors.states.PlayerStates;
import com.attacks.Attack;
import com.attacks.BasicAttack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.constants.Constants;
import com.constants.MessageType;
import com.game.MainGame;
import com.screens.GameScreen;
import com.screens.Hud;
import com.services.collision.userdata.UserData;

public class Player extends Character {

	// Player properties
	// Health is in Character class.
	public int maxHealth = 200;
	public int maxMana = 400;
	public int maxEnergy = 100;
	public int minArmorDef = 20;
	public int maxArmorDef = 25;
	public int minHelmetDef = 10;
	public int maxHelmetDef = 15;
	public int minShieldDef = 2;
	public int maxShieldDef = 5;
	public int minAttackDamage = 50;
	public int maxAttackDamage = 70;

	// Scene2d
	private Label playerLabel;
	public Actor actor;

	private float time = 0f;

	public Player(MainGame game, World world, String name) {
		super(game, world, name);

		super.texture = new Texture("player.png");
		super.region = new TextureRegion(super.texture, 0, 0, 32, 48); // En el sprite sheet empieza en x = 16 y y =
																		// 908.
		super.attackDamage = 50;
		super.health = 200;
		super.mana = 400;
		super.energy = 100;

		definePlayerBody();
		createAnimations();

		setBounds(body.getPosition().x, body.getPosition().y, 32 / PPM, 48 / PPM);
		setRegion(standingTextures[0]);

	}

	public void definePlayerBody() {

		BodyDef bdef = new BodyDef();
		bdef.position.set(Hud.HUD_HALF_WIDTH / PPM, Hud.HUD_HALF_HEIGHT / PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;

		super.body = super.world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.region.getRegionWidth() / 2) / PPM, (this.region.getRegionHeight() / 4) / PPM);
		fdef.filter.categoryBits = Constants.BIT_PLAYER;
		fdef.filter.maskBits = Constants.BIT_COLLISION | Constants.BIT_PLAYER;
		fdef.shape = shape;

		UserData userData = new UserData("Player", 1, false);

		super.body.createFixture(fdef).setUserData(userData);

	}

	public void defineStageElements() {
		// SCENE2D STAGE
		playerLabel = new Label(this.name, Hud.skin, "little-font", Color.WHITE);
		playerLabel.setPosition((body.getPosition().x * PPM) - (this.region.getRegionWidth() / 2) - 22,
				(body.getPosition().y * PPM) - (this.region.getRegionHeight() / 2) - 6);
		playerLabel.setSize(80, 12);
		playerLabel.setAlignment(Align.center);
		this.game.stage.addActor(playerLabel);
	}

	public void update(float delta) {
		super.update(delta);
		if (this.energy < this.maxEnergy) {
			time += delta;
			if (time > 1f) {
				this.energy += 10;
				GameScreen.hud.updateStats(this);
				time = 0f;
			}

		}

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
		super.setRegion(getFrame(delta));
	}

	public void createAnimations() {
		// Animation
		direction = PlayerStates.FRONT;
		currentState = PlayerStates.FRONT;
		previousState = PlayerStates.FRONT;
		stateTimer = 0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(super.texture, i * 64, 0, 32, 48));
		}
		movingBack = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(super.texture, i * 64, 63, 32, 48));
		}
		movingLeft = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(super.texture, i * 64, 125, 32, 48));

		}
		movingFront = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(super.texture, i * 64, 191, 32, 48));
		}
		movingRight = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();

		standingTextures = new TextureRegion[4];
		standingTextures[0] = new TextureRegion(super.texture, 0, 0, 32, 48); // STANDING_BACK
		standingTextures[1] = new TextureRegion(super.texture, 0, 125, 32, 48); // STANDING_FRONT
		standingTextures[2] = new TextureRegion(super.texture, 0, 191, 32, 48); // STANDING_RIGHT
		standingTextures[3] = new TextureRegion(super.texture, 0, 63, 32, 48); // STANDING_LEFT

	}

	@Override
	public void draw(Batch batch) {
		if (alive) {
			super.draw(batch);
		}

	}

	public void dispose() {
		world.destroyBody(body);
	}

	public void attack(Enemy enemy, float delta) {

		if (enemy.alive){
		// Esto es para hechizos:
		enemy.attack = new BasicAttack();
		enemy.isBeingAttacked = true;
		enemy.attackedBy = this;
		this.doingAttack = true;
		Hud.printMessage("Le has causado " + enemy.attack.damage + " a " + enemy.name + " con " + enemy.attack.name,
				MessageType.COMBAT);
		GameScreen.hud.updateStats(this);
		}else{
			Hud.printMessage("Enemigo Muerto", MessageType.COMBAT);
		}
		
	}

}
