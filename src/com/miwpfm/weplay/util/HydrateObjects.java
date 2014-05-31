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

import com.miwpfm.weplay.model.Game;

public class HydrateObjects {
	
    public static ArrayList<Game> getGamesFromJSON(JSONObject json)
    {
    	Iterator<String> iter = json.keys();
    	ArrayList<Game> games = new ArrayList<Game>();
    	
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                JSONObject value = (JSONObject) json.get(key);
                JSONObject center =(JSONObject) value.get("center");
                JSONObject sport =(JSONObject) value.get("sport");
                JSONArray players = value.getJSONArray("players");
                
                Game game= new Game();
                game.setId(value.getString("id"));
                game.setSport(sport.getString("name"));
                game.setNumPlayers(players.length()+1);
                game.setMaxPlayers(value.getInt("num_players"));
                game.setPlace(center.getString("name"));
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String stringDate=value.getString("game_date");
                
                try {
					Date date =  df.parse(stringDate);
				    game.setGameDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}               
                games.add(game);
                
            } catch (JSONException e) {

            }
        }
		return games;    	
    }
    
    public static ArrayList<Game> getRecommendedGamesFromJSON(JSONArray jsonGames) 
    {
    	ArrayList<Game> recommended = new ArrayList<Game>();
    	
    	for (int i = 0; i < jsonGames.length(); i++) {    	
    		try {
	    		JSONObject row = jsonGames.getJSONObject(i);
	    		Game game = new Game();	   
	    		
	    		game.setDistance(row.getString("distance"));	    	    
	    	    JSONObject JSONgame = (JSONObject) row.get("game");    	    
	    	    game.setId(JSONgame.getString("id"));
	    	    
	    	    JSONArray players = JSONgame.getJSONArray("players");
	    	    game.setNumPlayers(players.length());    	    
	    	    game.setMaxPlayers(JSONgame.getInt("num_players"));
	    	    
	    	    JSONObject sport = (JSONObject) JSONgame.get("sport");
	    	    game.setSport(sport.getString("name"));    	    
	    	    JSONObject center = (JSONObject) JSONgame.get("center");
	    	    game.setPlace(center.getString("name"));	  	    
	    	    
	    	    /*	    	    
	    	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	            String stringDate = game.getString("game_date");            
	            try {
					Date date =  df.parse(stringDate);
					recommendedGame.setGameDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}*/
	    	    recommended.add(game);
	    	    
    		} catch (JSONException e) {

            }            
    	}
        
		return recommended;
    }
    
}
