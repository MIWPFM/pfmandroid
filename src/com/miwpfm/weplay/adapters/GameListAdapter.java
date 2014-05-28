package com.miwpfm.weplay.adapters;

import java.util.ArrayList;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.model.Game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameListAdapter extends ArrayAdapter<Game> {

	  private final Context context;
	  private final ArrayList<Game> games;
	  static class ViewHolder {
	    public TextView sport;
	    public TextView place;
	    public TextView gameDate;
	    public TextView numPlayers;
	  }

	  public GameListAdapter(Context context,ArrayList<Game> games) {
	    super(context, R.layout.game_list_element,games);
	    this.context = context;
	    this.games=games;
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
	      viewHolder.numPlayers = (TextView) rowView.findViewById(R.id.numPlayers);
	      rowView.setTag(viewHolder);
	    }

	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    Game game=games.get(position);
	    holder.sport.setText(game.getSport());
	    holder.place.setText(game.getPlace());
	    holder.numPlayers.setText(String.valueOf(game.getNumPlayers()));
	    

	    return rowView;
	  }
}
