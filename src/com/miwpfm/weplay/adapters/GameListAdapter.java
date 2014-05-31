package com.miwpfm.weplay.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.model.Game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GameListAdapter extends ArrayAdapter<Game> {

	  private final Context context;
	  private final ArrayList<Game> games;
	  
	  static class ViewHolder {
	    public TextView sport;
	    public TextView place;
	    public TextView gameDate;
	    public TextView hourDate;
	    public TextView numPlayers;
	    public TextView distance;
	  }

	  public GameListAdapter(Context context, ArrayList<Game> games) {
	    super(context, R.layout.game_list_element, games);
	    this.context = context;
	    this.games = games;
	  }
	
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    // reuse views
	    if (rowView == null) {
	      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      rowView = inflater.inflate(R.layout.game_list_element, null);
	      
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.sport = (TextView) rowView.findViewById(R.id.sport);
	      viewHolder.place = (TextView) rowView.findViewById(R.id.place);
	      viewHolder.gameDate = (TextView) rowView.findViewById(R.id.gameDate);
	      viewHolder.hourDate = (TextView) rowView.findViewById(R.id.gameHour);
	      viewHolder.numPlayers = (TextView) rowView.findViewById(R.id.numPlayers);
	      viewHolder.distance = (TextView) rowView.findViewById(R.id.distance);
	      rowView.setTag(viewHolder);
	    }

	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    Game game = games.get(position);
	    holder.sport.setText(game.getSport());
	    holder.place.setText(game.getPlace());
	    String numPlayers=game.getNumPlayers()+"/"+game.getMaxPlayers();
	    holder.numPlayers.setText(numPlayers);
	    DateFormat dateFormat=new SimpleDateFormat("dd/MM");
	    DateFormat hourFormat=new SimpleDateFormat("HH:mm");
	    String date = dateFormat.format(game.getGameDate());
	    String hour = hourFormat.format(game.getGameDate());
	    holder.gameDate.setText(date);
	    holder.hourDate.setText(hour);	    
	    holder.distance.setText(game.getDistance().toString());
	    
	    return rowView;
	  }
}
