package com.screens;
import com.actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.game.MainGame;

public class MiPantalla implements Screen {
	
	
	private Batch batch;
	private OrthographicCamera camera;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	public static final int TILES_IN_CAMERA_WIDTH = 100 ;
	public static final int TILES_IN_CAMERA_HEIGHT = 100;
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	
	


	public MiPantalla(MainGame mainGame) {
		super();
	}

	@Override
	public void show() {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600); 
		camera.position.y = (TILES_IN_CAMERA_HEIGHT*TILE_HEIGHT)/2;
		camera.position.x = (TILES_IN_CAMERA_WIDTH*TILE_WIDTH)/2;
		camera.update();
		
		map = new TmxMapLoader().load("Mapa de Prueba.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		batch = mapRenderer.getBatch();
		
	}

	@Override
	public void render(float delta) {

		
		batch.begin();
		
		batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			camera.position.y +=2;
			//camera.zoom += 0.2f;
			}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			camera.position.y -=2;
			//camera.zoom -= 0.2f;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			camera.position.x -=2;
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			camera.position.x +=2;
		}	
		
		mapRenderer.render();
		mapRenderer.setView(camera);
		camera.update();
		
		
	}
	
	
	
		
	@Override
	public void resize(int width, int height) {
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
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
