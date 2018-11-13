package com.services.collision.userdata;

public class CollisionMovement {

	public int index;
	public String enemyCollidingTo;
	public boolean enemyColliding;
	
	public CollisionMovement(int index, String enemyCollidingTo, boolean enemyColliding) {
		this.index = index;
		this.enemyCollidingTo = enemyCollidingTo;
		this.enemyColliding = enemyColliding;
	}
	
}
