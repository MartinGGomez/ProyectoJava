package com.services.collision;

import static com.constants.Constants.PPM;

import java.util.Random;

import com.actors.Enemy;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.constants.Constants;
import com.game.MainGame;
import com.services.collision.userdata.UserData;

public class CollisionHelper {

	private TiledMap map;
	private World world;
	public static Array<Vector2> enemiesPositions = new Array<Vector2>();
	
	public CollisionHelper(TiledMap map, World world) {
		this.map = map;
		this.world = world;
	}
	
	public void createMapObjects() {
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();

		for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {

			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

			Body body = world.createBody(bdef);

			shape.setAsBox((rect.getWidth() / 2) / PPM, (rect.getHeight() / 2) / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = Constants.BIT_COLLISION;
			fdef.filter.maskBits = Constants.BIT_PLAYER;
			body.createFixture(fdef).setUserData(new UserData("Map", 0, false));

		}
		
		for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {

			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

			Body body = world.createBody(bdef);

			shape.setAsBox((rect.getWidth() / 2) / PPM, (rect.getHeight() / 2) / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = Constants.BIT_COLLISION;
			fdef.filter.maskBits = Constants.BIT_PLAYER;
			body.createFixture(fdef).setUserData(new UserData("Map", 0, false));

		}
		
		for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {

			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

			Body body = world.createBody(bdef);

			shape.setAsBox((rect.getWidth() / 2) / PPM, (rect.getHeight() / 2) / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = Constants.BIT_COLLISION;
			fdef.filter.maskBits = Constants.BIT_PLAYER;
			body.createFixture(fdef).setUserData(new UserData("Map", 0, false));

		}
		
		for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

			Body body = world.createBody(bdef);

			shape.setAsBox((rect.getWidth() / 2) / PPM, (rect.getHeight() / 2) / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = Constants.BIT_COLLISION;
			fdef.filter.maskBits = Constants.BIT_PLAYER;
			body.createFixture(fdef).setUserData(new UserData("Map", 0, false));

		}
	}

	public Array<Enemy> createEnemies(MainGame game) {
		Random random = new Random();
		int maxEnemies = 2;

		Array<Enemy> enemies = new Array<Enemy>();
		for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			for (int i = 1; i < maxEnemies+1; i++) {
				
				float posX = ((rect.getX() + rect.getWidth()) * 0.20f) * i; 
				float posY = ((rect.getY() + rect.getHeight()) * 0.20f) * i;

				if(game.menuScreen.esCliente) {
					enemiesPositions.add(new Vector2(posX / PPM, posY / PPM));
				} else {
					enemies.add(new Enemy(game, world, posX / PPM, posY / PPM, i));	
				}
				
			}

		}
		return enemies;
	}
	
	public void generateEnemiesPositions(MainGame game) {
		int maxEnemies = 1;
		
		for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			for (int i = 1; i < maxEnemies+1; i++) {				
				
				float posX = rect.getX() / PPM;
				float posY = rect.getY() / PPM;

				if(game.menuScreen.esCliente) {
					enemiesPositions.add(new Vector2(posX, posY));
				}
			
			}

		}
	}
	
	public static void guardarPosicion(float x, float y) {
		Vector2 vector = new Vector2(x, y);
		enemiesPositions.add(vector);
	}
	
	public Array<Enemy> copiarEnemigos(MainGame game){
		Array<Enemy> enemies = new Array<Enemy>();
		for(int i = 0; i < enemiesPositions.size; i++ ) {
			enemies.add(new Enemy(game, world, enemiesPositions.get(i).x, enemiesPositions.get(i).x, i));
		}
		return enemies;
	}
}
