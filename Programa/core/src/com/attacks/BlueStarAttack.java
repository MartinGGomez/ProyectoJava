package com.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class BlueStarAttack extends Attack {

	 public BlueStarAttack() {
	
		    super.texture = new Texture("blueStar.png");
			this.region = new TextureRegion(texture, 0, 960, 192, 192); 
			createAnimations();
			setScale(1.5f);
			super.name = "Ataque de hielo";
			super.damage = 50;
			super.mana = 100;
			super.energy = 20;
			super.duration = 2.5f;
		 
	}
	 
	 @Override
		public void draw(Batch batch) {
			super.draw(batch);
		}

		public void dispose() {
			texture.dispose();
		}

		

		public void createAnimations() {
			 
			 stateTimer = 0;
			 Array<TextureRegion> frames = new Array<TextureRegion>();
			 frames.add(new TextureRegion(this.texture, 0, 576, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,576, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 576, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 576, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 576, 192, 192));
			 
			 frames.add(new TextureRegion(this.texture, 0, 768, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,768, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 768, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 768, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 768, 192, 192));
			 
			 frames.add(new TextureRegion(this.texture, 0, 960, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,960, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 960, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 960, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 960, 192, 192));
			 
			 frames.add(new TextureRegion(this.texture, 0, 1152, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,1152, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 1152, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 1152, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 1152, 192, 192));
			 
			 frames.add(new TextureRegion(this.texture, 0, 1344, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,1344, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 1344, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 1344, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 1344, 192, 192));

			 animation = new Animation<TextureRegion>(0.1f, frames);
			 frames.clear();
		}
	
}
