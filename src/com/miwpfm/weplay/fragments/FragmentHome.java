package com.miwpfm.weplay.fragments;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class FragmentHome extends Fragment {
	private RecommendedGamesTask task;
	private ProgressDialog dialog;
	private Activity parent;
	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	//this.initLocation();
    	parent = getActivity();
    	task = new RecommendedGamesTask(parent);
		task.execute();
 
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    private void initLocation()
    {
        //Obtenemos una referencia al LocationManager
        /*LocationManager locManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    	List<String> listaProviders = locManager.getAllProviders();
    	
    	Criteria req = new Criteria();
    	req.setAccuracy(Criteria.ACCURACY_FINE);
    	req.setAltitudeRequired(true);*/
    	
    	//Mejor proveedor por criterio
    	//String mejorProviderCrit = locManager.getBestProvider(req, false);    	 
    	//Lista de proveedores por criterio
    	//List<String> listaProvidersCrit = locManager.getProviders(req, false);
    	
    	//Si el GPS no está habilitado
    	/*if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
    		showGpsDesabled();
    	} else {*/
    		
    		//Nos registramos para recibir actualizaciones de la posición
    		/*LocationListener locListener = new LocationListener() {    			 
    		    public void onLocationChanged(Location location) {
    		    	showPosition(location);
    		    }    		 
    		    public void onProviderDisabled(String provider){
    		        //lblEstado.setText("Provider OFF");
    		    }    		 
    		    public void onProviderEnabled(String provider){
    		        //lblEstado.setText("Provider ON");
    		    }    		 
    		    public void onStatusChanged(String provider, int status, Bundle extras){
    		        //lblEstado.setText("Provider Status: " + status);
    		    }
    		};*/
    		
            //locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);
            //Obtenemos la última posición conocida
            //Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);         
            //Mostramos la última posición conocida
            //showPosition(loc);
    	//}
    	
    }
    
    private void showPosition(Location loc) {
        /*if(loc != null) {
            lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
            lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
            lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
        } else {
            lblLatitud.setText("Latitud: (sin_datos)");
            lblLongitud.setText("Longitud: (sin_datos)");
            lblPrecision.setText("Precision: (sin_datos)");
        }*/
    }

	private void showGpsDesabled() {
		
	}

	public class RecommendedGamesTask extends AsyncTask<Void, Void, Boolean> {		
		private Activity context;
		RestClient recommendedGamesClient;
		private ArrayList<Game> recommendedGames = new ArrayList<Game>();
		
		public RecommendedGamesTask(Activity parent) {
   			super();
   			context = parent;
   			recommendedGamesClient = new RestClient(Parameters.API_URL + "me/recommended-games");
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
   				recommendedGamesClient.Execute(RestClient.RequestMethod.GET);
   				switch (recommendedGamesClient.getResponseCode()) {
	   				
   					case 200:
   						JSONArray recommendedJSON = null;
   						recommendedJSON = recommendedGamesClient.getJsonArrayResponse();
   						recommendedGames = (ArrayList<Game>)HydrateObjects.getRecommendedGamesFromJSON(recommendedJSON);
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
	   			GameListAdapter adaptador = new GameListAdapter(getActivity(), recommendedGames);
				ListView lstOpciones = (ListView)getActivity().findViewById(R.id.my_games_list);
				 if(lstOpciones != null)
					 	lstOpciones.setAdapter(adaptador);
   			}
   		}

		@Override
		protected void onCancelled() {

		}
	}
}
