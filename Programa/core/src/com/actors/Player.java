package com.actors;

import static com.game.MainGame.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends Actor {

	public World world;
	public Body body;

	private Texture playerTexture;
	private TextureRegion region;

	private Viewport gameport;

	// Animations
	public PlayerStates states;
	public PlayerStates direction;
	private Animation<TextureRegion> movingRight;
	private Animation<TextureRegion> movingLeft;
	private Animation<TextureRegion> movingBack;
	private Animation<TextureRegion> movingFront;
	private float stateTimer;
	private PlayerStates currentState;
	private PlayerStates previousState;

	public Player(World world, Viewport gameport) {
		this.playerTexture = new Texture("player.png");
		this.world = world;
		this.gameport = gameport;

		this.region = new TextureRegion(playerTexture, 0, 0, 32, 48); // En el sprite sheet empieza en x = 16 y y = 908.
																		// Pj de 32x52
		setWidth(this.region.getRegionWidth());
		setHeight(this.region.getRegionHeight());

		setPosition((Gdx.graphics.getWidth() / 2) - (getWidth() / 2),
				(Gdx.graphics.getHeight() / 2) - (getHeight() / 2));

		definePlayerBody();

		// Animation
		direction = PlayerStates.FRONT;
		currentState = PlayerStates.FRONT;
		previousState = PlayerStates.FRONT;
		stateTimer = 0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(this.playerTexture, i * 64, 0, 32, 48));
			System.out.println("Posicion: " + (i * 32));
		}
		movingBack = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(this.playerTexture, i * 64, 63, 32, 48));
		}
		movingLeft = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(this.playerTexture, i * 64, 125, 32, 48));

		}
		movingFront = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();
		for (int i = 0; i < 9; i++) {
			frames.add(new TextureRegion(this.playerTexture, i * 64, 191, 32, 48));
		}
		movingRight = new Animation<TextureRegion>(0.1f, frames);
		frames.clear();

	}

	public void definePlayerBody() {
		BodyDef bdef = new BodyDef();
		//bdef.position.set((Gdx.graphics.getWidth() / 2) - (getWidth() / 2),
			//	(Gdx.graphics.getHeight() / 2) - (getHeight() / 2));
		bdef.position.set(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getWidth() / PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;

		body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((getWidth() / 2) / PPM, (getHeight() / 2) / PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
	}

	@Override
	public void act(float delta) {
		// NO SE HACE ASI. ES UNA PRUEBA
		//
		//
		// if(Gdx.input.isKeyPressed(Keys.W)) {
		// setY(getY()+1);
		// }
		// if(Gdx.input.isKeyPressed(Keys.S)) {
		// setY(getY()-1);
		// }
		// if(Gdx.input.isKeyPressed(Keys.A)) {
		// setX(getX()-1);
		// }
		// if(Gdx.input.isKeyPressed(Keys.D)) {
		// setX(getX()+1);
		// }
		//
		//
		this.region.setRegion(getFrame(delta));

	}

	public TextureRegion getFrame(float delta) {
		currentState = getState();

		TextureRegion textureRegion;

		switch (currentState) {
		case FRONT:
			textureRegion = movingFront.getKeyFrame(stateTimer, true);
			break;
		case BACK:
			textureRegion = movingBack.getKeyFrame(stateTimer, true);
			break;
		case RIGHT:
			textureRegion = movingRight.getKeyFrame(stateTimer, true);
			break;
		case LEFT:
			textureRegion = movingLeft.getKeyFrame(stateTimer, true);
			break;
		default:
			textureRegion = movingFront.getKeyFrame(stateTimer, true);
			break;
		}

		stateTimer = currentState == previousState ? stateTimer + delta : 0;

		previousState = currentState;

		return textureRegion;
	}

	public PlayerStates getState() {
		if (body.getLinearVelocity().x > 0 || direction.equals(PlayerStates.RIGHT)) {
			return PlayerStates.RIGHT;
		} else if (body.getLinearVelocity().x < 0 || direction.equals(PlayerStates.LEFT)) {
			return PlayerStates.LEFT;
		} else if (body.getLinearVelocity().y > 0 || direction.equals(PlayerStates.BACK)) {
			return PlayerStates.BACK;
		} else {
			return PlayerStates.FRONT;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(region, body.getPosition().x/PPM  + getX()   , body.getPosition().y /PPM + getY()  );
		
		System.out.println( "x "  +getX() + "y: " + getY());
	}

	public void dispose() {
		playerTexture.dispose();
	}

}
