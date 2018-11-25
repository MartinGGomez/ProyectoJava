package com.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class FireAttack extends Attack{
	

	
	public FireAttack() {
		super.texture = new Texture("fireAttack.png");
		this.region = new TextureRegion(texture, 34, 7, 123, 115); 
		createAnimations();
		setScale(1.5f);
		super.name = "Ataque de fuego";
		super.damage = 50;
		super.mana = 100;
		super.energy = 20;
		super.duration = 1.5f;
	}
		
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}

	public void dispose() {
		texture.dispose();
	}

	

	public void createAnimations() {
		 // Animation DE FIRE ATTACK
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
