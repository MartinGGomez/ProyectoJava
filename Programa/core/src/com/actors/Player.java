package com.actors;

import static com.constants.Constants.PPM;
import static com.constants.Constants.SPEED;

import com.actors.states.PlayerStates;
import com.attacks.Attack;
import com.attacks.BasicAttack;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.constants.Constants;
import com.constants.MessageType;
import com.game.MainGame;
import com.screens.GameScreen;
import com.screens.Hud;
import com.services.collision.MyContactListener;
import com.services.collision.userdata.UserData;

public class Player extends Character{

	// Player properties
	// Health is in Character class.
	public int maxHealth = 200;
	public int maxMana = 400;
	public int maxEnergy = 100;
	public int minArmorDef = 20;
	public int maxArmorDef = 25;
	public int minHelmetDef = 10;
	public int maxHelmetDef = 15;
	public int minShieldDef = 2;
	public int maxShieldDef = 5;
	public int minAttackDamage = 50;
	public int maxAttackDamage = 70;
	public int healthPotions = 20;
	public int manaPotions = 30;
	public int money = 0;
	public float exp = 0f;

	// Scene2d
	private Label playerLabel;

	private float time = 0f;

	public float respawnTime = 4f;
	public float deadTime = 0f;

	public boolean canMoveTop = true;
	public boolean canMoveBot = true;
	public boolean canMoveRight = true;
	public boolean canMoveLeft = true;
	
	
	private Sound golpe;
	private Sound pasos;
	
	private int timePaso=0;
	
	private float deltaTime = 0f;
	private int cantPasos = 0;
	
	public boolean arriba = false, abajo = false, izquierda = false, derecha = false;
	public boolean stop = false;
	
	public boolean colliding = false;
	
	public float x, y;
	
	

	public Player(MainGame game, World world, String name, int nroJugador) {
		super(game, world, name);
		this.nroJugador = nroJugador;
		System.out.println("Jugador nro: " + nroJugador);
		if (nroJugador == 1) {
			super.texture = new Texture("player.png");
		} else {
			super.texture = new Texture("player2.png");
		}

		super.region = new TextureRegion(super.texture, 0, 0, 32, 48); // En el sprite sheet empieza en x = 16 y y =
																		// 908.
		super.attackDamage = 50;
		
		super.health = 200;
		super.mana = 400;
		super.energy = 100;

		if(!this.game.menuScreen.esCliente) {
			definePlayerBody();	
		}
		
		createAnimations();

		if(this.nroJugador == 1) {
			setBounds(29.84f, 19.88f, 32 / PPM, 48 / PPM);
			setPosition(29.84f, 19.88f);
		} else {
			setBounds(37.84f, 19.88f, 32 / PPM, 48 / PPM);
			setPosition(37.84f, 19.88f);
		}
		
		
		setRegion(standingTextures[0]);
		
		golpe = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/GolpePlayer.ogg", FileType.Internal));
		pasos = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/paso1.ogg", FileType.Internal));
		
	}

	public void definePlayerBody() {

		BodyDef bdef = new BodyDef();
		if (nroJugador == 1) {
			System.err.println("Jugador : " + this.nroJugador + " : " + this.name);
			bdef.position.set(30, 20);
		} else {
			System.err.println("Jugador 2: " + this.nroJugador + " : " + this.name);
			bdef.position.set(38, 20);
		}
		bdef.type = BodyDef.BodyType.DynamicBody;

		super.body = super.world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((this.region.getRegionWidth() / 2) / PPM, (this.region.getRegionHeight() / 4) / PPM);
		fdef.filter.categoryBits = Constants.BIT_PLAYER;
		fdef.filter.maskBits = Constants.BIT_COLLISION | Constants.BIT_PLAYER;
		fdef.shape = shape;

		UserData userData = new UserData("Player", this.nroJugador, false);

		super.body.createFixture(fdef).setUserData(userData);

		super.body.setLinearDamping(0);
		stop = false;
		setPosition(body.getPosition().x - (this.region.getRegionWidth() / 2) / PPM,
				body.getPosition().y - (this.region.getRegionHeight() / 4) / PPM);	
	}

	public void update(float delta) {
		super.update(delta);
		if(this.nroJugador == game.nroCliente) {
//			System.out.println("Posicion BODY: " + this.body.getPosition().x + " - " + this.body.getPosition().y);	
		}	
		
		deltaTime = delta;
		
		if (this.energy < this.maxEnergy) {
			time += delta;
			if (time > 1f) {
				if(this.energy + 4 > this.maxEnergy) {
					this.energy = this.maxEnergy;
				} else {
					this.energy += 4;	
				}
				if (this.nroJugador == game.nroCliente) { // Reemplazar cuando sea red: if this.nroJugador ==
															// gamescreen.nroCliente
					GameScreen.hud.updateStats(this);
				}
				time = 0f;
			}

		}
		
		if(!this.game.menuScreen.esCliente) {
			setPosition(body.getPosition().x - (this.region.getRegionWidth() / 2) / PPM,
					body.getPosition().y - (this.region.getRegionHeight() / 4) / PPM);	
		} else {
			setPosition(this.x, this.y);
		}
		
		
		
		if (this.alive) {
			
//			if (this.nroJugador == game.nroCliente) {
				if(!stop && !this.game.menuScreen.esCliente) {
					if(arriba) {
						this.moverArriba();
					}
					if(abajo) {
						this.moverAbajo();
					}
					if(derecha) {
						this.moverDerecha();
					}
					if(izquierda) {
						this.moverIzquierda();
					}
				}else {
					arriba = false;
					abajo = false;
					derecha = false;
					izquierda = false;
					if(!this.game.menuScreen.esCliente) {
						body.setLinearVelocity(0, 0);	
					}
							
				}

		} else {
			this.deadTime += delta;
			if (deadTime >= respawnTime) {
				deadTime = 0f;
				this.alive = true;
				this.resetStats();
				if (this.nroJugador == this.game.nroCliente) { // Reemplazar cuando sea red: if this.nroJugador ==

					GameScreen.hud.updateStats(this);
				}
			} else {
				if (this.nroJugador == this.game.nroCliente) { // Reemplazar cuando sea red: if this.nroJugador ==

					GameScreen.hud.updateStats(this);
				}

			}
		}
		
		if(!this.game.menuScreen.esCliente) {
			super.setRegion(getFrame(delta, false));	
		} else {
			super.setRegion(getFrame(delta, true));
		}
		
		
		// pos/x/y/nroJugador/direccion/frame
		if(!this.game.menuScreen.esCliente) {
			this.game.servidor.hiloServidor.enviarDatosATodos("pos/"+this.getX()+"/"+this.getY()+"/"+this.nroJugador+"/"+currentState+"/"+this.frameIndex);
		}
		
	}

	public void moverDerecha() {
		if (canMoveRight) {
			body.setLinearVelocity(new Vector2(SPEED, 0));
			timePaso++;
			if(timePaso>8){
			pasos.play();
			timePaso=0;
			}
		}
		states = PlayerStates.RIGHT;
		direction = PlayerStates.RIGHT;
	}

	public void moverIzquierda() {
		if (canMoveLeft) {
			body.setLinearVelocity(new Vector2(-SPEED, 0));
			timePaso++;
			if(timePaso>8){
			pasos.play();
			timePaso=0;
			}
		}
		states = PlayerStates.LEFT;
		direction = PlayerStates.LEFT;
	}

	public void moverAbajo() {
		if (canMoveBot) {
			body.setLinearVelocity(new Vector2(0, -SPEED));
			timePaso++;
			if(timePaso>8 ){
			pasos.play();
			timePaso=0;
			}
		}
		states = PlayerStates.FRONT;
		direction = PlayerStates.FRONT;
	}

	public void moverArriba() {
		if (canMoveTop) {
			body.setLinearVelocity(new Vector2(0, SPEED));	
			timePaso++;
			if(timePaso>8){
			pasos.play();
			timePaso=0;
			}
		}
		states = PlayerStates.BACK;
		direction = PlayerStates.BACK;
		cantPasos++;
		System.out.println("Moviendose arriba- cantidad de pasos: " + cantPasos);
		System.out.println("X: " + this.body.getPosition().x + " Y: " + this.body.getPosition().y);
	}
	
	public void stopPlayer() {
		this.body.setLinearVelocity(0, 0);
	}

	private void resetStats() {
		setBounds(getX(), getY(), 32 / PPM, 48 / PPM);
		this.health = this.maxHealth;
		this.mana = this.maxMana;
		this.energy = this.maxEnergy;
	}

	public void createAnimations() {
		// Animation
		direction = PlayerStates.FRONT;
		currentState = PlayerStates.FRONT;
		previousState = PlayerStates.FRONT;
		stateTimer = 0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(super.texture, i * 64, 0, 32, 48));
		}
		movingBack = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(super.texture, i * 64, 63, 32, 48));
		}
		movingLeft = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(super.texture, i * 64, 125, 32, 48));

		}
		movingFront = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(super.texture, i * 64, 191, 32, 48));
		}
		movingRight = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();

		standingTextures = new TextureRegion[4];
		standingTextures[0] = new TextureRegion(super.texture, 0, 0, 32, 48); // STANDING_BACK
		standingTextures[1] = new TextureRegion(super.texture, 0, 125, 32, 48); // STANDING_FRONT
		standingTextures[2] = new TextureRegion(super.texture, 0, 191, 32, 48); // STANDING_RIGHT
		standingTextures[3] = new TextureRegion(super.texture, 0, 63, 32, 48); // STANDING_LEFT

	}

	@Override
	public void draw(Batch batch) {
		if (alive) {
			if(this.nroJugador == this.game.nroCliente) {
//				System.out.println("dibuja en " + super.getX() + " - " + super.getY());	
			}
			super.draw(batch);
		} else {
//			System.err.println("MUERTO " + this.nroJugador);
		}

	}

	public void dispose() {
		world.destroyBody(body);
	}

	public void attack(Character enemy, Attack attack, boolean sendMessage) {
		if(this.mana >= attack.mana){
		if (this.energy > attack.energy) {
			if (enemy.alive) {
				if(sendMessage) {
					if(enemy.npc) {
						this.game.cliente.hiloCliente.enviarDatos("atacarNPC/"+enemy.enemyIndex+"/"+attack.name+"/"+this.nroJugador);	
					} else {
						this.game.cliente.hiloCliente.enviarDatos("atacarPlayer/"+enemy.enemyIndex+"/"+attack.name+"/"+this.nroJugador);
					}	
				}
				
				System.err.println("Ataque: " + attack.name + " enemigo: " + enemy.name + " hecho por: " + this.name);
				
				enemy.attack = attack;
				enemy.isBeingAttacked = true;
				enemy.attackedBy = this;
				this.doingAttack = true;
				
				if(this.nroJugador == game.nroCliente) {
					Hud.printMessage(
						"Le has causado " + enemy.attack.damage + " a " + enemy.name + " con " + enemy.attack.name,
						MessageType.COMBAT);
				} 
				if(this.nroJugador != game.nroCliente && !enemy.npc) {
					Hud.printMessage(this.name + " te ha atacado con " + attack.name + " por " + enemy.attack.damage + " puntos.",MessageType.COMBAT);
					GameScreen.hud.updateStats((Player) enemy);
				}
				
				if (this.nroJugador == game.nroCliente) { // Reemplazar cuando sea red: if this.nroJugador == gamescreen.nroCliente
					GameScreen.hud.updateStats(this);
				}
				
				
				if(enemy.attack.name.equals("Ataque basico")){
					golpe.play();	
				}else{
					selectedAttack.getSound();
				}
				
			}
		} else {
			Hud.printMessage("No tienes suficiente energia.", MessageType.COMBAT);
		}
		}else{
			Hud.printMessage("No tienes suficiente mana.", MessageType.COMBAT);
		}

	}

}

