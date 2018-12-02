package com.attacks;

import com.actors.Character;
import com.actors.Player;
import com.actors.states.PlayerStates;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.MainGame;
import com.screens.GameScreen;
import static com.constants.Constants.PPM;

public abstract class Attack extends Sprite {

	public String name;
	protected float duration;
	public int damage;
	public int mana;
	public int energy;

	protected Texture texture;
	protected TextureRegion region;

	protected Animation<TextureRegion> animation;
	protected float stateTimer;

	private float currentDuration = 0;
	private PlayerStates currentState;
	private PlayerStates previousState;

	private Character characterToAttack;
	private Character attacker;

	public boolean started = false;
	
	public Sound sound;
	
	public MainGame game;

	public Attack() {
	}

	public void begin(Character characterToAttack, Character attacker) {
		this.characterToAttack = characterToAttack;
		this.attacker = attacker;
		if (!this.started) {
			this.started = true;
			if(this.characterToAttack.health - this.damage < 0) {
				this.characterToAttack.health = 0;
			} else {
				this.characterToAttack.health -= this.damage;
			}
			
			if (this.attacker.mana != 0) {
				if(this.attacker.mana - this.mana < 0) {
					this.attacker.mana = 0;
				} else {
					this.attacker.mana -= this.mana;
				}
			}
			if (this.attacker.energy != 0) {
				if(this.attacker.energy - this.energy < 0) {
					this.attacker.energy = 0;
				} else {
					this.attacker.energy -= this.energy;	
				}
			}
			// Actualizar HUD
			if(!this.characterToAttack.npc) {
				Player p = (Player) this.characterToAttack;
				if(p.nroJugador == MainGame.nroCliente) {
					GameScreen.hud.updateStats(p);	
				}
			}
		}

		
		setBounds(characterToAttack.body.getPosition().x, characterToAttack.body.getPosition().y, 32 / PPM, 48 / PPM);
		
	}

	public void update(float delta) {
		
		float width2 =0.20f;
		float height2 = 0.24f;
		
		
		currentDuration += delta;
		if (currentDuration < duration) {
			setPosition(characterToAttack.body.getPosition().x - width2  , characterToAttack.body.getPosition().y - height2);
			setRegion(getFrame(delta));
		} else {
			characterToAttack.isBeingAttacked = false;
			attacker.doingAttack = false;
		}
	}

	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}

	public void dispose() {
		texture.dispose();
	}

	public TextureRegion getFrame(float delta) {

		TextureRegion textureRegion;

		textureRegion = animation.getKeyFrame(stateTimer, true);

		stateTimer = currentState == previousState ? stateTimer + delta : 0;

		previousState = currentState;

		return textureRegion;
	}

	public void getSound() {
		this.sound.play();
	}
	
	public static Attack getAttackByName(String name) {
		Attack attack = null;
		if(name.equals("Ataque basico")) {
			attack = new BasicAttack();
		}
		if(name.equals("Blue")) {
			attack = new BlueCircleAttack();
		}
		if(name.equals("Ataque de hielo")) {
			attack = new BlueStarAttack();
		}
		if(name.equals("Rugido")) {
			attack = new DogFireAttack();
		}
		if(name.equals("Ataque de fuego")) {
			attack = new FireAttack();
		}
		if(name.equals("Health")) {
			attack = new HealthAttack();
		}
		if(name.equals("Ataque de furia")) {
			attack = new PinkAttack();
		}
		if(name.equals("Shot Blue")) {
			attack = new ShotBlueAttack();
		}
		if(name.equals("Volcan")) {
			attack = new VulcanAttack();
		}
		return attack;
	}
}
