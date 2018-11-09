package com.attacks;

import static com.constants.Constants.PPM;

import com.actors.Enemy;
import com.actors.states.PlayerStates;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Attack extends Sprite{

	public String name = "Prueba de Ataque";
	private float duration = 2;
	
	private Texture texture;
	private TextureRegion region;
	
	private Animation<TextureRegion> animation;
	private float stateTimer;
	
	private float currentDuration = 0;
	private PlayerStates currentState;
	private PlayerStates previousState;

	private Enemy enemy;
	
	public Attack() {
		this.texture = new Texture("fireAttack.png");
		this.region = new TextureRegion(texture, 34, 7, 123, 115); 
		createAnimations();
		setScale(1.5f);
	}
	
	public void begin(Enemy enemy) {
		this.enemy = enemy;
		setBounds(enemy.body.getPosition().x , enemy.body.getPosition().y, 32 / PPM, 48 / PPM);
	}
	
	public void update(float delta) {
		currentDuration += delta;
		if(currentDuration < duration) {
			setPosition(enemy.body.getPosition().x, enemy.body.getPosition().y);
			setRegion(getFrame(delta));			
		} else {
			enemy.isBeingAttacked = false;
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

	public void createAnimations() {
		// Animation
		stateTimer = 0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		frames.add(new TextureRegion(this.texture, 34, 7, 123, 115));
		frames.add(new TextureRegion(this.texture, 207, 22, 123, 115));
		frames.add(new TextureRegion(this.texture, 394, 10, 123, 135));
		frames.add(new TextureRegion(this.texture, 595, 1, 114, 135));
		animation = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();

	}
}
