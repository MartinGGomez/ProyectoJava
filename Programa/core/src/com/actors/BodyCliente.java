package com.actors;

import com.badlogic.gdx.math.Vector2;

public class BodyCliente {

	private Vector2 position;
	
	public Vector2 getPosition() {
		return this.position;
	}	
	
	public void setX(float x) {
		this.position.x = x;
	}
	
	public void setY(float y) {
		this.position.y = y;
	}
	
	public Vector2 getLinearVelocity() {
		return null;
	}
	
}
