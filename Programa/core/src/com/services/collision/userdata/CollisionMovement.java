package com.services.collision.userdata;

public class CollisionMovement {

	public int index;
	public String enemyCollidingTo;
	public boolean enemyColliding;
	public int playerIndex;
	
	public CollisionMovement(int index, int playerIndex) {
		this.index = index;
		this.playerIndex = playerIndex;
	}
	
}
