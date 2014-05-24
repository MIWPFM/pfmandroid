package com.miwpfm.weplay.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.activities.LoginActivity.UserLoginTask;
import com.miwpfm.weplay.model.User;
import com.miwpfm.weplay.security.WsseToken;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHome extends Fragment {
	RecommendedGamesTask task;
	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	task = new RecommendedGamesTask();
		task.execute((Void) null);
 
        return inflater.inflate(R.layout.fragment_home, container, false);
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
