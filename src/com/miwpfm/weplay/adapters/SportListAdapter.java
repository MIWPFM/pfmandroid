package com.miwpfm.weplay.adapters;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.model.Sport;

public class SportListAdapter extends ArrayAdapter<Sport> {	
	private final Context context;
	private final ArrayList<Sport> sports;
	
	static class ViewHolder {
	    public TextView name;
	    public ImageView level;
	}
	
	public SportListAdapter(Context context, ArrayList<Sport> sports) {
	    super(context, R.layout.sport_list_element, sports);
	    this.context = context;
	    this.sports = sports;
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
		      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		      rowView = inflater.inflate(R.layout.sport_list_element, null);
		      
		      ViewHolder viewHolder = new ViewHolder();
		      viewHolder.name = (TextView) rowView.findViewById(R.id.name);
		      viewHolder.level = (ImageView) rowView.findViewById(R.id.level);
		      rowView.setTag(viewHolder);
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    Sport sport = sports.get(position);
	    String imageName = "star_" + String.valueOf(sport.getLevel());
	    int imageId = context.getResources().getIdentifier(imageName,
                "drawable", context.getPackageName());
	    holder.name.setText(sport.getName());
	    holder.level.setImageResource(imageId);
	    
	    return rowView;
	  }
	
}
