package com.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class BasicAttack extends Attack{
	
	public BasicAttack() {
		super.texture = new Texture("bloodsplat.png");
		this.region = new TextureRegion(texture, 34, 7, 123, 115); 
		createAnimations();
		setScale(1f);
		super.name = "Ataque basico";
		super.damage = 20;
		super.mana = 0;
		super.energy = 2;
		super.duration = 0.5f;
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
		frames.add(new TextureRegion(this.texture, 3, 15, 13, 15));
		frames.add(new TextureRegion(this.texture, 25, 10, 20, 22));
		frames.add(new TextureRegion(this.texture, 53, 6, 23, 28));
		frames.add(new TextureRegion(this.texture, 88, 4, 27, 31));
		frames.add(new TextureRegion(this.texture, 124, 8, 30, 29));
		frames.add(new TextureRegion(this.texture, 162, 9, 31, 30));
		frames.add(new TextureRegion(this.texture, 203, 9, 32, 32));
		frames.add(new TextureRegion(this.texture, 245, 9, 36, 35));
		animation = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();

	}
}
