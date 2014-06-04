package com.miwpfm.weplay.fragments;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import com.miwpfm.weplay.R;
import com.miwpfm.weplay.adapters.GameListAdapter;
import com.miwpfm.weplay.model.Game;
import com.miwpfm.weplay.util.HydrateObjects;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class FragmentHome extends Fragment {
	private RecommendedGamesTask task;
	private ProgressDialog dialog;
	private Activity parent;
	private ListView gamesList;
	private Map<String, String> myPosition = new HashMap<String, String>(); 
	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	this.parent = getActivity();
    	this.myPosition = this.initLocation(this.parent);
    	this.task = new RecommendedGamesTask(this.parent, this.myPosition);
		this.task.execute();
 
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    private Map<String, String> initLocation(Activity parent)
    {
    	/*LocationManager locManager = (LocationManager)parent.getSystemService(parent.LOCATION_SERVICE);
            	MyLocationListener myListener = new MyLocationListener();
        myListener.setActivity(parent);
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener)myListener);
                Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);         
        return myListener.setLocation(loc);*/
    	
    	Criteria c = new Criteria();
    	Map<String, String> locationArray = new HashMap<String, String>();
    	LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		String provider = locManager.getBestProvider(c, false);
		Location location = locManager.getLastKnownLocation(provider);
		
		if (location != null) {
			locationArray.put("lat", Double.toString(location.getLatitude()));
			locationArray.put("long", Double.toString(location.getLongitude()));
		} else
			locationArray = null;
		
		return locationArray;
    }
    
	public class RecommendedGamesTask extends AsyncTask<Void, Void, Boolean> {		
		private Activity context;
		private RestClient recommendedGamesClient;
		private ArrayList<Game> recommendedGames = new ArrayList<Game>();
		
		public RecommendedGamesTask(Activity parent, Map<String, String> position) {
   			super();
   			this.context = parent;
   			if(position == null)
   				this.recommendedGamesClient = new RestClient(Parameters.API_URL + "me/recommended-games");
   			else
   				this.recommendedGamesClient = new RestClient(Parameters.API_URL + "me/recommended-games/" + position);
   		}
		
		@Override
   		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setTitle("WePlay");
			dialog.setMessage("Cargando partidos");
			dialog.setCancelable(false);
			dialog.show();
   		}
		
		@Override
   		protected Boolean doInBackground(Void... params) {
   			boolean valid = false;
   			try {
   				this.recommendedGamesClient.Execute(RestClient.RequestMethod.GET);
   				switch (this.recommendedGamesClient.getResponseCode()) {
	   				
   					case 200:
   						JSONArray recommendedJSON = null;
   						recommendedJSON = this.recommendedGamesClient.getJsonArrayResponse();
   						this.recommendedGames = (ArrayList<Game>)HydrateObjects.getRecommendedGamesFromJSON(recommendedJSON);
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
	   			GameListAdapter adaptador = new GameListAdapter(getActivity(), this.recommendedGames);
	   			gamesList = (ListView)getActivity().findViewById(R.id.my_games_list);
				 
	   			if(gamesList != null){
					 gamesList.setAdapter(adaptador);
					 gamesList.setOnItemClickListener(new OnItemClickListener() {
				            @Override
							public void onItemClick(AdapterView<?> parent, View view,
				                int position, long id) {

				                // selected item
				            	Game game = (Game) parent.getItemAtPosition(position);
				                game.getId();
				            	Toast toast = Toast.makeText(getActivity(), game.getId(), Toast.LENGTH_SHORT);
				                toast.show();

				            }
				          });
				 }
   			}
   		}

		@Override
		protected void onCancelled() {

		}
	}
}
