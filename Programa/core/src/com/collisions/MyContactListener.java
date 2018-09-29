package com.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.collisions.userdata.UserData;

public class MyContactListener implements ContactListener {

	private boolean isColliding;
	private boolean playerCanAttack;
	private int enemyToAttack;

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		UserData userDataA = (UserData) fixtureA.getUserData();
		UserData userDataB = (UserData) fixtureB.getUserData();
		
		System.out.println(userDataA.type);
		System.out.println(userDataB.type);
		
		//
		isColliding = true;
		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")){
			playerCanAttack = true;
			enemyToAttack = userDataB.index;
		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")){
			playerCanAttack = true;
			enemyToAttack = userDataA.index;
		}
	}

	@Override
	public void endContact(Contact contact) {
		UserData userDataA = (UserData) contact.getFixtureA().getUserData();
		UserData userDataB = (UserData) contact.getFixtureB().getUserData();
		
		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")){
			playerCanAttack = false;
		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")){
			playerCanAttack = false;
		}

	}

	public int getEnemyToAttack() {
		return enemyToAttack;
	}

	public boolean playerCanAttack() {
		return playerCanAttack;
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
