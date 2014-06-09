package com.miwpfm.weplay.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.miwpfm.weplay.model.Game;
import com.miwpfm.weplay.model.Sport;

public class HydrateObjects {

	public static ArrayList<Game> getGamesFromJSON(JSONArray json)
			throws JSONException {

		ArrayList<Game> games = new ArrayList<Game>();

		for (int i = 0; i < json.length(); i++) {
			JSONObject value = (JSONObject) json.get(i);			

			try {

				Game game = new Game();
				if (value.has("id")) {
					game.setId(value.getString("id"));
				}
				if (value.has("num_players")) {
					game.setMaxPlayers(value.getInt("num_players"));
				}
				if (value.has("game_date")) {
					DateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss");
					String stringDate = value.getString("game_date");

					try {
						Date date = df.parse(stringDate);
						game.setGameDate(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if (value.has("center")) {
					JSONObject center = (JSONObject) value.get("center");
					if (center.has("name")) {
						game.setPlace(center.getString("name"));
					}
				}
				if (value.has("sport")) {
					JSONObject sport = (JSONObject) value.get("sport");
					if (sport.has("name")) {
						game.setSport(sport.getString("name"));
					}
				}
				if (value.has("players")) {
					JSONArray players = value.getJSONArray("players");
					game.setNumPlayers(players.length() + 1);
				}
				games.add(game);

			} catch (JSONException e) {

			}
		}
		return games;

	}

	public static ArrayList<Game> getRecommendedGamesFromJSON(
			JSONArray jsonGames) {
		ArrayList<Game> recommended = new ArrayList<Game>();

		for (int i = 0; i < jsonGames.length(); i++) {
			try {
				JSONObject row = jsonGames.getJSONObject(i);
				Game game = new Game();
				if (row.has("game")) {
					JSONObject JSONgame = (JSONObject) row.get("game");
					if (JSONgame.has("id")) {
						game.setId(JSONgame.getString("id"));
					}
					if (JSONgame.has("num_players")) {
						game.setMaxPlayers(JSONgame.getInt("num_players"));
					}
					if (JSONgame.has("players")) {
						JSONArray players = JSONgame.getJSONArray("players");
						game.setNumPlayers(players.length() + 1);
					}
					if (JSONgame.has("sport")) {
						JSONObject sport = (JSONObject) JSONgame.get("sport");
						if (sport.has("name")) {
							game.setSport(sport.getString("name"));
						}
					}
					if (JSONgame.has("center")) {
						JSONObject center = (JSONObject) JSONgame.get("center");
						if (center.has("name")) {
							game.setPlace(center.getString("name"));
						}
					}
					if (JSONgame.has("game_date")) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						String stringDate = JSONgame.getString("game_date");
						try {
							Date date = df.parse(stringDate);
							game.setGameDate(date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				if (row.has("distance")) {
					game.setDistance(Float.valueOf(row.getString("distance")));
				}
				recommended.add(game);

			} catch (JSONException e) {

			}
		}
		return recommended;
	}

	public static ArrayList<Sport> getMySportsFromJSON(JSONObject jsonSports) {
		ArrayList<Sport> sports = new ArrayList<Sport>();

		try {
			if (jsonSports.has("mySports")) {
				JSONArray mySports = jsonSports.getJSONArray("mySports");

				for (int i = 0; i < mySports.length(); i++) {
					JSONObject row = mySports.getJSONObject(i);
					Sport mysport = new Sport();
					if (row.has("sport")) {
						JSONObject sport = (JSONObject) row.get("sport");
						if (sport.has("name")) {
							mysport.setName(sport.getString("name"));
						}
					}
					if (row.has("level")) {
						mysport.setLevel(row.getInt("level"));
					}
					sports.add(mysport);
				}
			}
		} catch (JSONException e) {

		}
		return sports;
	}

}
