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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miwpfm.weplay.R;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

public class FragmentUserLocation extends Fragment {
	EditText editAddress;
	EditText editCity;
	EditText editProvince;
	EditText editCommunity;
	MapView mapView;
	UserLocationTask task;
	GoogleMap map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_user_location,
				container, false);
		editAddress = (EditText) view.findViewById(R.id.editAddress);
		editCity = (EditText) view.findViewById(R.id.editCity);
		editProvince = (EditText) view.findViewById(R.id.editProvince);
		editCommunity = (EditText) view.findViewById(R.id.editCommunity);
		mapView = (MapView) view.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);

		// Gets to GoogleMap from the MapView and does initialization stuff
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.setMyLocationEnabled(true);

		MapsInitializer.initialize(this.getActivity());

		// Updates the location and zoom of the MapView
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
				new LatLng(40.430083, -3.648775), 16);
		map.animateCamera(cameraUpdate);

		task = new UserLocationTask();
		task.execute((Void) null);

		return view;
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
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

			if (success) {
				JSONObject addressObject;
				JSONObject coordinatesObject;
				String name = null;
				String address = null;
				String city = null;
				String community = null;
				String province = null;
				Double x = null;
				Double y = null;
				dialog.dismiss();
				try {
					if (userLocation.has("name")) {
						name = userLocation.getString("name");
					}
					if (userLocation.has("address")) {
						addressObject = userLocation.getJSONObject("address");
						if (addressObject.has("address")) {
							address = addressObject.getString("address");
						}
						if (addressObject.has("city")) {
							city = addressObject.getString("city");
						}
						if (addressObject.has("community")) {
							community = addressObject.getString("community");
						}
						if (addressObject.has("province")) {
							province = addressObject.getString("province");
						}
						if (addressObject.has("coordinates")) {
							coordinatesObject = addressObject
									.getJSONObject("coordinates");
							if (coordinatesObject.has("x")) {
								x = coordinatesObject.getDouble("x");
							}
							if (coordinatesObject.has("y")) {
								y = coordinatesObject.getDouble("y");
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (x != null || y != null) {
					CameraUpdate cameraUpdate = CameraUpdateFactory
							.newLatLngZoom(new LatLng(x, y), 16);
					map.animateCamera(cameraUpdate);
					@SuppressWarnings("unused")
					Marker home = map.addMarker(new MarkerOptions()
							.position(new LatLng(x, y))
							.title(name)
							.snippet(address)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.home_success)));
				}

				editAddress.setText(address);
				editCity.setText(city);
				editProvince.setText(province);
				editCommunity.setText(community);
			}
		}

		@Override
		protected void onCancelled() {

		}
	}
}
