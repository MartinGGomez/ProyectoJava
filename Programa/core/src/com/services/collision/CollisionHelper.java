package com.services.collision;

import static com.constants.Constants.PPM;

import java.util.Random;

import com.actors.Enemy;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.constants.Constants;
import com.services.collision.userdata.UserData;

public class CollisionHelper {

	private TiledMap map;
	private World world;
	
	public CollisionHelper(TiledMap map, World world) {
		this.map = map;
		this.world = world;
	}
	
	public void createMapObjects() {
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();

		for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {

			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

			Body body = world.createBody(bdef);

			shape.setAsBox((rect.getWidth() / 2) / PPM, (rect.getHeight() / 2) / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = Constants.BIT_COLLISION;
			fdef.filter.maskBits = Constants.BIT_PLAYER;
			body.createFixture(fdef).setUserData(new UserData("Map", 0));

		}
	}

	public Array<Enemy> createEnemies() {
		Random random = new Random();
		int maxEnemies = 3;

		Array<Enemy> enemies = new Array<Enemy>();
		for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			for (int i = 0; i < maxEnemies; i++) {
				float posX = random.nextInt((int) ((rect.getWidth() + rect.getX()) - rect.getX())) + rect.getX();
				float posY = random.nextInt((int) ((rect.getHeight() + rect.getY()) - rect.getY())) + rect.getY();

				enemies.add(new Enemy(world, posX / PPM, posY / PPM, i));
			}

		}
		return enemies;
	}
	
}