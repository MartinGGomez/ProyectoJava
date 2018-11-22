package com.actors;

import com.actors.states.PlayerStates;
import com.attacks.Attack;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.game.MainGame;

public class Character extends Sprite{
	
	public String name;
	public int health;
	public int mana;
	public int energy;
	
	public boolean alive = true;
	
	public boolean isBeingAttacked = false;
	public Attack attack;
	public Attack selectedAttack;
	public boolean doingAttack = false;
	public Character attackedBy;
	protected int attackDamage;
	
	public World world;
	public Body body;
	protected MainGame game;

	protected Texture texture;
	protected TextureRegion region;
	
	// Animations
	public PlayerStates states;
	public PlayerStates direction;
	protected Animation<TextureRegion> movingRight;
	protected Animation<TextureRegion> movingLeft;
	protected Animation<TextureRegion> movingBack;
	protected Animation<TextureRegion> movingFront;
	protected TextureRegion[] standingTextures;
	protected float stateTimer;
	protected PlayerStates currentState;
	protected PlayerStates previousState;
	//
	
	public Character(MainGame game, World world, String name) {
		this.world = world;
		this.game = game;
		this.name = name;
	}

	public void update(float delta) {
		if(isBeingAttacked) {
			attack.begin(this, this.attackedBy);
		}
		if(health <= 0) {
			alive = false;
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

	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}

	public void dispose() {
		texture.dispose();
		world.destroyBody(body);
	}

	public void attack(Enemy enemy, float delta) {
		
	}

}

	
