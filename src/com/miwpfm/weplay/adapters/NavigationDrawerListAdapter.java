package com.miwpfm.weplay.adapters;

import com.miwpfm.weplay.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawerListAdapter extends ArrayAdapter<String> {

	  private final Context context;
	  private final String[] names;
	  private final int[] icons;

	  static class ViewHolder {
	    public TextView text;
	    public ImageView image;
	  }

	  public NavigationDrawerListAdapter(Context context, String[] names, int[] icons) {
	    super(context, R.layout.drawer_list_item, names);
	    this.context = context;
	    this.names = names;
	    this.icons = icons;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    // reuse views
	    if (rowView == null) {
	      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      rowView = inflater.inflate(R.layout.drawer_list_item, null);
	      
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.text);
	      viewHolder.image = (ImageView) rowView
	          .findViewById(R.id.icon);
	      rowView.setTag(viewHolder);
	    }

	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String str = names[position];
	    int img = icons[position];
	    holder.text.setText(str);
	    holder.image.setImageResource(img);

	    return rowView;
	  }
}
