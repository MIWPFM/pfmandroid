package com.miwpfm.weplay.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.fragments.FragmentUserInfo.UserInfoTask;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

public class FragmentUserLocation extends Fragment {
	TextView text;
	UserLocationTask task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_user_location, null,
				false);
		text = (TextView) view.findViewById(R.id.section_label);

		task = new UserLocationTask();
		task.execute((Void) null);

		return view;
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLocationTask extends AsyncTask<Void, Void, Boolean> {

		RestClient userLocationClient;
		JSONObject userLocation = null;

		public UserLocationTask() {
			super();

			userLocationClient = new RestClient(Parameters.API_URL + "me");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid = false;
			try {

				userLocationClient.Execute(RestClient.RequestMethod.GET);
				Log.e("AAAAAAAAA",
						String.valueOf(userLocationClient.getResponseCode()));
				switch (userLocationClient.getResponseCode()) {
				case 200:
					userLocation = userLocationClient.getJsonResponse();

					valid = true;
					break;
				case 404:
					text.setText("Lo siento, no ha sido posible cargar sus datos.");
					break;
				default:
					text.setText("Lo siento, no ha sido posible cargar sus datos.");
				}

			} catch (Exception e) {
				return false;
			}

			return valid;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			JSONObject addressObject;
			JSONObject coordinatesObject;
			String address = null;
			String city = null;
			String community = null;
			String province = null;
			Long x = (long) 0.00;
			Long y = (long) 0.00;
			try {
				addressObject = userLocation.getJSONObject("address");
				
				Log.e("AAAAAAAAAA", userLocation.toString());

				address = addressObject.getString("address");
				city = addressObject.getString("city");
				community = addressObject.getString("community");
				province = addressObject.getString("province");
				coordinatesObject = addressObject.getJSONObject("coordinates");
				Log.e("AAAAAAAAAA", coordinatesObject.toString());
				x = coordinatesObject.getLong("x");
				y = coordinatesObject.getLong("y");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			text.setText(address + " " + city + " " + community + " "
					+ province + "x: " + Long.toString(x) + ", y: " + Long.toString(y));
		}

		@Override
		protected void onCancelled() {

		}
	}
}
