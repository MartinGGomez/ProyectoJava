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
	private int  enemyIndex;
	private boolean enemyColliding;
	private String enemyCollidingTo;
	private int playerIndex;
	private boolean isCollidingToChest;
	private int chestIndex;
	private boolean isCollidingToPlayer;
	
	public ArrayList<CollisionMovement> enemiesColliding = new ArrayList<CollisionMovement>();
	public ArrayList<Integer> enemiesStopColliding = new ArrayList<Integer>();
	public ArrayList<CollisionMovement> enemiesCollidingWithPlayer = new ArrayList<CollisionMovement>();
	public ArrayList<Integer> enemiesStopCollidingWithPlayer = new ArrayList<Integer>();

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		UserData userDataA = (UserData) fixtureA.getUserData();
		UserData userDataB = (UserData) fixtureB.getUserData();

		//
		if(userDataA.type.equals("Player") && userDataB.type.equals("Player")) {
			isCollidingToPlayer = true;
		}
		if(userDataB.type.equals("Player") && userDataA.type.equals("Player")) {
			isCollidingToPlayer = true;
		}
		
		//
		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")) {
			isColliding = true;
			enemyColliding = true;
			enemyIndex = userDataB.index;
			playerIndex = userDataA.index;
			boolean existe = false;
			for (int i = 0; i < enemiesCollidingWithPlayer.size(); i++) {
				if (enemiesCollidingWithPlayer.get(i).index == userDataB.index) {
					existe = true;
				}
			}
			if (!existe) {
				CollisionMovement c = new CollisionMovement(userDataB.index, userDataA.index);
				enemiesCollidingWithPlayer.add(c);
			}

			for (int i = 0; i < enemiesStopCollidingWithPlayer.size(); i++) {
				if (userDataB.index == enemiesStopCollidingWithPlayer.get(i)) {
					enemiesStopCollidingWithPlayer.remove(i);
				}
			}

		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")) {
			isColliding = true;
			enemyColliding = true;
			enemyIndex = userDataA.index;
			playerIndex = userDataB.index;
			boolean existe = false;
			for (int i = 0; i < enemiesCollidingWithPlayer.size(); i++) {
				if (enemiesCollidingWithPlayer.get(i).index == userDataA.index) {
					existe = true;
				}
			}
			if (!existe) {
				CollisionMovement c = new CollisionMovement(userDataA.index, userDataB.index);
				enemiesCollidingWithPlayer.add(c);
			}

			for (int i = 0; i < enemiesStopCollidingWithPlayer.size(); i++) {
				if (userDataA.index == enemiesStopCollidingWithPlayer.get(i)) {
					enemiesStopCollidingWithPlayer.remove(i);
				}
			}

		}

		if (fixtureA.getRestitution() != 0 || fixtureB.getRestitution() != 0) {
			if (userDataB.type.equals("Enemy")) {
				for (int i = 0; i < enemiesStopColliding.size(); i++) {
					if (userDataB.index == enemiesStopColliding.get(i)) {
						enemiesStopColliding.remove(i);
					}
				}
			}
			if (userDataA.type.equals("Enemy")) {
				for (int i = 0; i < enemiesStopColliding.size(); i++) {
					if (userDataA.index == enemiesStopColliding.get(i)) {
						enemiesStopColliding.remove(i);
					}
				}
			}

			if (userDataA.type.equals("Enemy") && !userDataB.type.equals("Player")) {
				boolean existe = false;
				for (int i = 0; i < enemiesColliding.size(); i++) {
					if (enemiesColliding.get(i).index == userDataA.index) {
						existe = true;
					}
				}
				if (!existe) {
					CollisionMovement c = new CollisionMovement(userDataA.index, userDataB.index);
					c.enemyCollidingTo = restitutionToDirection(fixtureA.getRestitution());
//					System.out.println("Colliding to " + c.enemyCollidingTo);
					enemiesColliding.add(c);
				}

			}
			if (userDataB.type.equals("Enemy") && !userDataA.type.equals("Player")) {
				boolean existe = false;
				for (int i = 0; i < enemiesColliding.size(); i++) {
					if (enemiesColliding.get(i).index == userDataB.index) {
						existe = true;
					}
				}
				if (!existe) {
					CollisionMovement c = new CollisionMovement(userDataB.index, userDataA.index);
					c.enemyCollidingTo = restitutionToDirection(fixtureB.getRestitution());
//					System.out.println("Colliding to " + c.enemyCollidingTo);
					enemiesColliding.add(c);
				}
			}
		}

	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		UserData userDataA = (UserData) contact.getFixtureA().getUserData();
		UserData userDataB = (UserData) contact.getFixtureB().getUserData();

		//
		if(userDataA.type.equals("Player") && userDataB.type.equals("Player")) {
			isCollidingToPlayer = false;
		}
		if(userDataB.type.equals("Player") && userDataA.type.equals("Player")) {
			isCollidingToPlayer = false;
		}
		//
		
		if (userDataA.type.equals("Player") && userDataB.type.equals("Enemy")) {
			enemyColliding = false;
			isColliding = false;

			boolean remove = false;
			int toRemove = 0;
			for (int i = 0; i < enemiesCollidingWithPlayer.size(); i++) {
				if (enemiesCollidingWithPlayer.get(i).index == userDataB.index) {
					remove = true;
					toRemove = i;
				}
			}
			if (remove) {
				enemiesCollidingWithPlayer.remove(toRemove);
			}
			enemiesStopCollidingWithPlayer.add(userDataB.index);

		}
		if (userDataB.type.equals("Player") && userDataA.type.equals("Enemy")) {
			isColliding = false;
			enemyColliding = false;

			boolean remove = false;
			int toRemove = 0;
			for (int i = 0; i < enemiesCollidingWithPlayer.size(); i++) {
				if (enemiesCollidingWithPlayer.get(i).index == userDataA.index) {
					remove = true;
					toRemove = i;
				}
			}
			if (remove) {
				enemiesCollidingWithPlayer.remove(toRemove);
			}

			enemiesStopCollidingWithPlayer.add(userDataA.index);

		}

		if (fixtureA.getRestitution() != 0 || fixtureB.getRestitution() != 0) {
			if (userDataA.type.equals("Enemy") && !userDataB.type.equals("Player")) {
				boolean remove = false;
				int toRemove = 0;
				for (int i = 0; i < enemiesColliding.size(); i++) {
					if (enemiesColliding.get(i).index == userDataA.index) {
						remove = true;
						toRemove = i;
					}
				}
				if (remove) {
					enemiesColliding.remove(toRemove);
				}
				enemiesStopColliding.add(userDataA.index);

			}
			if (userDataB.type.equals("Enemy") && !userDataA.type.equals("Player")) {
				boolean remove = false;
				int toRemove = 0;
				for (int i = 0; i < enemiesColliding.size(); i++) {
					if (enemiesColliding.get(i).index == userDataB.index) {
						remove = true;
						toRemove = i;
					}
				}
				if (remove) {
					enemiesColliding.remove(toRemove);
				}
				enemiesStopColliding.add(userDataB.index);

			}
		}

	}

	private String restitutionToDirection(float restitution) {
		String direction = "";
		// Usamos Restitution para identificar de que lado colisiona. Es un dato
		// interno. (UserData no anda en diferentes fixtures)
		// No tiene nada que ver con la propiedad restitution en si.
		// Restitution: 1-Bot 2-Top 3-Right 4-Left
		switch ((int) restitution) {
		case 1:
			direction = "Bot";
			break;
		case 2:
			direction = "Top";
			break;
		case 3:
			direction = "Right";
			break;
		case 4:
			direction = "Left";
			break;
		}
		return direction;
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
	
	public boolean isCollidingToPlayer() {
		return isCollidingToPlayer;
	}
	
	public int getPlayerIndex() {
		return playerIndex;
	}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
