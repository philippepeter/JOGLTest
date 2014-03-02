package fr.pip.jogl.glshape;
import java.awt.Color;


public class GLCubeFactory {

	public final static GLShape createCube(float x, float y, float z, float demiWidth, Color color) {
		return new GLCube(x, y, z, demiWidth, color);
	}
	
}
