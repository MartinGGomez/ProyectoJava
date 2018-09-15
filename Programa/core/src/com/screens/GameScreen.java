package com.screens;

import com.actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.MainGame;

public class GameScreen extends BaseScreen{

	private Stage stage;
	private Player player;
	
	// Tiled Map
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	public OrthographicCamera camera;
	private Viewport vista;
	private AssetManager manager;
	//
	private TmxMapLoader mapLoader;
	
	
	//	
	// Box2D
	private Box2DDebugRenderer box2dRender; 
	private World world;
	
	
	public GameScreen (MainGame game) {
		super(game);
		world = new World(new Vector2(0, 0), true);
		
		//Modo Pantalla completa 
		//Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
		//Gdx.graphics.setFullscreenMode(mode);

		stage = new Stage();
		player = new Player(world);

		
		stage.setDebugAll(true);
		
		stage.addActor(player);
		int vpWidth = Gdx.graphics.getWidth(), vpHeight = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(vpWidth/2,vpHeight/2);
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camera.position.set(player.getX(),player.getY(),0);
		camera.zoom += 3;
		camera.update();
		
		player.setPosition(camera.position.x*vpHeight,camera.position.y*vpWidth);
		
	
		vista = new StretchViewport(800, 600, camera);
		
		manager = new AssetManager();
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		manager.load("Mapa de Prueba.tmx", TiledMap.class);
		manager.finishLoading();
		
		map = manager.get("Mapa de Prueba.tmx", TiledMap.class);
		renderer = new OrthogonalTiledMapRenderer(map);
	

		
		
		// Box2D
		box2dRender = new Box2DDebugRenderer();

		
		BodyDef bodyDef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fixtureDef = new FixtureDef();
		Body body;
		

		// Colisiones del mapa.
		for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bodyDef.type = BodyType.StaticBody;
			System.out.println(rect.getX() + " x " + rect.getY());
			bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
			
			body = world.createBody(bodyDef);
			
			shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
			fixtureDef.shape = shape;
			body.createFixture(fixtureDef);
		}
		

		
		// Box2D Player 
		// No funciona. Player position = 50, 100. Map objects 1800 - 1200
		BodyDef playerBodyDef = new BodyDef();
		PolygonShape playerShape = new PolygonShape();
		FixtureDef playerFixtureDef = new FixtureDef();
		Body playerBody;
		
		System.out.println("Player position: " + player.getX() + " , " + player.getY());
		System.out.println("Player size: " + player.getWidth() + " , " + player.getHeight());
		
		
		playerBodyDef.type = BodyType.DynamicBody;
		playerBodyDef.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
		
		playerBody = world.createBody(playerBodyDef);
		
		playerShape.setAsBox(player.getWidth() / 2, player.getHeight() / 2);
		playerFixtureDef.shape = playerShape;
		playerBody.createFixture(playerFixtureDef);

	}
	
	
	@Override
	public void show() {
		
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.7f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			camera.position.y +=2;
			}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			camera.position.y -=2;;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			camera.position.x -=2;
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			camera.position.x +=2;
		}		
		
		//posicion de la camara 
		//System.out.println(position);
		
		camera.update();
		renderer.setView(camera);
		renderer.render();
		
		box2dRender.render(world, camera.combined);
	
		stage.act();
		
	
		stage.draw();

	}
	
	@Override
	public void hide() {
		stage.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		vista.update(width, height);

	}
	
	@Override
	public void dispose() {
		manager.dispose();

	}
}
