package com.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	private boolean playerCanAttack;
	private int enemyToAttack;
	
	@Override
	public void beginContact(Contact contact) {
		System.out.println(contact.getFixtureA().getUserData());
		System.out.println(contact.getFixtureB().getUserData());
		//
		if(contact.getFixtureA().getUserData().equals("Player") && contact.getFixtureB().getUserData().equals("Enemy- 0")) {
			playerCanAttack = true;
			enemyToAttack = 0;
		}
		if(contact.getFixtureA().getUserData().equals("Enemy- 0") && contact.getFixtureB().getUserData().equals("Player")) {
			playerCanAttack = true;
			enemyToAttack = 0;
		}
	}

	@Override
	public void endContact(Contact contact) {
		if(contact.getFixtureA().getUserData().equals("Player") && contact.getFixtureB().getUserData().equals("Enemy- 0")) {
			playerCanAttack = false;
		}
		if(contact.getFixtureA().getUserData().equals("Enemy- 0") && contact.getFixtureB().getUserData().equals("Player")) {
			playerCanAttack = false;
		}
		
	}
	
	public int getEnemyToAttack() {
		return enemyToAttack;
	}
	
	public boolean playerCanAttack() { 
		return playerCanAttack;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}



}
