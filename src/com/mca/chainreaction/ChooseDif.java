package com.mca.chainreaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ChooseDif extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosedif);
		// Set up click listeners facor all the buttons
		View easyButton = findViewById(R.id.easy_button);
		easyButton.setOnClickListener(this);
		View customButton = findViewById(R.id.custom_button);
		customButton.setOnClickListener(this);
		View mediumButton = findViewById(R.id.medium_button);
		mediumButton.setOnClickListener(this);
		View hardButton = findViewById(R.id.hard_button);
		hardButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		SharedPreferences prefs = getSharedPreferences("statePrefs", 0);
		SharedPreferences.Editor edit = prefs.edit();
		edit.clear();
		switch (v.getId()) {
		case R.id.easy_button:
			//openNewGameDialog();
			edit.putInt("initialNumSpheres",100).commit();
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			break;
		case R.id.medium_button:
			edit.putInt("initialNumSpheres",80).commit();
			Intent t = new Intent(this, MainActivity.class);
			startActivity(t);
			break;
		case R.id.hard_button:
			edit.putInt("initialNumSpheres",60).commit();
			Intent k = new Intent(this, MainActivity.class);
			startActivity(k);
			break;
		case R.id.custom_button:
			edit.putBoolean("isCustom",true).commit();
			Intent m = new Intent(this, Custom.class);
			startActivity(m);
			break;
		}
	}

}