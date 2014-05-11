package com.miwpfm.weplay;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.Activity;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.find_game:
			Toast.makeText(getApplicationContext(), "Buscar partido",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.share_app:
			Toast.makeText(getApplicationContext(), "Compartir",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.config_notif:
			Toast.makeText(getApplicationContext(),
					"Configurar Notificaciones", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
