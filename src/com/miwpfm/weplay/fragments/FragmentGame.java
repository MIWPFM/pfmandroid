package com.miwpfm.weplay.fragments;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;
import com.miwpfm.weplay.util.Utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentGame extends Fragment {
	String gameId;
	String slat, slng, dlat, dlng;
	TextView textSport, textDate, textVacancies, textCenter, textCity,
			textPrice, textUnsuscribeDate;
	Button btnHowToGo;
	GameTask task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(getString(R.string.menu_option_show_game));
		Bundle args = getArguments();
		if (args != null && args.containsKey("gameId")) {
			gameId = args.getString("gameId");
		}
		View view = inflater.inflate(R.layout.fragment_game, container, false);
		textSport = (TextView) view.findViewById(R.id.textSport);
		textDate = (TextView) view.findViewById(R.id.textDate);
		textVacancies = (TextView) view.findViewById(R.id.textVacancies);
		textCenter = (TextView) view.findViewById(R.id.textCenter);
		textCity = (TextView) view.findViewById(R.id.textCity);
		textPrice = (TextView) view.findViewById(R.id.textPrice);
		textUnsuscribeDate = (TextView) view
				.findViewById(R.id.textUnsuscribeDate);
		btnHowToGo = (Button) view.findViewById(R.id.btnHowToGo);

		btnHowToGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCurrentLocation();
				if (slat == null || slng == null) {
					Toast.makeText(getActivity(),
							"No ha sido posible localizarle",
							Toast.LENGTH_SHORT).show();
				} else if (dlat == null || slng == null) {
					Toast.makeText(getActivity(),
							"El partido no tiene localización asignada",
							Toast.LENGTH_SHORT).show();
				} else {
					String uri = "http://maps.google.com/maps?saddr=" + slat
							+ "," + slng + "&daddr=" + dlat + "," + dlng;
					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(uri));
					intent.setClassName("com.google.android.apps.maps",
							"com.google.android.maps.MapsActivity");
					startActivity(intent);
				}
			}
		});

		task = new GameTask();
		task.execute((Void) null);

		return view;
	}

	public void getCurrentLocation() {
		LocationManager lm = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		Criteria c = new Criteria();
		String provider = lm.getBestProvider(c, false);
		Location location = lm.getLastKnownLocation(provider);
		if (location != null) {
			slng = Double.toString(location.getLongitude());
			slat = Double.toString(location.getLatitude());
		} else {
			slng = null;
			slat = null;
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class GameTask extends AsyncTask<Void, Void, Boolean> {

		RestClient gameInfoClient;
		JSONObject gameInfo = null;
		private ProgressDialog dialog;

		public GameTask() {
			super();
			gameInfoClient = new RestClient(Parameters.API_URL + "game/"
					+ gameId);
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getString(R.string.weplay));
			dialog.setMessage(getString(R.string.loading_game));
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid = false;
			try {

				gameInfoClient.Execute(RestClient.RequestMethod.GET);
				switch (gameInfoClient.getResponseCode()) {
				case 200:
					gameInfo = gameInfoClient.getJsonResponse();

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

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				SimpleDateFormat formatterIn = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				SimpleDateFormat formatterOut = new SimpleDateFormat(
						"dd/MM/yyyy");
				JSONObject sportObject;
				JSONObject centerObject;
				JSONObject addressObject;
				JSONObject coordinatesObject;
				JSONArray playersArray;
				String sport = "";
				String date = "";
				String vacancies = "";
				String center = "";
				String city = "";
				String price = "";
				String unsuscribeDate = "";
				dialog.dismiss();
				try {
					sportObject = gameInfo.getJSONObject("sport");
					centerObject = gameInfo.getJSONObject("center");
					addressObject = centerObject.getJSONObject("address");
					coordinatesObject = addressObject
							.getJSONObject("coordinates");
					dlat = Double.toString(coordinatesObject.getDouble("x"));
					dlng = Double.toString(coordinatesObject.getDouble("y"));
					playersArray = gameInfo.getJSONArray("players");
					sport = sportObject.getString("name");
					date = Utils.formatDate(gameInfo.getString("game_date"));
					int numPlayers = playersArray.length() + 1;
					vacancies = numPlayers + "/"
							+ gameInfo.getInt("num_players");
					center = centerObject.getString("name");
					city = addressObject.getString("city");
					price = gameInfo.getString("price") + "€";
					unsuscribeDate = Utils.formatDate(gameInfo
							.getString("limit_date"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				textSport.setText(sport);
				textDate.setText(date);
				textVacancies.setText(vacancies);
				textCenter.setText(center);
				textCity.setText(city);
				textPrice.setText(price);
				textUnsuscribeDate.setText(unsuscribeDate);
			}
		}

		@Override
		protected void onCancelled() {

		}
	}
}
