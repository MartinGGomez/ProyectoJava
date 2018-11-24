package com.actors;

import static com.constants.Constants.PPM;

import java.util.Random;

import com.actors.states.PlayerStates;
import com.attacks.Attack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.constants.MessageType;
import com.game.MainGame;
import com.screens.GameScreen;
import com.screens.Hud;

public abstract class Character extends Sprite {

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

	// COFRE DE DROP
	public boolean isChest = false;
	public boolean open = false;

	private Texture textureChest;
	private TextureRegion regionOpen;
	private TextureRegion regionClose;

	private Random random = new Random();
	
	private boolean rewarded = false;

	public Character(MainGame game, World world, String name) {
		this.world = world;
		this.game = game;
		this.name = name;

		this.textureChest = new Texture(Gdx.files.internal("chest.png"));
		this.regionOpen = new TextureRegion(this.textureChest, 33, 0, 30, 32);
		this.regionClose = new TextureRegion(this.textureChest, 0, 0, 30, 32);
	}

	public void update(float delta) {
		if (alive) {
			if (isBeingAttacked) {
				attack.begin(this, this.attackedBy);
			}
			if (health <= 0) {
				alive = false;
			}
			setRegion(getFrame(delta));
		} else {// ES UN COFRE
			if(!rewarded) {
				Player player = (Player) this.attackedBy;
				player.money += 100; 
				player.exp += 10f;
				GameScreen.hud.updateStats(player);
				Hud.printMessage("Ganaste 10% de experiencia y 100 monedas de oro", MessageType.REWARD);
				rewarded = true;
			}
			
			this.isChest = true;
			setBounds(body.getPosition().x - (this.regionClose.getRegionWidth() / 2) / PPM,
					body.getPosition().y - (this.regionClose.getRegionWidth() / 2) / PPM, 30 / PPM, 32 / PPM);
			if (open) {
				setRegion(this.regionOpen);
			} else {
				setRegion(this.regionClose);
			}
		}

	}

	public void openChest() {
		int cantHealthPotions = random.nextInt(5) + 1;
		int cantManaPotions = random.nextInt(10) + 1;
		Player player = (Player) this.attackedBy;
		player.healthPotions += cantHealthPotions;
		player.manaPotions += cantManaPotions;
		GameScreen.hud.updateStats(player);
		Hud.printMessage("Abriste el cofre y obtuviste " + cantHealthPotions + " pociones de vida y " + cantManaPotions
				+ " pociones de mana!!!", MessageType.REWARD);
		
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

	public void attack(Enemy enemy, Attack attack) {
		
	}

}
