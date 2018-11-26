package com.attacks;

import static com.constants.Constants.PPM;

import com.actors.Character;
import com.actors.states.PlayerStates;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
		}

		setBounds(characterToAttack.body.getPosition().x, characterToAttack.body.getPosition().y, 32 / PPM, 48 / PPM);
	}

	public void update(float delta) {
		currentDuration += delta;
		if (currentDuration < duration) {
			setPosition(characterToAttack.body.getPosition().x, characterToAttack.body.getPosition().y);
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
}
