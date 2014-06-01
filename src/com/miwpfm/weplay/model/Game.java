package com.miwpfm.weplay.model;

import java.util.Date;

public class Game {
	private String id;
	private Date gameDate;
	private int numPlayers;
	private int maxPlayers;
	private String sport;
	private String place;
	private float distance;
	
	public Game() {
		super();
		distance=-1;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getGameDate() {
		return gameDate;
	}
	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}

}
