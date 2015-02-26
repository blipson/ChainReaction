package com.mca.chainreaction;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Drawer {
	private FloatBuffer sVertBuffer;
	private FloatBuffer normalBuffer;
	private int vertLength;
	public GL10 gl;
	
	public Drawer (int slices, int rings) {
		this.stitchHalfSphereStripped(slices, rings);
	}
	
	public void setGL (GL10 gl) {
		this.gl = gl;
	}
	
	public void stitchHalfSphereStripped (int slices, int rings) {
		// uses triangle strips
		// number of vertices: slices*rings*2
		float pi = 3.14159265358979f;
		float deltaTheta = (pi/2)/rings; // azimuth
		float deltaPhi = (2*pi)/slices; // radial
		float theta = pi/2;
		float phi = 0.0f;
		float[] vertices;
		vertices = new float[(slices+1)*rings*6]; // contains vertices
		float[] normals;
		normals = new float[(slices+1)*rings*6];
		int i=0;
		for (int ring=0;ring<rings;ring++) {
			phi = 0.0f;
			theta -= deltaTheta;
			for (int slice=0;slice<slices;slice++) {
				// bottom-left corner point
				// note: i++ returns the pre-incremented value
				vertices[i++] = (float) (Math.cos(phi)*Math.sin(theta)); // x
				vertices[i++] = (float) (Math.sin(phi)*Math.sin(theta)); // y
				vertices[i++] = (float) (Math.cos(theta)); // z
				normals[i-3] = vertices[i-3];
				normals[i-2] = vertices[i-2];
				normals[i-1] = vertices[i-1];
				
				// top-left corner point
				vertices[i++] = (float) (Math.cos(phi)*Math.sin(theta+deltaTheta)); // x
				vertices[i++] = (float) (Math.sin(phi)*Math.sin(theta+deltaTheta)); // y
				vertices[i++] = (float) (Math.cos(theta+deltaTheta)); // z
				normals[i-3] = vertices[i-3];
				normals[i-2] = vertices[i-2];
				normals[i-1] = vertices[i-1];
				
				phi += deltaPhi;
			}
		}
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		sVertBuffer = vbb.asFloatBuffer();
		sVertBuffer.put(vertices);
		sVertBuffer.position(0);
		
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		
		vertLength = vertices.length;
	}
	
	public void stitchHalfSphere (int slices, int rings) {
		// slices: vertical "wedges"
		// rings: horizontal
		// length of vertex array is slices*rings*18
		float pi = 3.14159265358979f;
		float deltaTheta = (pi/2)/rings; // azimuth
		float deltaPhi = (2*pi)/slices; // radial
		float theta = pi/2;
		float phi = 0.0f;
		float[] vertices;
		vertices = new float[slices*rings*18]; // contains vertices
		float[] normals;
		normals = new float[slices*rings*18];
		int i=0;
		for (int ring=0;ring<rings;ring++) {
			phi = 0.0f;
			theta -= deltaTheta;
			for (int slice=0;slice<slices+1;slice++) {
				// bottom-left corner point
				// note: i++ returns the pre-incremented value
				vertices[i++] = (float) (Math.cos(phi)*Math.sin(theta)); // x
				vertices[i++] = (float) (Math.sin(phi)*Math.sin(theta)); // y
				vertices[i++] = (float) (Math.cos(theta)); // z
				normals[i-3] = vertices[i-3];
				normals[i-2] = vertices[i-2];
				normals[i-1] = vertices[i-1];
				
				// top-left corner point
				vertices[i++] = (float) (Math.cos(phi)*Math.sin(theta+deltaTheta)); // x
				vertices[i++] = (float) (Math.sin(phi)*Math.sin(theta+deltaTheta)); // y
				vertices[i++] = (float) (Math.cos(theta+deltaTheta)); // z
				normals[i-3] = vertices[i-3];
				normals[i-2] = vertices[i-2];
				normals[i-1] = vertices[i-1];
				
				// top-right corner point
				vertices[i++] = (float) (Math.cos(phi+deltaPhi)*Math.sin(theta+deltaTheta)); // x
				vertices[i++] = (float) (Math.sin(phi+deltaPhi)*Math.sin(theta+deltaTheta)); // y
				vertices[i++] = (float) (Math.cos(theta+deltaTheta)); // z
				normals[i-3] = vertices[i-3];
				normals[i-2] = vertices[i-2];
				normals[i-1] = vertices[i-1];
				
				// top-right corner point
				vertices[i++] = (float) (Math.cos(phi+deltaPhi)*Math.sin(theta+deltaTheta)); // x
				vertices[i++] = (float) (Math.sin(phi+deltaPhi)*Math.sin(theta+deltaTheta)); // y
				vertices[i++] = (float) (Math.cos(theta+deltaTheta)); // z
				normals[i-3] = vertices[i-3];
				normals[i-2] = vertices[i-2];
				normals[i-1] = vertices[i-1];
				
				// bottom-right corner point
				vertices[i++] = (float) (Math.cos(phi+deltaPhi)*Math.sin(theta)); // x
				vertices[i++] = (float) (Math.sin(phi+deltaPhi)*Math.sin(theta)); // y
				vertices[i++] = (float) (Math.cos(theta)); // z
				normals[i-3] = vertices[i-3];
				normals[i-2] = vertices[i-2];
				normals[i-1] = vertices[i-1];
				
				// bottom-left corner point
				vertices[i++] = (float) (Math.cos(phi)*Math.sin(theta)); // x
				vertices[i++] = (float) (Math.sin(phi)*Math.sin(theta)); // y
				vertices[i++] = (float) (Math.cos(theta)); // z
				normals[i-3] = vertices[i-3];
				normals[i-2] = vertices[i-2];
				normals[i-1] = vertices[i-1];
				
				phi += deltaPhi;
			}
		}
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		sVertBuffer = vbb.asFloatBuffer();
		sVertBuffer.put(vertices);
		sVertBuffer.position(0);
		
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		
		vertLength = vertices.length;
	}
	
	public void halfSphere (float posx, float posy, float scale, float[] mat) {
		gl.glPushMatrix();
		
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,mat, 0);
		
		gl.glTranslatef(posx,posy,0);
		gl.glScalef(scale,scale,scale);
		
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,sVertBuffer);
		//gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,vertLength/3);
		
		gl.glPopMatrix();
	}

	static void loadTexture(GL10 gl, Context context, int resource) {
		Bitmap bmp = BitmapFactory.decodeResource(
				context.getResources(), resource);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,
				GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,
				GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		bmp.recycle();
	}
}
