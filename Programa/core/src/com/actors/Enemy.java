package com.actors;

import static com.constants.Constants.PPM;
import static com.constants.Constants.SPEED;

import com.actors.states.PlayerStates;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.constants.Constants;
import com.constants.MessageType;
import com.game.MainGame;
import com.screens.GameScreen;
import com.screens.Hud;
import com.services.collision.userdata.UserData;

public class Enemy extends Character {

	public static String name = "Monstruo";
	// Health is in Character class.

	private float posX, posY;
	

	public Label enemyLabel;

	public boolean changePath = false;
	public String collidingTo;
	private String moveTo;

	public Player collidingWith;
	private float time = 0f;

	private int cont = 0;
	
	private Sound golpe;

	public Enemy(MainGame game, World world, float posX, float posY, int enemyIndex) {
		super(game, world, name);
		super.texture = new Texture("monster.png");
		super.region = new TextureRegion(super.texture, 18, 0, 29, 55);
		super.attackDamage = 20;
		super.health = 100;
		super.mana = 0;
		super.energy = 0;

		this.posX = posX;
		this.posY = posY;
		this.enemyIndex = enemyIndex;

		defineEnemyBody();
		createAnimations();

		setBounds(body.getPosition().x, body.getPosition().y, 29 / PPM, 55 / PPM);
		setRegion(standingTextures[0]);
		
		golpe = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/GolpeBasico.ogg", FileType.Internal));

	}

	public int getEnemyIndex() {
		return enemyIndex;
	}

	public void update(float delta) {
		super.update(delta);
		if (alive) {
			if (preventMove) {
				MassData mass = new MassData();
				mass.mass = 999999;
				body.setMassData(mass);

				handleAttackToPlayer(delta);

			} else {
				body.resetMassData();
			}

			setPosition(body.getPosition().x - (this.region.getRegionWidth() / 2) / PPM,
					body.getPosition().y - (this.region.getRegionHeight() / 4) / PPM);
			
			
			body.setLinearVelocity(0, 0);

			// Movimiento automatico
			float activeDistance = 2f;
			Player player = getCloserPlayer(activeDistance);
			boolean ningunPlayerCerca = false;
			if(player==null) {
				ningunPlayerCerca = true;
			} else {
				ningunPlayerCerca = false;
			}

			if (!preventMove) {

				if(!ningunPlayerCerca) {
					
				
				
				if (((super.body.getPosition().x - player.body.getPosition().x) < activeDistance) // SIGUE A LA
																									// IZQUIERDA
						&& super.body.getPosition().x > player.body.getPosition().x) {
					body.setLinearVelocity(new Vector2((float) -(SPEED * 0.55), 0));
				}

				if (((super.body.getPosition().x + player.body.getPosition().x) > activeDistance) // SIGUE A LA DERECHA
						&& super.body.getPosition().x < player.body.getPosition().x) {
					body.setLinearVelocity(new Vector2((float) (SPEED * 0.55), 0));
				}

				float dif = Math.abs(super.body.getPosition().x - player.body.getPosition().x);

				if (dif < 0.1f) {
					if (((super.body.getPosition().y + player.body.getPosition().y) > activeDistance) // SIGUE A ARRIBA
							&& super.body.getPosition().y < player.body.getPosition().y) {
						body.setLinearVelocity(new Vector2(0, (float) +(SPEED * 0.55)));
					}

					if (((super.body.getPosition().y - player.body.getPosition().y) < activeDistance) // SIGUE A ABAJO
							&& super.body.getPosition().y > player.body.getPosition().y) {
						body.setLinearVelocity(new Vector2(0, (float) -(SPEED * 0.55)));
					}
				}

				if (changePath) {
					//
					
					moveTo = changeDirectionTo(this.collidingTo);
					if(this.enemyIndex == 33) {
						System.out.println("Should change path to: " + moveTo);	
					}
					
					if (moveTo.equals("Top")) {
						body.setLinearVelocity(new Vector2(0, (float) (SPEED * 0.55)));
					} else if (moveTo.equals("Bot")) {
						body.setLinearVelocity(new Vector2(0, (float) -(SPEED * 0.55)));
					} else if (moveTo.equals("Right")) {
						body.setLinearVelocity(new Vector2((float) (SPEED * 0.55), 0));
					} else {
						body.setLinearVelocity(new Vector2((float) -(SPEED * 0.55), 0));
					}
				}
				
				}

			}

			if (body.getLinearVelocity().y > 0) {
				states = PlayerStates.BACK;
				direction = PlayerStates.BACK;
			}
			if (body.getLinearVelocity().y < 0) {
				states = PlayerStates.FRONT;
				direction = PlayerStates.FRONT;
			}
			if (body.getLinearVelocity().x < 0) {
				states = PlayerStates.LEFT;
				direction = PlayerStates.LEFT;
			}
			if (body.getLinearVelocity().x > 0) {
				states = PlayerStates.RIGHT;
				direction = PlayerStates.RIGHT;
			}
			super.setRegion(getFrame(delta));
		} else {
			body.setLinearVelocity(0, 0);
			MassData mass = new MassData();
			mass.mass = 999999;
			body.setMassData(mass);
			
		}
	}

	private Player getCloserPlayer(float minDistancia) {
		Player player1 = GameScreen.player;
		Player player2 = GameScreen.player2;
		
		float difPlayer1X = this.body.getPosition().x - player1.body.getPosition().x;
		float difPlayer2X = this.body.getPosition().x - player2.body.getPosition().x;
		float difPlayer1Y = this.body.getPosition().y - player1.body.getPosition().y;
		float difPlayer2Y = this.body.getPosition().y - player2.body.getPosition().y;
		
		
		// Teorema de Pitagoras // RaizDe(difx^2 + dify^2) = distanciaTotal 
		float distanciaTotalPlayer1 =(float) Math.sqrt(Math.pow(difPlayer1X, 2) + Math.pow(difPlayer1Y, 2));
		float distanciaTotalPlayer2 =(float) Math.sqrt(Math.pow(difPlayer2X, 2) + Math.pow(difPlayer2Y, 2));
		
		if(distanciaTotalPlayer1>minDistancia && distanciaTotalPlayer2>minDistancia) {
			return null;
		}
		
		
		if(!player1.alive) {
			return player2;
		}
		if(!player2.alive) {
			return player1;
		}
		
		if(distanciaTotalPlayer1 < distanciaTotalPlayer2) {
			return player1;
		} else {
			return player2;
		}
	}

	private void handleAttackToPlayer(float delta) {
		Player playerToAttack = this.collidingWith;
		time += delta;

		if (time > Constants.ENEMY_ATTACK_SPEED) {
			this.doingAttack = false;
			time = 0f;
		}

		if (!this.doingAttack) {
			if(playerToAttack.alive) {
			this.doingAttack = true;
			if(playerToAttack.health - this.attackDamage < 0) {
				playerToAttack.health = 0;
			} else {
				playerToAttack.health -= this.attackDamage;	
			}
			if(playerToAttack.name.equals("Coxne")) { // CAMBIAR CUANDO SEA EN RED POR Nº CLIENTE
				Hud.printMessage(this.name + " te ha pegado por " + this.attackDamage + " puntos de vida",
						MessageType.COMBAT);
				GameScreen.hud.updateStats(playerToAttack);
				
				
			}

			golpe.play();
			
			}
		}

	}

	private String changeDirectionTo(String collidingTo) {
		String moveTo = "";
		if (collidingTo.equals("Top")) {
			moveTo = "Right";
		}
		if (collidingTo.equals("Bot")) {
			moveTo = "Left";
		}
		if (collidingTo.equals("Right")) {
			moveTo = "Bot";
		}
		if (collidingTo.equals("Left")) {
			moveTo = "Top";
		}
		return moveTo;
	}

	@Override
	public void draw(Batch batch) {

//		if (alive) {
			super.draw(batch); // SE PUEDE PONER UN IF
			// if (1==2) { no se dibuja } => Respawn??
//		}
		// else if (!alive && cont==0){
		// cont=1;
		// Sound enemyKill;
		// enemyKill = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/enemyKill.wav",
		// FileType.Internal));
		// enemyKill.play();
		// }

	}

	public void dispose() {
		world.destroyBody(body);
	}

	public void defineEnemyBody() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(this.posX, this.posY);
		bdef.type = BodyDef.BodyType.DynamicBody;

		super.body = super.world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.region.getRegionWidth() / 2) / PPM, (this.region.getRegionHeight() / 4) / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = Constants.BIT_PLAYER;
		fdef.isSensor = false;
		fdef.filter.maskBits = Constants.BIT_COLLISION | Constants.BIT_PLAYER;

		UserData userData = new UserData("Enemy", enemyIndex, false);

		super.body.createFixture(fdef).setUserData(userData);

		// Collision sensor
		// Bottom
		shape.setAsBox((((this.region.getRegionWidth()) / 2.5f)) / PPM, 2 / PPM, new Vector2(0, -12f / PPM), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.restitution = 1;
		super.body.createFixture(fdef).setUserData(userData);

		// Top
		shape.setAsBox((((this.region.getRegionWidth()) / 2.5f)) / PPM, 2 / PPM, new Vector2(0, 12f / PPM), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.restitution = 2f;
		super.body.createFixture(fdef).setUserData(userData);

		// Right
		shape.setAsBox(2 / PPM, ((this.region.getRegionHeight() / 4.2f)) / PPM, new Vector2(0.15f, 0), 0);
		fdef.shape = shape;
		fdef.restitution = 3f;
		fdef.isSensor = true;
		super.body.createFixture(fdef).setUserData(userData);

		// Left
		shape.setAsBox(2 / PPM, ((this.region.getRegionHeight() / 4.2f)) / PPM, new Vector2(-0.15f, 0), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.restitution = 4f;
		super.body.createFixture(fdef).setUserData(userData);

	}

	public void createAnimations() {
		// Animation
		direction = PlayerStates.FRONT;
		currentState = PlayerStates.FRONT;
		previousState = PlayerStates.FRONT;
		stateTimer = 0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		frames.add(new TextureRegion(super.texture, 292, 291, 44, 91));
		frames.add(new TextureRegion(super.texture, 337, 291, 44, 91));
		frames.add(new TextureRegion(super.texture, 384, 291, 44, 91));
		movingBack = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();
		frames.add(new TextureRegion(super.texture, 288, 99, 44, 91));
		frames.add(new TextureRegion(super.texture, 348, 99, 44, 91));
		frames.add(new TextureRegion(super.texture, 384, 99, 44, 91));
		movingLeft = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();
		frames.add(new TextureRegion(super.texture, 290, 3, 46, 91));
		frames.add(new TextureRegion(super.texture, 337, 3, 46, 91));
		frames.add(new TextureRegion(super.texture, 384, 3, 46, 91));
		movingFront = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();
		frames.add(new TextureRegion(super.texture, 295, 195, 46, 91));
		frames.add(new TextureRegion(super.texture, 345, 195, 46, 91));
		frames.add(new TextureRegion(super.texture, 387, 195, 46, 91));
		movingRight = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		standingTextures = new TextureRegion[4];
		standingTextures[0] = new TextureRegion(super.texture, 292, 291, 44, 91); // STANDING_BACK
		standingTextures[1] = new TextureRegion(super.texture, 290, 3, 46, 91); // STANDING_FRONT
		standingTextures[2] = new TextureRegion(super.texture, 295, 195, 46, 91); // STANDING_RIGHT
		standingTextures[3] = new TextureRegion(super.texture, 288, 99, 44, 91); // STANDING_LEFT

	}

}
