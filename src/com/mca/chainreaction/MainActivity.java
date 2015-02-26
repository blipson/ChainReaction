package com.mca.chainreaction;

import java.util.Random;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {
	public GLView view;
	public boolean needsDimensions = true;

	public float minSize = 0.02f;
	public float maxSize = 0.1f;
	public float speed = 0.5f;
	public int numSpheres = 250;
	public int initialNumSpheres = 250;
	private Sphere[] spheres;
	private Sphere[] sTemp;
	protected Sphere[] sphereLoc;
	public int sSize = 64;
	private int tos = 0;
	private double lastUpdate;
	private int numCores = 4;
	private Thread[] threads;
	private double lastFPSUpdate;
	private int frames;
	public int score = 0;
	public TextView display_score;
	private MediaPlayer mp;
	private MediaPlayer[] m;
	private boolean isCustom = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		SharedPreferences prefs = getSharedPreferences("statePrefs", 0);
		initialNumSpheres = prefs.getInt("initialNumSpheres", initialNumSpheres);
		numSpheres = prefs.getInt("numSpheres", initialNumSpheres);
		score = prefs.getInt("score", 0);
		minSize = prefs.getFloat("minSize", minSize);
		maxSize = prefs.getFloat("maxSize", maxSize);
		speed = prefs.getFloat("speed", speed);
		isCustom = prefs.getBoolean("isCustom", false);

		view = (GLView) findViewById(R.id.glview);
		numCores = Runtime.getRuntime().availableProcessors();
		lastUpdate = System.currentTimeMillis()/1000.0;		
		sTemp = new Sphere[sSize];
		display_score = (TextView) findViewById(R.id.score_label);
	}

	public void update () {
		display_score.setText(Integer.toString(score));
	}

	@Override
	protected void onPause () {
		for (Thread t : threads) {
			t.interrupt();
		}
		//mp.release();
		for (MediaPlayer medp : m) {
			if (medp!=null) {
				medp.release();
			}
		}
		super.onPause();
		view.onPause();
	}

	@Override
	protected void onResume () {
		m = new MediaPlayer[11];
		m[0] = MediaPlayer.create(this,R.raw.hi_a);
		m[1] = MediaPlayer.create(this,R.raw.hi_c);
		m[2] = MediaPlayer.create(this,R.raw.hi_d);
		m[3] = MediaPlayer.create(this,R.raw.hi_f);
		m[4] = MediaPlayer.create(this,R.raw.hi_g);
		m[5] = MediaPlayer.create(this,R.raw.hihi_d);
		m[6] = MediaPlayer.create(this,R.raw.low_a);
		m[7] = MediaPlayer.create(this,R.raw.low_c);
		m[8] = MediaPlayer.create(this,R.raw.low_d);
		m[9] = MediaPlayer.create(this,R.raw.low_f);
		m[10] = MediaPlayer.create(this,R.raw.low_g);
		for (MediaPlayer mac : m) {
			mac.setVolume(0.15f, 0.15f);
		}
		if (!needsDimensions) makeThreads();
		super.onResume();
		lastUpdate = System.currentTimeMillis()/1000.0;
		view.onResume();
		/*mp = MediaPlayer.create(this, R.raw.loop);
		mp.setLooping(true);
		mp.start();*/
	}

	public void drawFrame (Drawer draw) {
		//double deltaTime = System.currentTimeMillis()/1000.0 - lastUpdate;
		lastUpdate = System.currentTimeMillis()/1000.0;
		frames++;
		if (lastUpdate-lastFPSUpdate>=1.5) {
			System.out.println(1/((lastUpdate-lastFPSUpdate)/(double)frames));
			lastFPSUpdate = lastUpdate;
			frames = 0;
		}
		if (needsDimensions||threads==null) return;
		runOnUiThread(new Runnable() {
			public void run() { display_score.setText("Score: "+score); }
		});
		int spheresDestroyed = 0;
		for (int i=0;i<spheres.length;i++) {
			if (spheres[i]==null) spheresDestroyed++;
		}
		if (spheresDestroyed==numSpheres) {
			runOnUiThread(new Runnable() {
				public void run() { reset(); }
			});
		}
		for (Thread t : threads) {
			if (t.isAlive()) {
				t.interrupt(); // interrupts waiting and cycles run() again
			}
			else {
				t.start();
			}
		}
		Sphere tempSphere;
		for (int i=0;i<numSpheres;i++) {
			tempSphere = spheres[i];
			if (tempSphere!=null) {
				draw.halfSphere(tempSphere.x,tempSphere.y,tempSphere.scale,tempSphere.mat);
			}
		}
		for (int k=0;k<sTemp.length;k++) {
			if (sTemp[k]!=null) {
				draw.halfSphere(sTemp[k].x,sTemp[k].y,sTemp[k].scale,sTemp[k].mat); // BUG
			}
		}
	}

	public void updateDimensions (float w, float h) {
		if (!needsDimensions) {
			for (int s=0;s<numSpheres;s++) {
				if (spheres[s]!=null) {
					spheres[s].width = w;
					spheres[s].height = h;
				}
			}
		}
		else {
			spheres = new Sphere[numSpheres];
			sphereLoc = new Sphere[numSpheres];
			for (int s=0;s<numSpheres;s++) {
				spheres[s] = new Sphere(w,h,speed,minSize,maxSize);
				spheres[s].randomize();
				sphereLoc[s] = spheres[s];
			}
			makeThreads();
			needsDimensions = false;
		}
	}

	public void makeThreads () {
		threads = new Thread[numCores];
		int minIndex = 0;
		int indexStep = numSpheres/numCores;
		int leftover = numSpheres%numCores;
		for (int i=0;i<numCores;i++) {
			int max = minIndex+indexStep;
			if (i==numCores-1) max += leftover;
			threads[i] = new Thread(new CollisionWorker(sphereLoc,spheres,sTemp,minIndex,max,numCores,this));
			minIndex+=indexStep;
			threads[i].start();
		}
	}

	public void onTouch (MotionEvent e) {
		int action = e.getActionMasked()&MotionEvent.ACTION_MASK;
		if (action==MotionEvent.ACTION_DOWN||action==MotionEvent.ACTION_POINTER_DOWN) {
			//||action==MotionEvent.ACTION_MOVE) {
			float tx = (e.getX(e.getActionIndex()) - view.getWidth()/2)/(view.getWidth()/2)*view.width;
			float ty = (e.getY(e.getActionIndex()) - view.getHeight()/2)/(view.getHeight()/2)*view.height;
			Sphere s = new Sphere(view.width,view.height,speed,minSize,maxSize);
			s.x = tx;
			s.y = -ty;
			s.pop();
			tos++;
			if (tos>=sTemp.length-1) {
				tos = 0;
			}
			sTemp[tos] = s;
			addToScore(-5);
		}
	}

	public synchronized void addToScore (int s) {
		score += s;
		return;
	}

	public synchronized void reviseSpheres (Sphere[] local, int min, int max) {
		for (int i=min;i<max;i++) {
			spheres[i] = local[i];
		}
		if (min>0) {
			for (int i=0;i<min;i++) {
				local[i] = spheres[i];
			}
		}
		if (max<spheres.length-1){ 
			for (int i=max;i<spheres.length;i++) {
				local[i] = spheres[i];
			}
		}
		return;
	}

	public void randomChime () {
		Random r = new Random();
		int thisInt = r.nextInt(m.length);
		try {
			m[thisInt].seekTo(0);
			m[thisInt].start();
		}
		catch (IllegalStateException e) { }
	}

	public void reset () {
		needsDimensions = true;
		SharedPreferences prefs = getSharedPreferences("statePrefs", 0);
		SharedPreferences.Editor edit = prefs.edit();
		if (numSpheres<=(initialNumSpheres/5)+initialNumSpheres%5||isCustom) {
			edit.clear();
			edit.putInt("score",score).commit();
			Intent score_intent = new Intent(this, ScoreScreen.class);
			startActivity(score_intent);
			finish();
		}
		else {
			edit.putInt("numSpheres", numSpheres-(initialNumSpheres/5));
			edit.putInt("score", score);
			edit.commit();
			numSpheres-=10;
			recreate();
		}
	}

}

class CollisionWorker implements Runnable {
	Sphere[] s;
	Sphere[] ts;
	Sphere[] ls; // local copy. allowed to modify
	int minIndex;
	int maxIndex;
	//Drawer draw;
	double deltaTime;
	double lastUpdate;
	int cores;
	MainActivity m;

	public CollisionWorker (Sphere[] loc, Sphere[] sa, Sphere[] tsa, int min, int max, int c, MainActivity m) {
		this.ls = loc;
		this.s = sa;
		this.ts = tsa;
		this.minIndex = min;
		this.maxIndex = max;
		//this.draw = draw;
		this.lastUpdate = System.currentTimeMillis()/1000.0;
		this.cores = c;
		this.m = m;
	}

	public void run () {
		while (true) {
			int scoreThisFrame = 0;
			deltaTime = System.currentTimeMillis()/1000.0 - lastUpdate;
			lastUpdate = System.currentTimeMillis()/1000.0;
			for (int i=minIndex;i<maxIndex;i++) {
				if (ls[i]!=null&&s[i]!=null) {
					ls[i].manageBouncing();
					ls[i].manageMovement(deltaTime);
					if (ls[i].scale==0.0f) {
						ls[i] = null;
					}
					else {
						if (ls[i].layer==2) {
							for (int j=0;j<s.length;j++) {
								if (ls[j]!=null&&ls[i]!=null) {
									if (ls[i].checkCollisionAgainst(ls[j])) {
										ls[j].pop();
										m.randomChime();
										scoreThisFrame++;
									}
								}
							}
						}
						for (int j=0;j<ts.length;j++) {
							if (ts[j]!=null&&ls[i]!=null) {
								if (ts[j].checkCollisionAgainst(ls[i])) {
									ls[i].pop();
									m.randomChime();
									scoreThisFrame++;
								}
							}
						}
						//draw.halfSphere(s[i].x,s[i].y,s[i].scale,s[i].red,s[i].green,s[i].blue);
					}
				}
			}
			int start, end;
			start = (minIndex/s.length)*ts.length;
			end = (maxIndex/s.length)*ts.length;
			for (int k=start;k<end;k++) {
				if (ts[k]!=null) {
					ts[k].manageSize();
					if (ts[k].scale==0.0f) {
						ts[k] = null;
					}
					else {
						//draw.halfSphere(ts[k].x,ts[k].y,ts[k].scale,ts[k].red,ts[k].green,ts[k].blue);
					}
				}
			}
			m.addToScore(scoreThisFrame);
			m.reviseSpheres(ls, minIndex, maxIndex);
			while (true) {
				try { Thread.sleep(1000); }
				catch (InterruptedException e) { break; }
			}
		}
	}
}