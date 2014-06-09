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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class FragmentHome extends Fragment {
	private RecommendedGamesTask task;
	private ProgressDialog dialog;
	private Activity parent;
	private TextView txtRecommended;
	private ListView gamesList;
	private Map<String, String> myPosition = new HashMap<String, String>();
	OnGameSelectedListener mCallback;

	// Container Activity must implement this interface
	public interface OnGameSelectedListener {
		public void onGameSelected(String gameId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnGameSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnGameSelectedListener");
		}
	}
	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_home, container, false);
    	this.parent = getActivity();
    	this.initLocation(this.parent);
    	txtRecommended = (TextView) view.findViewById(R.id.txt_recommended);
    	this.task = new RecommendedGamesTask(this.parent);
    	this.task.execute(this.myPosition.get("lat"), this.myPosition.get("long"));
    	
        return view;
    }
    
    private void initLocation(Activity parent)
    {
    	Criteria c = new Criteria();
    	LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		String provider = locManager.getBestProvider(c, false);
		Location location = locManager.getLastKnownLocation(provider);
		
		if (location != null) {
			this.myPosition.put("lat", Double.toString(location.getLatitude()));
			this.myPosition.put("long", Double.toString(location.getLongitude()));
		}		
    }
    
    public class RecommendedGamesTask extends AsyncTask <String, Void, Boolean> {		
		private Activity context;
		private RestClient recommendedGamesClient;
		private ArrayList<Game> recommendedGames = new ArrayList<Game>();
		
		public RecommendedGamesTask(Activity parent) {
   			super();
   			this.context = parent;
   			this.recommendedGamesClient = new RestClient(Parameters.API_URL + "me/recommended-games");
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
   		protected Boolean doInBackground(String... params) {
   			boolean valid = false;
   			if(!myPosition.isEmpty()) {
	   			this.recommendedGamesClient.AddParam("lat", params[0]);
				this.recommendedGamesClient.AddParam("long", params[1]);
   			}
   			
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
	   			if (adaptador.getCount() == 0) {
	   				txtRecommended.setText(getString(R.string.not_found_games));
				}
				
	   			gamesList = (ListView)getActivity().findViewById(R.id.my_games_list);
				 
	   			if(gamesList != null){
					 gamesList.setAdapter(adaptador);
					 gamesList.setOnItemClickListener(new OnItemClickListener() {
				            @Override
							public void onItemClick(AdapterView<?> parent, View view,
				                int position, long id) {
								Game game = (Game) parent
										.getItemAtPosition(position);
								String gameId = game.getId();
								if (gameId != null) {
									mCallback.onGameSelected(gameId);
								} else {
									Toast.makeText(getActivity(), getString(R.string.error_loading_game), Toast.LENGTH_SHORT).show();
								}
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
