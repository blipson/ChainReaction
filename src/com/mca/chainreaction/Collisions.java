package com.mca.chainreaction;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;

//import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;


public class Collisions extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collisions);
		// Set up click listeners facor all the buttons
		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		/*View continueButton = findViewById(R.id.continue_button);
		continueButton.setOnClickListener(this);
		View customButton = findViewById(R.id.custom_button);
		customButton.setOnClickListener(this);*/
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
	}
	// ...
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_button:
			//openNewGameDialog();
			/*SharedPreferences prefs = getSharedPreferences("statePrefs", 0);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt("numSpheres",100);
		MainActivity.minSize = 0.02f;
		MainActivity.maxSize = 0.1f;
		MainActivity.speed = 0.5f;
		MainActivity.score = 0;*/
			SharedPreferences prefs = getSharedPreferences("statePrefs", 0);
			prefs.edit().clear().commit();
			Intent i = new Intent(this, ChooseDif.class);
			startActivity(i);
			break;
		case R.id.about_button:
			Intent t = new Intent(this, Info.class);
			startActivity(t);
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
			// More items go here (if any) ...
		}
		return false; 
	}

	private static final String TAG = "Collisions";

	private void openNewGameDialog() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.new_game_title)
		.setItems(R.array.difficulty,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface,int i) {
				startGame(i);
			}
		})
		.show();
	}
	private void startGame(int i) {
		Log.d(TAG, "clicked on " + i);
		// Start game here...
	}

}