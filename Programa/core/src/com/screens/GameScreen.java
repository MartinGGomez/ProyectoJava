package com.screens;

import com.actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.MainGame;

public class GameScreen extends BaseScreen{

	private Stage stage;
	private Player player;
	private Texture playerTexture;
	
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	public OrthographicCamera camera;
	private Viewport vista;
	private AssetManager manager;
	
	public GameScreen (MainGame game) {
		super(game);
		playerTexture = new Texture("player.png");
	}
	
	
	@Override
	public void show() {
		stage = new Stage();
		player = new Player(playerTexture);
		
		stage.addActor(player);
		player.setPosition(50, 100);
		
		int vpWidth = Gdx.graphics.getWidth(), vpHeight = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(vpWidth,vpHeight);
		camera.position.set(1800,1200,0);
		camera.update();
	
		vista = new StretchViewport(840, 620, camera);
		
		manager = new AssetManager();
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		manager.load("Mapa de Prueba.tmx", TiledMap.class);
		manager.finishLoading();
		
		map = manager.get("Mapa de Prueba.tmx", TiledMap.class);
		renderer = new OrthogonalTiledMapRenderer(map);
		
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.7f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.setView(camera);
		renderer.render();
		
		//NO FUNCIONA 
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)){
			camera.zoom +=10; 
			}
		if(Gdx.input.isKeyPressed(Input.Keys.N)){
			camera.zoom += 10; 
		}
	
		stage.act();
		
	
		stage.draw();

	}
	
	@Override
	public void hide() {
		stage.dispose();
		playerTexture.dispose();
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
