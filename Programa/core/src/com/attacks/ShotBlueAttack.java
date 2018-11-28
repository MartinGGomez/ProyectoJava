package com.attacks;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class ShotBlueAttack extends Attack {
	
	public ShotBlueAttack() {
	
					super.texture = new Texture("shotBlue.png");
					this.region = new TextureRegion(texture,  0, 384, 192, 192); 
					createAnimations();
					setScale(1.5f);
					super.name = "Blue";
					super.damage = 50;
					super.mana = 100;
					super.energy = 20;
					super.duration = 1.3f;
					super.sound = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/shot.ogg", FileType.Internal));
				 
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

					 
					

					 animation = new Animation<TextureRegion>(0.1f, frames);
					 frames.clear();
				}


}
