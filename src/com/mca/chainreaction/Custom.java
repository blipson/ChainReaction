package com.mca.chainreaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;


public class Custom extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom);
		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);


	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_button:
			EditText ballC = (EditText) findViewById(R.id.editText1);
			String myEditValue1 = ballC.getText().toString();
			int BallCount = Integer.parseInt(myEditValue1);

			EditText ballS = (EditText) findViewById(R.id.editText3);
			String myEditValue2 = ballS.getText().toString();
			float BallSize = Float.parseFloat(myEditValue2);

			EditText ballV = (EditText) findViewById(R.id.editText2);
			String myEditValue3 = ballV.getText().toString();
			float BallSpeed = Float.parseFloat(myEditValue3);

			EditText ballE = (EditText) findViewById(R.id.editText4);
			String myEditValueE = ballE.getText().toString();
			float BallSizeE = Float.parseFloat(myEditValueE);

			SharedPreferences prefs = getSharedPreferences("statePrefs", 0);
			SharedPreferences.Editor edit = prefs.edit();
			edit.remove("numSpheres");
			edit.putInt("initialNumSpheres",BallCount);
			edit.putFloat("minSize",BallSize/100.0f);
			edit.putFloat("maxSize",BallSizeE/100.0f);
			edit.putFloat("speed",BallSpeed/100.0f);
			edit.commit();

			//openNewGameDialog();
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			break;

		}
	}
}