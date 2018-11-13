package com.services.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.services.collision.userdata.UserData;

public class MyContactListener implements ContactListener {

	private boolean isColliding;
	private int enemyCollided, enemyIndex;
	private boolean enemyColliding;
	private String enemyCollidingTo;

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		UserData userDataA = (UserData) fixtureA.getUserData();
		UserData userDataB = (UserData) fixtureB.getUserData();

		//
		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")) {
			isColliding = true;
			enemyCollided = userDataB.index;
		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")) {
			isColliding = true;
			enemyCollided = userDataA.index;
		}
		if(userDataA.sensor) {
			enemyColliding = true;
			enemyCollidingTo = userDataA.type;
		}
		if(userDataB.sensor) {
			enemyColliding = true;
			enemyCollidingTo = userDataB.type;
		}
	}

	@Override
	public void endContact(Contact contact) {
		UserData userDataA = (UserData) contact.getFixtureA().getUserData();
		UserData userDataB = (UserData) contact.getFixtureB().getUserData();

		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")) {
			isColliding = false;
			enemyCollided = userDataB.index;

		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")) {
			isColliding = false;
			enemyCollided = userDataA.index;
		}

		if(userDataA.sensor) {
			enemyIndex = userDataA.index;
			enemyColliding = false;
		}
		if(userDataB.sensor) {
			enemyIndex = userDataA.index;
			enemyColliding = false;
		}

	}

	public int getEnemyCollided() {
		return enemyCollided;
	}
	
	public int getEnemyIndex() {
		return enemyIndex;
	}

	public boolean isColliding() {
		return isColliding;
	}
	
	public String getEnemyCollidingTo() {
		return enemyCollidingTo;
	}

	public boolean isEnemyColliding() {
		return enemyColliding;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
