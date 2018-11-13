package com.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.services.collision.userdata.UserData;

public class SteeringEntity implements Steerable<Vector2> {

	private Body body;
	private float boundingRadius;
	private boolean tagged;
	float maxLinearSpeed, maxLinearAcceleration, maxAngularSpeed, maxAngularAcceleration;
	
	private SteeringBehavior<Vector2> behavior; 
	private SteeringAcceleration<Vector2> steeringOutput;
	
	public SteeringEntity(Body body, float boundingRadius) {
		this.body = body;
		this.boundingRadius = boundingRadius;
		
		this.maxLinearSpeed = 400;
		this.maxLinearAcceleration = 4000;
		this.maxAngularSpeed = 0;
		this.maxAngularAcceleration = 0;
		
		UserData data = (UserData) body.getFixtureList().get(0).getUserData();
		
		this.tagged = true;
		
		this.steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

	}
	
	public void update (float delta) {
		if (behavior != null) {
			behavior.calculateSteering(steeringOutput);
			applySteering(delta);
		}
	}

	private void applySteering (float delta) {
		boolean anyAccelerations = false;
		
		if(!steeringOutput.linear.isZero()) {
			Vector2 force = steeringOutput.linear.scl(delta);
			body.applyForceToCenter(force, true);
			anyAccelerations = true;
		}
		
		// ANGULAR VELOCITY NO VA PORQUE MUEVE EL ANGULO DEL BODY
//		if(steeringOutput.angular != 0) {
//			body.applyTorque(steeringOutput.angular * delta, true);
//			anyAccelerations = true;
//		} else {
//			Vector2 linVel = this.getLinearVelocity();
//			if(!linVel.isZero()) {
//				float newOrientation = vectorToAngle(linVel);
//				body.setAngularVelocity((newOrientation - getAngularVelocity()) * delta);
//				body.setTransform(body.getPosition(), newOrientation);
//			}	
//		}
//		
		if(anyAccelerations) {
			Vector2 velocity = body.getLinearVelocity();
			float currentSpeedSquare = velocity.len2();
			if(currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
				body.setLinearVelocity(velocity.scl(maxAngularSpeed /  (float) Math.sqrt(currentSpeedSquare)));
			}
			
//			if(body.getAngularVelocity() > maxAngularSpeed) {
//				body.setAngularVelocity(maxAngularSpeed);
//			}
		}
		
	}

	@Override
	public Vector2 getPosition() {
		return this.body.getPosition();
	}

	@Override
	public float getOrientation() {	
		return 0f;
	}

	@Override
	public void setOrientation(float orientation) {
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float) Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float) Math.sin(angle);
		outVector.y = (float) Math.cos(angle);
		return outVector;
	}

	@Override
	public Location<Vector2> newLocation() {
		return null;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return 0;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
	}

	@Override
	public float getMaxLinearSpeed() {
		return this.maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return this.maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return this.maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return this.maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return this.body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return this.body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return this.boundingRadius;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}

	public void setBehaviour(SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}

}
