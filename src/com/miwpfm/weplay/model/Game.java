package com.miwpfm.weplay.model;

import java.util.Date;

public class Game {
	private String id;
	private Date gameDate;
	private int numPlayers;
	private String sport;
	private String place;
	
	public Game(Date gameDate, int numPlayers, String sport, String place) {
		super();
		this.gameDate = gameDate;
		this.numPlayers = numPlayers;
		this.sport = sport;
		this.place = place;
	}
	public Game() {
		super();
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
	
}
