package com.screens;

import static com.constants.Constants.PPM;

import java.util.ArrayList;

import com.actors.Enemy;
import com.actors.Player;
import com.actors.states.PlayerStates;
import com.attacks.Attack;
import com.attacks.BasicAttack;
import com.attacks.FireAttack;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.constants.MessageType;
import com.game.MainGame;
import com.services.collision.CollisionHelper;
import com.services.collision.MyContactListener;
import com.services.collision.userdata.CollisionMovement;
import com.services.combat.Combat;

public class GameScreen implements Screen, InputProcessor {

	private int cont = 0;
	private boolean estaAtacando = false;
	private OrthographicCamera gamecam;
	private Viewport gameport;
	private MainGame game;

	// Tiled Map
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	// Box2D
	public World world;
	// private Box2DDebugRenderer box2dRender;
	public static MyContactListener contactListener;

	public static Player player;
	public static Player player2;

	public static Array<Enemy> enemies = new Array<Enemy>();

	private int iteraciones = 0;

	// Helpers
	public CollisionHelper collisionHelper;

	// HUD
	public static Hud hud;

	private float cameraInitialPositionX;
	private float cameraInitialPositionY;

	private Music inicio;
	private Sound open;
	private Sound potas;

	public int nroJugador;

	// Copiar ataque de red.
	public boolean copyAttack = false;
	public boolean attackPlayer = false;
	public String attackToCopyName;
	public int attackToCopyEnemyIndex;
	public int attackToCopyAttackedBy;

	public GameScreen(MainGame game) {
		this.game = game;
		this.nroJugador = this.game.nroCliente;
		if (this.game.menuScreen.esCliente) {
			this.game.cliente.hiloCliente.app.gameScreen = this;
		} else {
			this.game.servidor.hiloServidor.app.gameScreen = this;
		}

		gamecam = new OrthographicCamera();

		gameport = new FitViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM, gamecam);

		this.game.stage = new Stage();

		// Tiled Map
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Nuevo Mapa.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

		// Box2D
		world = new World(new Vector2(0, 0), true);
		// box2dRender = new Box2DDebugRenderer();

		contactListener = new MyContactListener();
		world.setContactListener(contactListener);

		if (nroJugador == 1) {
			player = new Player(this.game, world, "Coxne", 1);
			player2 = new Player(this.game, world, "Player 2", 2);
		} else {
			player = new Player(this.game, world, "Coxne", 1);
			player2 = new Player(this.game, world, "Player 2", 2);
		}

		collisionHelper = new CollisionHelper(map, world);
		// Body Definitions
		collisionHelper.createMapObjects();
		if (!this.game.menuScreen.esCliente) {
			enemies = collisionHelper.createEnemies(game);
		} else {
			collisionHelper.generateEnemiesPositions(game);
			enemies = collisionHelper.copiarEnemigos(game);
			System.out.println("Enemigos creados : " + enemies.size);
		}

		// Hud
		if (this.nroJugador == 1) {
			hud = new Hud(this.game, this.player); // Jugador 1
		} else {
			hud = new Hud(this.game, this.player2); // Jugador 2
		}

		inicio = Gdx.audio.newMusic(Gdx.files.getFileHandle("mp3/inicio principal.mp3", FileType.Internal));
		potas = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/potas.ogg", FileType.Internal));
		open = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/openChest.ogg", FileType.Internal));

		InputMultiplexer processors = new InputMultiplexer();
		processors.addProcessor(this);
		processors.addProcessor(this.game.stage);
		Gdx.input.setInputProcessor(processors);

		hud.printMessage("Bienvenidos a LatzinaAO", MessageType.DROP);
		hud.printMessage("Game created by : GG-Games || CopyRight 2018", MessageType.REWARD);
		hud.printMessage("Garcia Gonzalo - Gomez Martin", MessageType.DROP);

	}

	public void update(float delta) {

		world.step(1 / 60f, 6, 2);

		player.update(delta);
		player2.update(delta);

		resolveCollisions();

		makeNetworkAttacks();

		handleAttacksToEnemy(contactListener.enemiesCollidingWithPlayer, delta);
		if (contactListener.isCollidingToPlayer()) {
			handleAttacksToPlayer();
		}

		for (Enemy enemy : enemies) {
			enemy.update(delta);
		}

		if (this.nroJugador == 1) {
			if (iteraciones == 1) {
				System.out.println("Siguiendo a player");
			}

			gamecam.position.x = player.body.getPosition().x + 1.23f; // Sumar diferencia de camara
			gamecam.position.y = player.body.getPosition().y + 0.5f; // Porque esta centrado con respecto al HUD
		} else {
			if (iteraciones == 1) {
				System.out.println("Siguiendo a player2");
			}

			gamecam.position.x = player2.body.getPosition().x + 1.23f; // Sumar diferencia de camara
			gamecam.position.y = player2.body.getPosition().y + 0.5f; // Porque esta centrado con respecto al HUD
		}

		if (iteraciones == 0) {
			this.cameraInitialPositionX = gamecam.position.x;
			this.cameraInitialPositionY = gamecam.position.y;
		}
		iteraciones++;

		gamecam.update();

		renderer.setView(gamecam);

		inicio.play();
	}

	private void makeNetworkAttacks() {
		if (this.copyAttack) {
			System.out.println("COPIAR ATAQUE");
			Attack attack = Attack.getAttackByName(this.attackToCopyName);
			if (this.attackPlayer) {
				if (this.attackToCopyAttackedBy == 1) { // Ataco el jugador 1
					this.player.selectedAttack = attack;
					this.player.attack(player2, attack, false);
					this.player.selectedAttack = null;
				} else { // Ataco el jugador 2
					this.player2.selectedAttack = attack;
					this.player2.attack(player, attack, false);
					this.player2.selectedAttack = null;
				}
				this.attackPlayer = false;
			} else {

				Enemy enemy = GameScreen.getEnemyByIndex(this.attackToCopyEnemyIndex);
				if (this.attackToCopyAttackedBy == 1) {
					this.player.selectedAttack = attack;
					this.player.attack(enemy, attack, false);
					this.player.selectedAttack = null;
				} else {
					this.player2.selectedAttack = attack;
					this.player2.attack(enemy, attack, false);
					this.player2.selectedAttack = null;
				}
			}
			this.copyAttack = false;
		}
	}

	@Override
	public void render(float delta) {

		//// if(!hud.boton1.isVisible()){
		//
		// estaAtacando = true;
		// cont++;
		//
		// if (cont==120){
		// hud.boton1.setVisible(true);
		// cont=0;
		// }
		// }else{
		// estaAtacando = false;
		// }

		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render(); // Tiled Map renderer.

		// box2dRender.render(world, gamecam.combined); // Box2D render.

		game.batch.setProjectionMatrix(gamecam.combined);

		game.batch.begin();

		// for (Enemy enemy : enemies) {
		// enemy.draw(game.batch);
		// if (enemy.isBeingAttacked) {
		// enemy.attack.update(delta);
		// enemy.attack.draw(game.batch);
		// }
		// }
		for (int i = 0; i < enemies.size; i++) {
			 enemies.get(i).draw(game.batch);
			if (enemies.get(i).isBeingAttacked) {
				enemies.get(i).attack.update(delta);
				 enemies.get(i).attack.draw(game.batch);
			}
		}

		 player.draw(game.batch);
		 player2.draw(game.batch);

		if (player.isBeingAttacked) {
			if (player.attack.started) {
				player.attack.update(delta);
				 player.attack.draw(game.batch);
			}
		}
		if (player2.isBeingAttacked) {
			if (player2.attack.started) {
				player2.attack.update(delta);
				 player2.attack.draw(game.batch);
			}
		}

		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		 hud.stage.draw();
		hud.stage.act();

		game.batch.end();

	}

	private Player getCloserPlayer(Enemy enemy, float minDistancia) {
		Player player1 = GameScreen.player;
		Player player2 = GameScreen.player2;

		float difPlayer1X = enemy.body.getPosition().x - player1.body.getPosition().x;
		float difPlayer2X = enemy.body.getPosition().x - player2.body.getPosition().x;
		float difPlayer1Y = enemy.body.getPosition().y - player1.body.getPosition().y;
		float difPlayer2Y = enemy.body.getPosition().y - player2.body.getPosition().y;

		// Teorema de Pitagoras // RaizDe(difx^2 + dify^2) = distanciaTotal
		float distanciaTotalPlayer1 = (float) Math.sqrt(Math.pow(difPlayer1X, 2) + Math.pow(difPlayer1Y, 2));
		float distanciaTotalPlayer2 = (float) Math.sqrt(Math.pow(difPlayer2X, 2) + Math.pow(difPlayer2Y, 2));

		if (distanciaTotalPlayer1 > minDistancia && distanciaTotalPlayer2 > minDistancia) {
			return null;
		}

		if (!player1.alive) {
			return player2;
		}
		if (!player2.alive) {
			return player1;
		}

		if (distanciaTotalPlayer1 < distanciaTotalPlayer2) {
			return player1;
		} else {
			return player2;
		}
	}

	private void resolveCollisions() {

		boolean changePathFound = false;
		for (int i = 0; i < contactListener.enemiesColliding.size(); i++) {
			for (Enemy enemy : enemies) {
				float activeDistance = 2f;
				Player player = getCloserPlayer(enemy, activeDistance);
				boolean ningunPlayerCerca = false;
				if (player == null) {
					ningunPlayerCerca = true;
				} else {
					ningunPlayerCerca = false;
				}
				if (!ningunPlayerCerca) {
					if (enemy.getEnemyIndex() == contactListener.enemiesColliding.get(i).index && !changePathFound) {
						enemy.changePath = true;
						enemy.collidingTo = contactListener.enemiesColliding.get(i).enemyCollidingTo;
						changePathFound = true;
					}
				}
			}
		}

		boolean found = false;
		for (int i = 0; i < contactListener.enemiesCollidingWithPlayer.size(); i++) {
			found = false;
			for (Enemy enemy : enemies) {
				if (enemy.getEnemyIndex() == contactListener.enemiesCollidingWithPlayer.get(i).index && !found) {
					enemy.preventMove = true;
					if (contactListener.enemiesCollidingWithPlayer.get(i).playerIndex == 1) {
						enemy.collidingWith = this.player;
					} else {
						enemy.collidingWith = this.player2;
					}
					found = true;
				}
			}

		}

		for (int i = 0; i < contactListener.enemiesStopCollidingWithPlayer.size(); i++) {
			if (contactListener.enemiesStopCollidingWithPlayer.get(i) < enemies.size) {
				enemies.get(contactListener.enemiesStopCollidingWithPlayer.get(i)).preventMove = false;
			}
		}

		for (int i = 0; i < contactListener.enemiesStopColliding.size(); i++) {
			if (contactListener.enemiesStopColliding.get(i) < enemies.size) {
				enemies.get(contactListener.enemiesStopColliding.get(i)).changePath = false;
			}
		}

		if (contactListener.isCollidingToPlayer()) {

			if (((player.body.getPosition().y + player.getHeight()) > player2.body.getPosition().y
					+ player2.getHeight())
					&& (player.body.getPosition().x < player2.body.getPosition().x + player2.getWidth()
							&& player.body.getPosition().x + player.getWidth() > player2.body.getPosition().x)) {
				// Player 1 arriba player 2 abajo
				resetMovement();
				player.canMoveBot = false;
				player2.canMoveTop = false;
			} else if (((player2.body.getPosition().y + player2.getHeight()) > player.body.getPosition().y
					+ player.getHeight())
					&& (player2.body.getPosition().x < player.body.getPosition().x + player.getWidth()
							&& player2.body.getPosition().x + player2.getWidth() > player.body.getPosition().x)) {
				// Player 2 arriba player 1 abajo
				resetMovement();
				player.canMoveTop = false;
				player2.canMoveBot = false;
			} else if (((player2.body.getPosition().x + player2.getWidth()) > player.body.getPosition().x
					+ player.getWidth())
					&& (player2.body.getPosition().y < player.body.getPosition().y + player.getHeight()
							&& player2.body.getPosition().y + player2.getHeight() > player.body.getPosition().y)) {
				// Player 1 izquierda player 2 derecha
				resetMovement();
				player.canMoveRight = false;
				player2.canMoveLeft = false;
			} else if (((player.body.getPosition().x + player.getWidth()) > player2.body.getPosition().x
					+ player2.getWidth())
					&& (player.body.getPosition().y < player2.body.getPosition().y + player2.getHeight()
							&& player.body.getPosition().y + player.getHeight() > player2.body.getPosition().y)) {
				// Player 2 izquierda player 1 derecha
				resetMovement();
				player.canMoveLeft = false;
				player2.canMoveRight = false;
			}
		} else {
			resetMovement();
		}

	}

	private void handleAttacksToEnemy(ArrayList<CollisionMovement> enemiesColliding, float delta) {
		if (this.nroJugador == 1) {
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				if (!player.doingAttack) {
					if (contactListener.isColliding()) {
						for (CollisionMovement enemyC : enemiesColliding) {
							Enemy enemy = enemies.get(enemyC.index);
							if (Combat.canAttackToEnemy(player, enemy) && (!estaAtacando)) {
								player.attack(enemy, new BasicAttack(), true);
							}
						}

					}
				}
			}
		}
		
		if (this.nroJugador == 2) {
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				if (!player2.doingAttack) {
					if (contactListener.isColliding()) {
						for (CollisionMovement enemyC : enemiesColliding) {
							Enemy enemy = enemies.get(enemyC.index);
							if (Combat.canAttackToEnemy(player2, enemy) && (!estaAtacando)) {
								player2.attack(enemy, new BasicAttack(), true);
							}
						}

					}
				}
			}
		}

	}

	private void handleAttacksToPlayer() {
		if (this.nroJugador == 1) {
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) { // Ataque del enemigo 1
				if (!player.doingAttack) {
					if (Combat.canAttackToEnemy(player, player2) && (!estaAtacando)) {
						player.attack(player2, new BasicAttack(), true);
					}
				}
			}
		}

		if (this.nroJugador == 2) {
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) { // Ataque del enemigo 2
				if (!player2.doingAttack) {
					if (Combat.canAttackToEnemy(player2, player) && (!estaAtacando)) {
						player2.attack(player, new BasicAttack(), true);
					}
				}
			}
		}
	}

	private void resetMovement() {
		player.canMoveBot = true;
		player.canMoveTop = true;
		player.canMoveLeft = true;
		player.canMoveRight = true;
		player2.canMoveBot = true;
		player2.canMoveTop = true;
		player2.canMoveLeft = true;
		player2.canMoveRight = true;
	}

	public static Enemy getEnemyByIndex(int index) {
		Enemy enemy = null;
		for (Enemy search : enemies) {
			if (search.enemyIndex == index) {
				return search;
			}
		}
		return enemy;
	}

	@Override
	public void resize(int width, int height) {
		gameport.update(width, height);
	}

	@Override
	public void dispose() {
		player.dispose();
		map.dispose();
		renderer.dispose();
		world.dispose();
		// box2dRender.dispose();
		hud.dispose();
	}

	@Override
	public void show() {

		InputMultiplexer processors = new InputMultiplexer();
		processors.addProcessor(this);
		processors.addProcessor(this.game.stage);
		Gdx.input.setInputProcessor(processors);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (this.game.menuScreen.esCliente) {
			float distanciaX, distanciaY;
			if (this.nroJugador == 1) {
				distanciaX = gamecam.position.x - this.cameraInitialPositionX + (27.265f); // 29.265 Es la diferencia
																							// entre la posicion inicial
																							// del mapa y la posicion
																							// inicial del juego
				distanciaY = gamecam.position.y - this.cameraInitialPositionY + (17.46f); // 17.46 Es la diferencia
																							// entre la posicion inicial
																							// del mapa y la posicion
																							// inicial del juego
			} else {
				distanciaX = gamecam.position.x - this.cameraInitialPositionX + (35.265f); // 29.265 Es la diferencia
																							// entre la posicion inicial
																							// del mapa y la posicion
																							// inicial del juego
				distanciaY = gamecam.position.y - this.cameraInitialPositionY + (17.46f); // 17.46 Es la diferencia
																							// entre la posicion inicial
																							// del mapa y la posicion
																							// inicial del juego
			}
			float posX = screenX / PPM + distanciaX;
			float posY = (Gdx.graphics.getHeight() / PPM - screenY / PPM) + distanciaY;

			System.out.println("Click en: " + posX + " - " + posY);

			if ((posX > (player.getX()) && posX < player.getX() + player.getWidth())
					&& (posY > player.getY() && posY < player.getY() + player.getHeight())) {
				Hud.printMessage(player.name + " - Vida: " + player.health, MessageType.PLAYER_CLICK);

				if (player2.selectedAttack != null) {
					player2.attack(player, player2.selectedAttack, true);
					player2.selectedAttack = null;
				}
			}

			if ((posX > (player2.getX()) && posX < player2.getX() + player2.getWidth())
					&& (posY > player2.getY() && posY < player2.getY() + player2.getHeight())) {
				Hud.printMessage(player2.name + " - Vida: " + player2.health, MessageType.PLAYER_CLICK);

				if (player.selectedAttack != null) {
					player.attack(player2, player.selectedAttack, true);
					player.selectedAttack = null;

				}

			}

			for (Enemy enemy : enemies) {
				if ((posX > (enemy.getX()) && posX < enemy.getX() + enemy.getWidth())
						&& (posY > enemy.getY() && posY < enemy.getY() + enemy.getHeight())) {
					if (!enemy.isChest) {
						Hud.printMessage(enemy.name + " - Vida: " + enemy.health, MessageType.ENEMY_CLICK);

						if (player.selectedAttack != null) {
							player.attack(enemy, player.selectedAttack, true);
							player.selectedAttack = null;
						}

						if (player2.selectedAttack != null) {
							player2.attack(enemy, player2.selectedAttack, true);
							player2.selectedAttack = null;
						}

					} else {
						if (button == Buttons.LEFT) {
							Hud.printMessage("Cofre de Drop", MessageType.DROP);
						}
						if (button == Buttons.RIGHT) {
							if (enemy.open) {
								Hud.printMessage("El cofre ya fue abierto", MessageType.DROP);
							} else {
								if (game.nroCliente == 1) {
									if (player == enemy.attackedBy) {
										enemy.open = true;
										enemy.openChest();
										open.play();
									} else {
										Hud.printMessage("No podes abrir este cofre", MessageType.ERROR);
									}
								}

								if (game.nroCliente == 2) {
									if (player2 == enemy.attackedBy) {
										enemy.open = true;
										enemy.openChest();
										open.play();
									} else {
										Hud.printMessage("No podes abrir este cofre", MessageType.ERROR);
									}
								}

							}
						}

					}

				}
			}

			player.selectedAttack = null;
			player2.selectedAttack = null;
		}

		if (screenX > 576 && screenX < 760 && screenY > 569 && screenY < 585) {
			this.game.cliente.hiloCliente.enviarDatos("salir");
			System.exit(0);
		}

		return false;
	}

	public void tomarPocion(int jugadorNro, String tipo) {
		if (tipo.equals("Vida")) {
			if (jugadorNro == 1) {
				if (player.health < player.maxHealth && player.healthPotions > 0) {
					player.healthPotions--;
					player.health += 10;
					potas.play();
				}
			}
			if (jugadorNro == 2) {
				if (player2.health < player2.maxHealth && player2.healthPotions > 0) {
					player2.healthPotions--;
					player2.health += 10;
					potas.play();
				}
			}
		} else {

			if (jugadorNro == 1) {
				if (player.mana < player.maxMana && player.manaPotions > 0) {
					player.manaPotions--;
					player.mana += 10;
					potas.play();
				}
			}
			if (jugadorNro == 2) {
				if (player2.mana < player2.maxMana && player2.manaPotions > 0) {
					player2.manaPotions--;
					player2.mana += 10;
					potas.play();
				}
			}

		}
		if (nroJugador == this.nroJugador) {
			if (nroJugador == 1) {
				hud.updateStats(player);
			} else {
				hud.updateStats(player2);
			}
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (this.game.menuScreen.esCliente) {
			if (keycode == Keys.NUM_1) {
				this.tomarPocion(this.nroJugador, "Vida");
				this.game.cliente.hiloCliente.enviarDatos("pocionVida/" + this.nroJugador);
			}
			if (keycode == Keys.NUM_2) {
				this.tomarPocion(this.nroJugador, "Mana");
				this.game.cliente.hiloCliente.enviarDatos("pocionMana/" + this.nroJugador);
			}
			if (keycode == Keys.W) {
				this.game.cliente.hiloCliente.enviarDatos("arriba/" + this.nroJugador);
			}
			if (keycode == Keys.S) {
				this.game.cliente.hiloCliente.enviarDatos("abajo/" + this.nroJugador);
			}
			if (keycode == Keys.A) {
				this.game.cliente.hiloCliente.enviarDatos("izquierda/" + this.nroJugador);
			}
			if (keycode == Keys.D) {
				this.game.cliente.hiloCliente.enviarDatos("derecha/" + this.nroJugador);
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (this.game.menuScreen.esCliente) {
			if (keycode == Keys.W) {
				this.game.cliente.hiloCliente.enviarDatos("stop/" + this.nroJugador);
			}
			if (keycode == Keys.S) {
				this.game.cliente.hiloCliente.enviarDatos("stop/" + this.nroJugador);
			}
			if (keycode == Keys.A) {
				this.game.cliente.hiloCliente.enviarDatos("stop/" + this.nroJugador);
			}
			if (keycode == Keys.D) {
				this.game.cliente.hiloCliente.enviarDatos("stop/" + this.nroJugador);
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
