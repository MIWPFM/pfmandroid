package com.miwpfm.weplay.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.adapters.SportListAdapter;
import com.miwpfm.weplay.model.Sport;
import com.miwpfm.weplay.util.HydrateObjects;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentMySports extends Fragment {
	private SportsTask task;
	private ProgressDialog dialog;
	private Activity parent;
	private ListView sportList;
	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	this.parent = getActivity();
    	this.task = new SportsTask(parent);
		this.task.execute();
		
        return inflater.inflate(R.layout.fragment_mysports, container, false);
    }
    
    public class SportsTask extends AsyncTask<Void, Void, Boolean> {		
		private Activity context;
		private RestClient sportsClient;
		private ArrayList<Sport> mySports = new ArrayList<Sport>();
		
		public SportsTask(Activity parent) {
			super();
   			this.context = parent;
   			this.sportsClient = new RestClient(Parameters.API_URL + "me/sports");
		}
		
		@Override
   		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("WePlay");
			dialog.setMessage("Cargando deportes");
			dialog.setCancelable(false);
			dialog.show();
   		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid = false;
   			try {
   				this.sportsClient.Execute(RestClient.RequestMethod.GET);
   				switch (this.sportsClient.getResponseCode()) {
	   				
   					case 200:
   						JSONObject sportJSON = null;
   						sportJSON = this.sportsClient.getJsonResponse();
   						this.mySports = HydrateObjects.getMySportsFromJSON(sportJSON);
   						valid = true;
	   					break;
	   					
	   				case 404:
	   					break;
	   					
	   				default:
   				}   				
   			} catch (Exception e) {
   				return false;
   			}
   			
   			return valid;
		}
		
		@Override
   		protected void onPostExecute(final Boolean success) {
			dialog.dismiss();   
			
   			if(success){
   				SportListAdapter adaptador = new SportListAdapter(getActivity(), this.mySports);
   				sportList = (ListView)getActivity().findViewById(R.id.my_sports_list);
				
	   			if(sportList != null){
	   				sportList.setAdapter(adaptador);
	   				/*sportList.setOnItemClickListener(new OnItemClickListener() {
				            public void onItemClick(AdapterView<?> parent, View view,
				                int position, long id) {

				            	Sport sport = (Sport) parent.getItemAtPosition(position);
				                sport.getId();
				            	Toast toast = Toast.makeText(getActivity(), sport.getId(), Toast.LENGTH_SHORT);
				                toast.show();
				            }
				     });*/
				 }
   			}
   		}
    }
}
