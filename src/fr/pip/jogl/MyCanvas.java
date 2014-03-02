package fr.pip.jogl;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST; // GL constants
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2.GL_COMPILE; // GL2 constants
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import fr.pip.jogl.glshape.GLCubeFactory;
import fr.pip.jogl.glshape.GLShape;

/**
 * JOGL 2.0 Example 2: Rotating 3D Shapes (GLCanvas)
 */
@SuppressWarnings("serial")
public class MyCanvas extends GLCanvas implements GLEventListener,
		MouseMotionListener, MouseListener, MouseWheelListener {

	private double draggX;
	private double draggY;
	private double mouseX;
	private double mouseY;
	private float rotateX;
	private float rotateY;
	private float zoom;

	private Random random;

	private GLU glu; // for the GL Utility
	private List<GLShape> shapes;

	private float[] lightAmbient = { 0.5f, 0.5f, 0.5f, 1.0f };
	private float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] lightPosition = { 0.0f, 0.0f, 2.0f, 1.0f };
	private int liste;
	private boolean useList = true;
	private float rotateZ;
	private int texture;
	private boolean isTextured = true;

	// Setup OpenGL Graphics Renderer

	/** Constructor to setup the GUI for this Component */
	public MyCanvas() {
		this.random = new Random();
		this.addGLEventListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		shapes = new ArrayList<GLShape>();

		int demiSize = 30;
		//int demiSize = 1;

		for (int i = 0; i < demiSize * 2; i++) {
			for (int j = 0; j < demiSize * 2; j++) {
				for (int k = 0; k < demiSize * 2; k++) {
					if (random.nextBoolean() && random.nextBoolean()
							&& random.nextBoolean() && random.nextBoolean())
						shapes.add(GLCubeFactory.createCube(i - demiSize, j
								- demiSize, k - demiSize, getRandomSize(),
								getRandomColor()));
				}
			}
		}

		zoom = -50f;

	}

	// ------ Implement methods declared in GLEventListener ------

	private float getRandomSize() {
		return Math.abs(random.nextFloat() - 0.6f);
	}

	private Color getRandomColor() {
		return new Color(random.nextInt(255), random.nextInt(255),
				random.nextInt(255));
	}

	/**
	 * Called back immediately after the OpenGL context is initialized. Can be
	 * used to perform one-time initialization. Run only once.
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		glu = new GLU(); // get GL Utilities
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best
																// perspective
																// correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out
									// lighting

		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		File file = new File("image.png");
		try {
			Texture t = TextureIO.newTexture(file, true);
			texture = t.getTextureObject(gl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (useList) {
			liste = gl.glGenLists(1);
			gl.glNewList(liste, GL_COMPILE);

			for (GLShape shape : shapes) {
				shape.draw(gl);
			}

			gl.glEndList();
		}
	}

	/**
	 * Call-back handler for window re-size event. Also called when the drawable
	 * is first set to visible.
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

		if (height == 0)
			height = 1; // prevent divide by zero
		float aspect = (float) width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear,
														// zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}

	/**
	 * Called back by the animator to perform rendering.
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color
																// and depth
																// buffers
		gl.glLoadIdentity(); // reset the current model-view matrix
		gl.glTranslatef(0, 0, zoom);
		gl.glRotatef(rotateX, 1, 0, 0);
		gl.glRotatef(rotateY, 0, 1, 0);
		gl.glRotatef(rotateZ, 0, 0, 1);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);

	    
		if (!useList) {
			for (GLShape shape : shapes) {
				shape.draw(gl);
			}
		} else {
			gl.glCallList(liste);
		}

		//rotateX += 0.2f;
		//rotateY += 0.2f;
		// rotateZ+=1;
	}

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such
	 * as buffers.
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		draggX = e.getX();
		draggY = e.getY();
		double angleX = mouseX - draggX;
		double angleY = mouseY - draggY;
		rotateY -= angleX;
		rotateX -= angleY;
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		zoom -= e.getWheelRotation() / 10f;
	}

}