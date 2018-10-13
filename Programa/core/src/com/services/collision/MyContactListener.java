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
	private int enemyCollided;

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		UserData userDataA = (UserData) fixtureA.getUserData();
		UserData userDataB = (UserData) fixtureB.getUserData();
		
		System.out.println(userDataA.type);
		System.out.println(userDataB.type);
		
		//
		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")){
			isColliding = true;
			enemyCollided = userDataB.index;
		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")){
			isColliding = true;
			enemyCollided = userDataA.index;
		}
	}

	@Override
	public void endContact(Contact contact) {
		UserData userDataA = (UserData) contact.getFixtureA().getUserData();
		UserData userDataB = (UserData) contact.getFixtureB().getUserData();
		
		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")){
			isColliding = false;
		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")){
			isColliding = false;
		}

	}

	public int getEnemyCollided() {
		return enemyCollided;
	}

	public boolean isColliding() {
		return isColliding;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
