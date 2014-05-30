package com.miwpfm.weplay.fragments;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.activities.LoginActivity.UserLoginTask;
import com.miwpfm.weplay.model.User;
import com.miwpfm.weplay.security.WsseToken;
//import com.miwpfm.weplay.util.HydrateObjects;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

import android.app.Fragment;
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
import android.widget.TextView;

public class FragmentHome extends Fragment {
	RecommendedGamesTask task;	
	protected TextView lblEstado;
	protected TextView lblLatitud;
	protected TextView lblLongitud;
	protected TextView lblPrecision;
	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	this.initLocation();
    	
    	//task = new RecommendedGamesTask();
		//task.execute((Void) null);
 
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    private void initLocation()
    {
        //Obtenemos una referencia al LocationManager
        LocationManager locManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    	List<String> listaProviders = locManager.getAllProviders();
    	 
    	LocationProvider provider = locManager.getProvider(listaProviders.get(0));
    	int precision = provider.getAccuracy();
    	boolean obtieneAltitud = provider.supportsAltitude();
    	int consumoRecursos = provider.getPowerRequirement();
    	
    	Criteria req = new Criteria();
    	req.setAccuracy(Criteria.ACCURACY_FINE);
    	req.setAltitudeRequired(true);
    	
    	//Mejor proveedor por criterio
    	String mejorProviderCrit = locManager.getBestProvider(req, false);    	 
    	//Lista de proveedores por criterio
    	List<String> listaProvidersCrit = locManager.getProviders(req, false);
    	
    	//Si el GPS no está habilitado
    	if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
    	     mostrarAvisoGpsDeshabilitado();
    	} else {
    		
    		//Nos registramos para recibir actualizaciones de la posición
    		LocationListener locListener = new LocationListener() {    			 
    		    public void onLocationChanged(Location location) {
    		        mostrarPosicion(location);
    		    }    		 
    		    public void onProviderDisabled(String provider){
    		        lblEstado.setText("Provider OFF");
    		    }    		 
    		    public void onProviderEnabled(String provider){
    		        lblEstado.setText("Provider ON");
    		    }    		 
    		    public void onStatusChanged(String provider, int status, Bundle extras){
    		        lblEstado.setText("Provider Status: " + status);
    		    }
    		};
    		
    		//Obtenemos la última posición conocida
            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);         
            //Mostramos la última posición conocida
            mostrarPosicion(loc);
            
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);
    	}
    	
    }
    
    private void mostrarPosicion(Location loc) {
        if(loc != null) {
            lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
            lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
            lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
        } else {
            lblLatitud.setText("Latitud: (sin_datos)");
            lblLongitud.setText("Longitud: (sin_datos)");
            lblPrecision.setText("Precision: (sin_datos)");
        }
    }

	private void mostrarAvisoGpsDeshabilitado() {
		
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class RecommendedGamesTask extends AsyncTask<Void, Void, Boolean> {
		
		RestClient recommendedGamesClient;
		RestClient login;
		
		public RecommendedGamesTask() {
			super();
			recommendedGamesClient = new RestClient(Parameters.API_URL+"me/recommended-games");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid=false;
			JSONObject recommendedGames = null;
			try {

				recommendedGamesClient.Execute(RestClient.RequestMethod.GET);
				Log.w("Juegos Recomendados", String.valueOf(recommendedGamesClient.getResponseCode()));
				switch (recommendedGamesClient.getResponseCode()) {
					case 200:						
						recommendedGames=recommendedGamesClient.getJsonResponse();						
						valid=true;
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
			
		}

		@Override
		protected void onCancelled() {

		}
	}
}
