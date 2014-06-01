package com.miwpfm.weplay.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.fragments.FragmentUserInfo.UserInfoTask;
import com.miwpfm.weplay.util.Parameters;
import com.miwpfm.weplay.util.RestClient;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentGame extends Fragment {
	private String id;
	TextView text;
	GameTask task;
	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game, container,
				false);
		text = (TextView) view.findViewById(R.id.section_label);

		task = new GameTask();
		task.execute((Void) null);
 
        return view;
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
			id = "5388be7d4ee65ce41e000029";

			gameInfoClient = new RestClient(Parameters.API_URL + "/game/" + id);
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getString(R.string.weplay));
			dialog.setMessage(getString(R.string.loading_my_info_info));
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
					Log.e("AAAAAAAAAAAAAAA", "hola0");
					Log.e("AAAAAAAAAAAAAAA", gameInfo.toString());

					valid = true;
					break;
				case 404:
					Log.e("AAAAAAAAAAAAAAA", "hola1");
					Toast.makeText(getActivity(),
							getString(R.string.error_loading),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Log.e("AAAAAAAAAAAAAAA", "hola2");
					Log.e("AAAAAAAAAAAAAAA", gameInfo.toString());
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
			/*String name = "";
			String username = "";
			String email = "";
			String birthday = "";*/
			dialog.dismiss();
			/*try {
				
				name = gameInfo.getString("name");
				username = gameInfo.getString("username");
				email = gameInfo.getString("email");
				birthday = gameInfo.getString("birthday");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			//text.setText(gameInfo.toString());
		}

		@Override
		protected void onCancelled() {

		}
	}
}
