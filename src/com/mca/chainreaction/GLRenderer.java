package com.mca.chainreaction;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class GLRenderer implements GLSurfaceView.Renderer {
	private static final String TAG = "GLRenderer";
	private final Context context;
	private GLView view;
	GLRenderer (Context context, GLView view) {
		this.context = context;
		this.view = view;
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_DITHER);
		float[] lightPos = new float[] {0,0,1,0};
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
		//float matDiffuse[] = new float[] {0.01f,0.01f,0.01f,1};
		//gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
		      //matAmbient, 0);
		//gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
		      //matDiffuse, 0);
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio = (float) width / height;
		if (width>height) {
			GLU.gluOrtho2D(gl, -1.0f, 1.0f, -(1/ratio), 1/ratio);
			view.width = 1.0f;
			view.height = 1/ratio;
		}
		else {
			GLU.gluOrtho2D(gl, -(ratio), ratio, -1.0f, 1.0f);
			view.width = ratio;
			view.height = 1.0f;
		}
		view.updateDimensions();
	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		view.drawFrame(gl);
	}
}
