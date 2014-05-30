package com.miwpfm.weplay.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

public class FragmentUserLocation extends Fragment {
	EditText editAddress;
	EditText editCity;
	EditText editProvince;
	EditText editCommunity;
	UserLocationTask task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_user_location, null,
				false);
		editAddress = (EditText) view.findViewById(R.id.editAddress);
		editCity = (EditText) view.findViewById(R.id.editCity);
		editProvince = (EditText) view.findViewById(R.id.editProvince);
		editCommunity = (EditText) view.findViewById(R.id.editCommunity);

		task = new UserLocationTask();
		task.execute((Void) null);

		return view;
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLocationTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog dialog;
		RestClient userLocationClient;
		JSONObject userLocation = null;

		public UserLocationTask() {
			super();

			userLocationClient = new RestClient(Parameters.API_URL + "me");
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getString(R.string.weplay));
			dialog.setMessage(getString(R.string.loading_my_info_location));
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid = false;
			try {

				userLocationClient.Execute(RestClient.RequestMethod.GET);
				switch (userLocationClient.getResponseCode()) {
				case 200:
					userLocation = userLocationClient.getJsonResponse();

					valid = true;
					break;
				case 404:
					Toast.makeText(getActivity(),
							getString(R.string.error_loading),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(getActivity(),
							getString(R.string.error_loading),
							Toast.LENGTH_SHORT).show();
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
			dialog.dismiss();
			Long x = (long) 0.00;
			Long y = (long) 0.00;
			try {
				addressObject = userLocation.getJSONObject("address");
				address = addressObject.getString("address");
				city = addressObject.getString("city");
				community = addressObject.getString("community");
				province = addressObject.getString("province");
				coordinatesObject = addressObject.getJSONObject("coordinates");
				x = coordinatesObject.getLong("x");
				y = coordinatesObject.getLong("y");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			editAddress.setText(address);
			editCity.setText(city);
			editProvince.setText(province);
			editCommunity.setText(community);
		}

		@Override
		protected void onCancelled() {

		}
	}
}
