package com.services.collision;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.services.collision.userdata.CollisionMovement;
import com.services.collision.userdata.UserData;

public class MyContactListener implements ContactListener {

	private boolean isColliding;
	private int enemyCollided, enemyIndex;
	private boolean enemyColliding;
	private String enemyCollidingTo;

	public ArrayList<CollisionMovement> enemiesColliding = new ArrayList<CollisionMovement>();

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
			enemyColliding = true;
			CollisionMovement c = new CollisionMovement(userDataB.index);
			enemiesColliding.add(c);
		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")) {
			isColliding = true;
			enemyCollided = userDataA.index;
			enemyColliding = true;
			CollisionMovement c = new CollisionMovement(userDataA.index);
			enemiesColliding.add(c);
		}

		if (userDataA.type.equals("Enemy") && userDataA.sensor && !userDataB.type.equals("Player")) {
			// Chocando contra algo que no es un jugador
			
		}

	}

	@Override
	public void endContact(Contact contact) {
		UserData userDataA = (UserData) contact.getFixtureA().getUserData();
		UserData userDataB = (UserData) contact.getFixtureB().getUserData();

		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")) {
			enemyColliding = false;
			isColliding = false;
			// Buscar index del array cuando el contenido sea igual a userDataB.index
//			enemiesColliding.remove(userDataB.index);

		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")) {
			isColliding = false;
			enemyColliding = false;
			// Buscar index del array cuando el contenido sea igual a userDataB.index
//			enemiesColliding.remove(userDataB.index);
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
