package com.miwpfm.weplay.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.adapters.GameListAdapter;
import com.miwpfm.weplay.model.Game;
import com.miwpfm.weplay.util.HydrateObjects;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FragmentMyGames extends Fragment {
	private ProgressDialog mProgressDialog;
	private Activity parent;
	private ListView gamesList;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		parent = getActivity();
		ActionBar actionBar = parent.getActionBar();
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle(getString(R.string.menu_option_mygames));

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			RestClient gamesClient;

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction arg1) {
				String[] datos = null;
				int position;

				position = tab.getPosition();

				switch (position) {
				case 0:
					new PlayingGamesTask(parent).execute();
					break;
				case 1:
					new PlayedGamesTask(parent).execute();
					break;
				case 2:
					new OrganizedGamesTask(parent).execute();
					break;

				default:
				}

			}

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}
		};

		actionBar.removeAllTabs();
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.my_games_tab_playing))
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.my_games_tab_played))
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.my_games_tab_organized))
				.setTabListener(tabListener));

		return inflater.inflate(R.layout.fragment_mygames, container, false);
	}

	public class PlayingGamesTask extends AsyncTask<Void, Void, Boolean> {

		RestClient playingGamesClient;
		private ProgressDialog dialog;
		private Activity context;
		private ArrayList<Game> games;

		public PlayingGamesTask(Activity parent) {
			super();
			context = parent;
			playingGamesClient = new RestClient(Parameters.API_URL
					+ "me/playing-games");
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setTitle("WePlay");
			mProgressDialog.setMessage("Cargando partidos");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid = false;

			try {

				playingGamesClient.Execute(RestClient.RequestMethod.GET);
				switch (playingGamesClient.getResponseCode()) {
				case 200:
					JSONObject playingGames = null;
					playingGames = playingGamesClient.getJsonResponse();
					games = (ArrayList<Game>) HydrateObjects
							.getGamesFromJSON(playingGames);
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
			mProgressDialog.dismiss();

			if (success) {
				GameListAdapter adaptador = new GameListAdapter(getActivity(),
						games);
				gamesList = (ListView) getActivity().findViewById(
						R.id.my_games_list);
				if (gamesList != null) {
					gamesList.setAdapter(adaptador);

					gamesList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							// selected item
							Game game = (Game) parent
									.getItemAtPosition(position);
							game.getId();
							Toast toast = Toast.makeText(getActivity(),
									game.getId(), Toast.LENGTH_SHORT);
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

	public class PlayedGamesTask extends AsyncTask<Void, Void, Boolean> {

		RestClient playedGamesClient;
		private ProgressDialog dialog;
		private Activity context;
		private ArrayList<Game> games;

		public PlayedGamesTask(Activity parent) {
			super();
			context = parent;
			playedGamesClient = new RestClient(Parameters.API_URL
					+ "me/played-games");
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setTitle("WePlay");
			mProgressDialog.setMessage("Cargando partidos");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid = false;

			try {

				playedGamesClient.Execute(RestClient.RequestMethod.GET);
				switch (playedGamesClient.getResponseCode()) {
				case 200:
					JSONObject playedGames = null;
					playedGames = playedGamesClient.getJsonResponse();
					games = (ArrayList<Game>) HydrateObjects
							.getGamesFromJSON(playedGames);
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
			mProgressDialog.dismiss();

			if (success) {
				GameListAdapter adaptador = new GameListAdapter(getActivity(),
						games);
				gamesList = (ListView) getActivity().findViewById(
						R.id.my_games_list);
				if (gamesList != null) {
					gamesList.setAdapter(adaptador);
					gamesList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							// selected item
							Game game = (Game) parent
									.getItemAtPosition(position);
							String gameId = game.getId();
							Toast toast = Toast.makeText(getActivity(), gameId,
									Toast.LENGTH_SHORT);
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

	public class OrganizedGamesTask extends AsyncTask<Void, Void, Boolean> {

		RestClient organizedClient;
		private ProgressDialog dialog;
		private Activity context;
		private ArrayList<Game> games;

		public OrganizedGamesTask(Activity parent) {
			super();
			context = parent;
			organizedClient = new RestClient(Parameters.API_URL
					+ "me/organized-games");
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setTitle("WePlay");
			mProgressDialog.setMessage("Cargando partidos");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid = false;

			try {

				organizedClient.Execute(RestClient.RequestMethod.GET);
				switch (organizedClient.getResponseCode()) {
				case 200:
					JSONObject organizedGames = null;
					organizedGames = organizedClient.getJsonResponse();
					games = (ArrayList<Game>) HydrateObjects
							.getGamesFromJSON(organizedGames);
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
			mProgressDialog.dismiss();

			if (success) {
				GameListAdapter adaptador = new GameListAdapter(getActivity(),
						games);
				gamesList = (ListView) getActivity().findViewById(
						R.id.my_games_list);
				if (gamesList != null) {
					gamesList.setAdapter(adaptador);
					gamesList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// selected item
							Game game = (Game) parent
									.getItemAtPosition(position);
							String gameId = game.getId();
							Toast toast = Toast.makeText(getActivity(), gameId,
									Toast.LENGTH_SHORT);
							toast.show();
							if (gameId != null) {
								mCallback.onGameSelected(gameId);
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
