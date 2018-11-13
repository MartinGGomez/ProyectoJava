package com.services.collision.userdata;

public class UserData {

	public String type;
	public int index;
	public boolean sensor;
	public String sensorDirection;
	
	public UserData(String type, int index, boolean sensor) {
		this.type = type;
		this.index = index;
		this.sensor = sensor;
	}
}
