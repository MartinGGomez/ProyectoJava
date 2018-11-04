package com.screens;

import static com.constants.Constants.PPM;

import com.actors.Enemy;
import com.actors.Player;
import com.attacks.Attack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.MainGame;
import com.services.collision.CollisionHelper;
import com.services.collision.MyContactListener;
import com.services.combat.Combat;

public class GameScreen implements Screen {

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
	private MyContactListener contactListener;

	private Player player;

	private Enemy enemy;

	private Array<Enemy> enemies;

	// Helpers
	private CollisionHelper collisionHelper;

	// HUD
	public static Hud hud;
	
	private Attack attack;

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

		// Body Definitions
		collisionHelper = new CollisionHelper(map, world);
		collisionHelper.createMapObjects();
		enemies = collisionHelper.createEnemies();

		player = new Player(this.game, world, "Coxne");

		// Hud
		hud = new Hud(this.game, this.player);
		player.defineStageElements();
		
	}

	public void update(float delta) {

		world.step(1 / 60f, 6, 2);

		player.update(delta);

		Enemy enemyCollided = enemies.get(contactListener.getEnemyCollided());
		enemyCollided.preventMove = true;

		handleAttacks(enemyCollided, delta);

		for (Enemy enemy : enemies) {
			enemy.update(delta);
			// System.out.println("Enemy " + enemy.getEnemyIndex() + " health " +
			// enemy.health);
		}

		gamecam.position.x = player.body.getPosition().x + 1.23f; // Sumar diferencia de camara
		gamecam.position.y = player.body.getPosition().y + 0.5f; // Porque esta centrado con respecto al HUD

		gamecam.update();

		renderer.setView(gamecam);
	}

	@Override
	public void render(float delta) {

		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// stage.act();

		renderer.render(); // Tiled Map renderer.

		box2dRender.render(world, gamecam.combined); // Box2D render.

		game.batch.setProjectionMatrix(gamecam.combined);

		game.batch.begin();

		for (Enemy enemy : enemies) {
			enemy.draw(game.batch);
			if(enemy.isBeingAttacked) {
				enemy.attack.update(delta);
				enemy.attack.draw(game.batch);	
			}
		}
		
		
		player.draw(game.batch);
		
		if(player.isBeingAttacked) {
			player.attack.update(delta);
			player.attack.draw(game.batch);	
		}
		
		
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		hud.stage.act();

		game.batch.end();

	}

	private void handleAttacks(Enemy enemy, float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (contactListener.isColliding()) { // Lo mismo para enemy attack player en un futuro
				if (Combat.canAttackToEnemy(player, enemy)) {
					player.attack(enemy, delta);
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

}
