package com.screens;

import static com.constants.Constants.PPM;

import java.rmi.server.SocketSecurityException;
import java.util.ArrayList;

import com.actors.Enemy;
import com.actors.Player;
import com.actors.states.PlayerStates;
import com.attacks.Attack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
	private World world;
	private Box2DDebugRenderer box2dRender;
	public static MyContactListener contactListener;

	public static Player player;

	private Enemy enemy;

	private Array<Enemy> enemies;

	private int iteraciones = 0;

	// Helpers
	private CollisionHelper collisionHelper;

	// HUD
	public static Hud hud;

	private Attack attack;

	private float cameraInitialPositionX;
	private float cameraInitialPositionY;

	public GameScreen(MainGame game) {
		this.game = game;

		gamecam = new OrthographicCamera();

		gameport = new FitViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM, gamecam);

		this.game.stage = new Stage();

		// Tiled Map
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Mapa de Prueba.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

		// Box2D
		world = new World(new Vector2(0, 0), true);
		box2dRender = new Box2DDebugRenderer();

		contactListener = new MyContactListener();
		world.setContactListener(contactListener);

		player = new Player(this.game, world, "Coxne");
		
		// Body Definitions
		collisionHelper = new CollisionHelper(map, world);
		collisionHelper.createMapObjects();
		enemies = collisionHelper.createEnemies(this.game);

		

		// Hud
		hud = new Hud(this.game, this.player);
		player.defineStageElements();
		
		
		InputMultiplexer processors = new InputMultiplexer();
		processors.addProcessor(this);
		processors.addProcessor(this.game.stage);
		Gdx.input.setInputProcessor(processors);

	}

	public void update(float delta) {

		world.step(1 / 60f, 6, 2);

		player.update(delta);
		
		resolveEnemyCollisions();
		
		

		handleAttacks(contactListener.enemiesCollidingWithPlayer, delta);

		for (Enemy enemy : enemies) {
			enemy.update(delta);
			// System.out.println("Enemy " + enemy.getEnemyIndex() + " health " +
			// enemy.health);
		}

		gamecam.position.x = player.body.getPosition().x + 1.23f; // Sumar diferencia de camara
		gamecam.position.y = player.body.getPosition().y + 0.5f; // Porque esta centrado con respecto al HUD

		if (iteraciones == 0) {
			this.cameraInitialPositionX = gamecam.position.x;
			this.cameraInitialPositionY = gamecam.position.y;
		}
		iteraciones++;


		gamecam.update();

		renderer.setView(gamecam);
	}

	private void resolveEnemyCollisions() {
		boolean changePathFound = false;
		for (int i = 0; i < contactListener.enemiesColliding.size(); i++) {
			for (Enemy enemy: enemies) {
				if(enemy.getEnemyIndex() == contactListener.enemiesColliding.get(i).index && !changePathFound) {
					enemy.changePath = true;
					enemy.collidingTo = contactListener.enemiesColliding.get(i).enemyCollidingTo;
//					System.err.println("Colliding to " + enemy.collidingTo);
					changePathFound = true;
				}
			}
		}
	
		
		boolean found = false;
		for (int i = 0; i < contactListener.enemiesCollidingWithPlayer.size(); i++) {
			found = false;
			for (Enemy enemy: enemies) {
				if(enemy.getEnemyIndex() == contactListener.enemiesCollidingWithPlayer.get(i).index && !found) {
					enemy.preventMove = true;
					found = true;
				}
			}
			
		}
		
		for (int i = 0; i < contactListener.enemiesStopCollidingWithPlayer.size(); i++) {
			enemies.get(contactListener.enemiesStopCollidingWithPlayer.get(i)).preventMove = false;
		}
		
		for (int i = 0; i < contactListener.enemiesStopColliding.size(); i++) {
			enemies.get(contactListener.enemiesStopColliding.get(i)).changePath = false;
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

		box2dRender.render(world, gamecam.combined); // Box2D render.

		game.batch.setProjectionMatrix(gamecam.combined);

		game.batch.begin();

		for (Enemy enemy : enemies) {
			enemy.draw(game.batch);
			if (enemy.isBeingAttacked) {
				enemy.attack.update(delta);
				enemy.attack.draw(game.batch);
			}
		}

		player.draw(game.batch);

		if (player.isBeingAttacked) {
			player.attack.update(delta);
			player.attack.draw(game.batch);
		}

		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		hud.stage.act();

		game.batch.end();

	}

	private void handleAttacks(ArrayList<CollisionMovement> enemiesColliding, float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (contactListener.isColliding()) { // Lo mismo para enemy attack player en un futuro
//				System.out.println("Puede atacar");
				for (CollisionMovement enemyC: enemiesColliding) {
					Enemy enemy = enemies.get(enemyC.index);
					if (Combat.canAttackToEnemy(player, enemy) && (!estaAtacando)) {
						player.attack(enemy, delta);
						// hud.boton1.setVisible(false);
					}
				}
				
				
			}
		}
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
		box2dRender.dispose();
		hud.dispose();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float distanciaX = gamecam.position.x - this.cameraInitialPositionX;
		float distanciaY = gamecam.position.y - this.cameraInitialPositionY;
		float posX = screenX / PPM + distanciaX;
		float posY = (Gdx.graphics.getHeight() / PPM - screenY / PPM) + distanciaY;

		if ((posX > (player.getX()) && posX < player.getX() + player.getWidth())
				&& (posY > player.getY() && posY < player.getY() + player.getHeight())) {
			hud.printMessage("Clickeaste al jugador");
		}

		for (Enemy enemy : enemies) {
			if ((posX > (enemy.getX()) && posX < enemy.getX() + enemy.getWidth())
					&& (posY > enemy.getY() && posY < enemy.getY() + enemy.getHeight())) {
				hud.printMessage("Clickeaste a " + enemy.name);
			}

		}

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
