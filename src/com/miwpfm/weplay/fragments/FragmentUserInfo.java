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
import com.miwpfm.weplay.util.Utils;

public class FragmentUserInfo extends Fragment {
	EditText editUsername;
	EditText editName;
	EditText editEmail;
	EditText editBirthday;
	UserInfoTask task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_info, container,
				false);
		editUsername = (EditText) view.findViewById(R.id.editUsername);
		editName = (EditText) view.findViewById(R.id.editName);
		editEmail = (EditText) view.findViewById(R.id.editEmail);
		editBirthday = (EditText) view.findViewById(R.id.editBirthday);

		task = new UserInfoTask();
		task.execute((Void) null);

		return view;
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserInfoTask extends AsyncTask<Void, Void, Boolean> {

		RestClient userInfoClient;
		JSONObject userInfo = null;
		private ProgressDialog dialog;

		public UserInfoTask() {
			super();

			userInfoClient = new RestClient(Parameters.API_URL + "me");
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

				userInfoClient.Execute(RestClient.RequestMethod.GET);
				switch (userInfoClient.getResponseCode()) {
				case 200:
					userInfo = userInfoClient.getJsonResponse();

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
				String name = "";
				String username = "";
				String email = "";
				String birthday = "";
				dialog.dismiss();
				try {
					if (userInfo.has("name")) {
						name = userInfo.getString("name");
					}
					if (userInfo.has("username")) {
						username = userInfo.getString("username");
					}
					if (userInfo.has("email")) {
						email = userInfo.getString("email");
					}
					if (userInfo.has("birthday")) {
						birthday = Utils.formatDate(userInfo
								.getString("birthday"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				editUsername.setText(username);
				editName.setText(name);
				editEmail.setText(email);
				editBirthday.setText(birthday);
			}
		}

		@Override
		protected void onCancelled() {

		}
	}
}
