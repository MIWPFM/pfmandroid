package com.miwpfm.weplay.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.model.Sport;

public class SportListAdapter extends ArrayAdapter<Sport> {	
	private final Context context;
	private final ArrayList<Sport> sports;
	
	static class ViewHolder {
	    public TextView name;
	    public TextView level;
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
		      viewHolder.level = (TextView) rowView.findViewById(R.id.level);
		      rowView.setTag(viewHolder);
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    Sport sport = sports.get(position);
	    holder.name.setText(sport.getName());
	    holder.level.setText(sport.getLevel());
	    
	    return rowView;
	  }
	
}
