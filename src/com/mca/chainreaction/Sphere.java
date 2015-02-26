package com.mca.chainreaction;

import java.util.*;

public class Sphere {
	public float x; // x position
	public float y; // y position
	// z position is implicitly 0
	public float vx; // x velocity
	public float vy; // y velocity
	public float width;
	public float height;
	public float scale;
	public float minScale;
	public float maxScale;
	public double timePopped;
	public float speed;
	public byte layer; // 0x01 is static, 0x02 is live
	public float red;
	public float blue;
	public float green;
	public float[] mat;

	public Sphere (float w, float h, float speed, float minScale, float maxScale) {
		this.x = 0.0f;
		this.y = 0.0f;
		this.vx = 0.0f;
		this.vy = 0.0f;
		this.width = w;
		this.height = h;
		this.scale = minScale;
		this.minScale = minScale;
		this.maxScale = maxScale;
		this.speed = speed;
		this.layer = 1;
		this.red = this.blue = this.green = 1.0f;
		mat = new float[] {1.0f,1.0f,1.0f,1.0f};
	}
	
	public void pop () {
		this.vx = 0.0f;
		this.vy = 0.0f;
		this.layer = 2;
		this.timePopped = System.currentTimeMillis()/1000.0;
	}

	public void randomize () {
		Random r = new Random();
		this.x = ((r.nextFloat()-(scale/2))-0.5f)*2.0f; // results in float between (-1+scale) and (1-scale)
		this.x *= width;
		this.y = ((r.nextFloat()-(scale/2))-0.5f)*2.0f; // results in float between (-1+scale) and (1-scale)
		this.y *= height;
		// new random vector for velocity
		vx = (r.nextFloat()-0.5f) * 2.0f;
		vy = (r.nextFloat()-0.5f) * 2.0f;
		// normalize and scale by speed
		float len = (float) Math.sqrt(vx*vx + vy*vy);
		vx = (vx/len)*speed;
		vy = (vy/len)*speed;
		this.red = r.nextFloat();
		this.blue = r.nextFloat();
		this.green = r.nextFloat();
		mat[0] = red*scale;
		mat[1] = green*scale;
		mat[2] = blue*scale;
	}

	public boolean checkCollisionAgainst (Sphere s) {
		if (this.layer!=2||s==null) return false;
		if (s.layer==1) { // only check live against static
			float xx = this.x - s.x;
			float yy = this.y - s.y;
			if (Math.sqrt(xx*xx + yy*yy)<=(this.scale+s.scale)) {
				return true;
			}
		}
		return false;
	}

	public void manageBouncing () {
		if (x+scale>width||x-scale<-width) {
			if (x+scale>width) x = width-scale;
			if (x-scale<-width) x = -width+scale;
			this.vx *= -1;
		}
		if (y+scale>height||y-scale<-height) {
			if (y+scale>height) y = height-scale;
			if (y-scale<-height) y = -height+scale;
			this.vy *= -1;
		}
	}

	public void manageMovement (double deltaTime) {
		if (layer==2) {
			manageSize();
			return;
		}
		this.x += (vx*deltaTime);
		this.y += (vy*deltaTime);
	}

	public void manageSize () {
		double time = System.currentTimeMillis()/1000.0;
		float popTime = 0.5f;
		float hangTime = 1.8f;//0.8f;
		
		if (time-timePopped<=popTime) {
			scale = (float) (minScale + ((maxScale-minScale)*((time-timePopped)/popTime)));
		}
		else if (time-timePopped<=popTime+hangTime) {
			// do nothing
		}
		else if (time-timePopped<=(popTime*2+hangTime)) {
			scale = (float) (maxScale-(maxScale*((time-timePopped-popTime-hangTime)/popTime)));
		}
		else {
			scale = 0.0f;
		}
		mat[0] = red*scale;
		mat[1] = green*scale;
		mat[2] = blue*scale;
	}

}