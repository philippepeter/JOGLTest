package fr.pip.jogl.glshape;
import static javax.media.opengl.GL2GL3.GL_QUADS;

import java.awt.Color;

import javax.media.opengl.GL2;

public class GLTexturedCube implements GLShape {

	private float x;
	private float y;
	private float z;
	
	private float red;
	private float green;
	private float blue;
	private float demiWidth;
	
	public GLTexturedCube(float x, float y, float z, float demiWidth, Color color) {
		this.demiWidth = demiWidth;
		this.x = x;
		this.y = y;
		this.z = z;
		red = color.getRed() / 255f;
		green = color.getGreen() / 255f;
		blue = color.getBlue() / 255f;
		
	}

	@Override
	public void draw(GL2 gl) {
		gl.glTranslatef(x, y, z);
		// ----- Render the Color Cube -----
		gl.glBegin(GL_QUADS); // of the color cube
		
		gl.glColor3f(red, green, blue); 
		gl.glVertex3f(-demiWidth, -demiWidth, -demiWidth);
		gl.glVertex3f(+demiWidth, -demiWidth, -demiWidth);
		gl.glVertex3f(+demiWidth, +demiWidth, -demiWidth);
		gl.glVertex3f(-demiWidth, +demiWidth, -demiWidth);
		
		gl.glVertex3f(-demiWidth, -demiWidth, -demiWidth);
		gl.glVertex3f(-demiWidth, -demiWidth, +demiWidth);
		gl.glVertex3f(-demiWidth, +demiWidth, +demiWidth);
		gl.glVertex3f(-demiWidth, +demiWidth, -demiWidth);
		
		gl.glVertex3f(-demiWidth, -demiWidth, +demiWidth);
		gl.glVertex3f(-demiWidth, +demiWidth, +demiWidth);
		gl.glVertex3f(+demiWidth, +demiWidth, +demiWidth);
		gl.glVertex3f(+demiWidth, -demiWidth, +demiWidth);
		
		gl.glVertex3f(+demiWidth, -demiWidth, -demiWidth);
		gl.glVertex3f(+demiWidth, +demiWidth, -demiWidth);
		gl.glVertex3f(+demiWidth, +demiWidth, +demiWidth);
		gl.glVertex3f(+demiWidth, -demiWidth, +demiWidth);
		
		gl.glVertex3f(+demiWidth, -demiWidth, -demiWidth);
		gl.glVertex3f(-demiWidth, -demiWidth, -demiWidth);
		gl.glVertex3f(-demiWidth, -demiWidth, +demiWidth);
		gl.glVertex3f(+demiWidth, -demiWidth, +demiWidth);
		
		gl.glVertex3f(-demiWidth, +demiWidth, +demiWidth);
		gl.glVertex3f(+demiWidth, +demiWidth, +demiWidth);
		gl.glVertex3f(+demiWidth, +demiWidth, -demiWidth);
		gl.glVertex3f(-demiWidth, +demiWidth, -demiWidth);
		
		gl.glEnd(); // of the color cube
		gl.glTranslatef(-x, -y, -z);
	}

}
