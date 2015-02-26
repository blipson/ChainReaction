package com.mca.chainreaction;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GLView extends GLSurfaceView {
	private final GLRenderer renderer;
	private MainActivity parent;
	private Drawer draw;
	public float width;
	public float height;
	public boolean hasNewDimensions = false;
	
	public GLView (Context context, AttributeSet args) {
		super(context,args);
		super.setEGLConfigChooser(8,8,8,8,16,0);
		renderer = new GLRenderer(context,this);
		parent = (MainActivity) context;
		setRenderer(renderer);
		draw = new Drawer(24,12);
		requestRender();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// MotionEvent reports input details from the touch screen
		// and other input controls. In this case, you are only
		// interested in events where the touch position changed.
		parent.onTouch(e);
		return true;
	}
	
	public void drawFrame (GL10 gl) {
		//if (draw==null) draw = new Drawer(12,8);
		draw.setGL(gl);
		//rot += 0.5f;
		parent.drawFrame(draw);
	}
	
	public void updateDimensions () {
		parent.updateDimensions(width,height);
	}
}
