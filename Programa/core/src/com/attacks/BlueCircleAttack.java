package com.attacks;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class BlueCircleAttack extends Attack{
	
	public BlueCircleAttack() {
		 
		 super.texture = new Texture("blueCircle.png");
			this.region = new TextureRegion(texture,  0, 384, 192, 192); 
			createAnimations();
			setScale(1.5f);
			super.name = "Blue";
			super.damage = 50;
			super.mana = 100;
			super.energy = 20;
			super.duration = 4.5f;
			super.sound = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/blue.ogg", FileType.Internal));
		 
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
			 
			 frames.add(new TextureRegion(this.texture, 0, 0, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,0, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 0, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 0, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 0, 192, 192));
			 
			 frames.add(new TextureRegion(this.texture, 0, 192, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,192, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 192, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 192, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 192, 192, 192));
			 
			 frames.add(new TextureRegion(this.texture, 0, 384, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,384, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 384, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 384, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 384, 192, 192));
			 
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
			 
			 frames.add(new TextureRegion(this.texture, 0, 1536, 192, 192));
			 frames.add(new TextureRegion(this.texture, 192,1536, 192, 192));
			 frames.add(new TextureRegion(this.texture, 384, 1536, 192, 192));
			 frames.add(new TextureRegion(this.texture, 576, 1536, 192, 192));
			 frames.add(new TextureRegion(this.texture, 768, 1536, 192, 192));


			 animation = new Animation<TextureRegion>(0.1f, frames);
			 frames.clear();
		}


}
