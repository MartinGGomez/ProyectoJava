package com.screens;

import com.actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.MainGame;

public class MiPantalla implements Screen {

	// Player
	private Player player;
	private Stage stage;

	// Camera
	private OrthographicCamera camera;
	private Viewport port;

	// Tiled
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private TmxMapLoader loader;

	// Box 2d
	private World world;
	private Box2DDebugRenderer b2dr;

	public MiPantalla(MainGame mainGame) {
		super();
		world = new World(new Vector2(0, 0), true);
		
		// Player setting
		stage = new Stage();
		player = new Player(world);
		stage.addActor(player);
		stage.setDebugAll(true);

		// Camera setting
		camera = new OrthographicCamera();
		port = new FitViewport(1024, 768, camera);

		// TiledMap setting
		loader = new TmxMapLoader();
		map = loader.load("Mapa de Prueba.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		camera.position.set(port.getWorldWidth() / 2, port.getWorldHeight() / 2, 0);

	
		b2dr = new Box2DDebugRenderer();

		// Objects from TiledMap
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;

		for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {

			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
			fdef.shape = shape;
			body.createFixture(fdef);

		}

		// Object Player
		BodyDef playerBodyDef = new BodyDef();
		PolygonShape playerShape = new PolygonShape();
		FixtureDef playerFixtureDef = new FixtureDef();
		Body playerBody;

		playerBodyDef.type = BodyType.DynamicBody;
		playerBodyDef.position.set(port.getWorldWidth() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
		playerBody = world.createBody(playerBodyDef);
		playerShape.setAsBox(player.getWidth() / 2, player.getHeight() / 2);
		playerFixtureDef.shape = playerShape;
		playerBody.createFixture(playerFixtureDef);

	}

	public void handleInput(float dt) {
//		if (Gdx.input.isKeyPressed(Keys.W)) {
//			camera.position.y += 1;
//		}
//		if (Gdx.input.isKeyPressed(Keys.S)) {
//			camera.position.y -= 1;
//			;
//		}
//		if (Gdx.input.isKeyPressed(Keys.A)) {
//			camera.position.x -= 1;
//		}
//		if (Gdx.input.isKeyPressed(Keys.D)) {
//			camera.position.x += 1;
//		}
	}

	public void update(float dt) {
		// Update camera
		handleInput(dt);
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
		mapRenderer.setView(camera);

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);
		mapRenderer.render();

		b2dr.render(world, camera.combined);

		// world.step(delta, 6, 2);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		port.update(width, height);
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

	}

	@Override
	public void dispose() {
		map.dispose();
		mapRenderer.dispose();
		world.dispose();
		b2dr.dispose();
		stage.dispose();
	}

}
