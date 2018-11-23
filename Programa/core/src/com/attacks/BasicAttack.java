package com.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.constants.Constants;

public class BasicAttack extends Attack{
	

	
	public BasicAttack() {
		super.texture = new Texture("blood.png");
		this.region = new TextureRegion(texture, 34, 7, 123, 115); 
		createAnimations();
		setScale(1f);
		super.name = "Ataque basico";
		super.damage = 50;
		super.mana = 0;
		super.energy = 2;
		super.duration = Constants.ATTACK_SPEED;
	}
		
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}

	public void dispose() {
		texture.dispose();
	}

	

	public void createAnimations() {
		// Animation
		stateTimer = 0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		frames.add(new TextureRegion(this.texture, 83, 4, 42, 31));
		frames.add(new TextureRegion(this.texture, 135, 2, 42, 31));
		frames.add(new TextureRegion(this.texture, 188, 2, 38, 31));
		frames.add(new TextureRegion(this.texture, 238, 2, 28, 31));
		frames.add(new TextureRegion(this.texture, 284, 2, 27, 31));
		frames.add(new TextureRegion(this.texture, 330, 2, 27, 31));
		animation = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();

	}
}
