package com.miwpfm.weplay.activities;

import com.miwpfm.weplay.R;
import com.miwpfm.weplay.fragments.FragmentConfiguration;
import com.miwpfm.weplay.fragments.FragmentGame;
import com.miwpfm.weplay.fragments.FragmentHome;
import com.miwpfm.weplay.fragments.FragmentMyGames;
import com.miwpfm.weplay.fragments.FragmentMyInfo;
import com.miwpfm.weplay.fragments.FragmentMyMessages;
import com.miwpfm.weplay.fragments.FragmentMySports;
import com.miwpfm.weplay.fragments.FragmentShowGames;
import com.miwpfm.weplay.fragments.NavigationDrawerFragment;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

public class HomeActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		FragmentMyGames.OnGameSelectedListener,
		FragmentShowGames.OnGameSelectedListener,
		FragmentHome.OnGameSelectedListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

		Fragment fragment = null;

		switch (position) {
		case 0:
			mTitle = getString(R.string.weplay);
			fragment = new FragmentHome();
			break;
		case 1:
			mTitle = getString(R.string.menu_option_myinfo);
			fragment = new FragmentMyInfo();
			break;
		case 2:
			mTitle = getString(R.string.menu_option_mysports);
			fragment = new FragmentMySports();
			break;
		case 3:
			mTitle = getString(R.string.menu_option_mygames);
			fragment = new FragmentMyGames();
			break;
		case 4:
			mTitle = getString(R.string.menu_option_mymessages);
			fragment = new FragmentMyMessages();
			break;
		case 5:
			mTitle = getString(R.string.menu_option_home);
			logout();
			break;
		}

		if (position != 5) {
			FragmentManager fragmentManager = getFragmentManager();

			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
		}
	}

	public void logout() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.weplay);
			break;
		case 2:
			mTitle = getString(R.string.menu_option_myinfo);
			break;
		case 3:
			mTitle = getString(R.string.menu_option_mysports);
			break;
		case 4:
			mTitle = getString(R.string.menu_option_mygames);
			break;
		case 5:
			mTitle = getString(R.string.menu_option_mymessages);
			break;
		case 6:
			mTitle = getString(R.string.menu_option_logout);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.home, menu);
			// restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Fragment fragment = null;

		int id = item.getItemId();
		if (id == R.id.find_game) {
			mTitle = getString(R.string.menu_option_showgames);
			fragment = new FragmentShowGames();

			FragmentManager fragmentManager = getFragmentManager();

			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
			restoreActionBar();
			return true;
		} else if (id == R.id.config_notif) {
			mTitle = getString(R.string.menu_option_configuration);
			fragment = new FragmentConfiguration();

			FragmentManager fragmentManager = getFragmentManager();

			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
			restoreActionBar();
			return true;
		} else if (id == R.id.share_app) {
			shareApp();
			return true;
		} else if (id == R.id.action_overflow) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void shareApp() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = getResources().getString(R.string.share_app_phrase);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Compártelo por"));
	}

	@Override
	public void onGameSelected(String gameId) {
		Fragment fragment = new FragmentGame();
		Bundle args = new Bundle();
		args.putString("gameId", gameId);
		fragment.setArguments(args);

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.container, fragment);

		transaction.commit();
	}

}
