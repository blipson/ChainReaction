package com.mca.chainreaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ScoreScreen extends Activity implements OnClickListener {
	int score;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_screen);
		View shareButton = findViewById(R.id.share_button);
		shareButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
		score = getSharedPreferences("statePrefs", 0).getInt("score", 0);
		getSharedPreferences("statePrefs", 0).edit().clear().commit();
		((TextView) findViewById(R.id.score_text)).setText("Score: "+score);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_button:
			Thread t = new Thread(new Runnable () {
				public void run () {
					Message m = new Message("FWRITE", "scr:"+score+"\n");
					m.send("rns202-4.cs.stolaf.edu",28440);
				}
			});
			t.start();
			break;
		case R.id.exit_button:
			finish();
			Intent menu_intent = new Intent(this, Collisions.class);
			startActivity(menu_intent);
			break;
		}
	}
}
