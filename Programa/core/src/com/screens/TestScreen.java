package com.screens;

import static com.game.MainGame.PPM;
import static com.game.MainGame.SPEED;

import com.actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.MainGame;

public class TestScreen implements Screen {

	private MainGame game;
	
	private OrthographicCamera gamecam;
	private Viewport gameport;
	
	// Tiled Map 
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	// Box2D 
	private World world;
	private Box2DDebugRenderer box2dRender;

	// Scene2D
	private Stage stage;
	
	private Player player;
	
	
	
	public TestScreen(MainGame game) {
		this.game = game;
		
		gamecam = new OrthographicCamera();
		
		gameport = new FitViewport(1024 / PPM, 768 / PPM, gamecam);
		
		// Tiled Map
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Mapa de prueba.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1  / PPM);
	
		gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);
		

		
		// Box2D
		world = new World(new Vector2(0, 0), true);
		box2dRender = new Box2DDebugRenderer();
		
		// Body Definitions
		createMapObjects();
		
		player = new Player(world);
	
		// Scene2D
		stage = new Stage();
		stage.addActor(player);
		
	}

	

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void handleInput(float delta) {
		
		player.body.setLinearVelocity(0,0);
		if(Gdx.input.isKeyPressed(Keys.W)) {
			player.body.setLinearVelocity(new Vector2(0, SPEED));
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			player.body.setLinearVelocity(new Vector2(0, -SPEED));
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			player.body.setLinearVelocity(new Vector2(-SPEED, 0));
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			player.body.setLinearVelocity(new Vector2(SPEED, 0));
		}
		
	}
	
	public void update(float delta) {
		handleInput(delta);
		
		world.step(1 / 60f, 6, 2);
		
		gamecam.position.x = player.body.getPosition().x;
		gamecam.position.y = player.body.getPosition().y;
		
		
		gamecam.update();
		
		renderer.setView(gamecam);
	}
	
	@Override
	public void render(float delta) {
		
		update(delta);
		
		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render(); // Tiled Map renderer.
		
		box2dRender.render(world, gamecam.combined); // Box2D render.
		
	}
	
	
	private void createMapObjects() {
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		
		for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2)  / PPM, (rect.getY() + rect.getHeight() / 2)  / PPM);
			body = world.createBody(bdef);
			
			shape.setAsBox((rect.getWidth() / 2) / PPM, (rect.getHeight() / 2) / PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
			
		}
	}

	@Override
	public void resize(int width, int height) {
		gameport.update(width, height);
		
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
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}