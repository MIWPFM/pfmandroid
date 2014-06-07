package com.miwpfm.weplay.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentShowGames extends Fragment {
	private ProgressDialog mProgressDialog;
	private GamesTask task;
	private ListView gamesList;
	OnGameSelectedListener mCallback;

	// Container Activity must implement this interface
	public interface OnGameSelectedListener {
		public void onGameSelected(String gameId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		task = new GamesTask(getActivity());
		task.execute((Void) null);

		return inflater.inflate(R.layout.fragment_showgames, container, false);
	}

	public class GamesTask extends AsyncTask<Void, Void, Boolean> {

		RestClient gamesClient;
		private Activity context;
		private ArrayList<Game> games;

		public GamesTask(Activity parent) {
			super();
			context = parent;
			gamesClient = new RestClient(Parameters.API_URL + "me/next-games");
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setTitle("WePlay");
			mProgressDialog.setMessage("Buscando partidos");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean valid = false;

			try {

				gamesClient.Execute(RestClient.RequestMethod.GET);
				switch (gamesClient.getResponseCode()) {
				case 200:
					JSONArray gamesJson = null;
					gamesJson = gamesClient.getJsonArrayResponse();
					games = HydrateObjects.getGamesFromJSON(gamesJson);
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
				gamesList = (ListView) getActivity().findViewById(R.id.games);
				if (gamesList != null) {
					gamesList.setAdapter(adaptador);
					gamesList.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							// selected item
							Game game = (Game) parent
									.getItemAtPosition(position);
							String gameId = game.getId();
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
