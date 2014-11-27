/*
Author: Steve Maddock
Last updated: 24 October 2013
 */



import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

public class AppScene {

	private GLU glu = new GLU();
	private GLUT glut = new GLUT();

	private final double INC_ROTATE=2.0;

	private double rotate=0.0;


	private int canvaswidth=0, canvasheight=0;

	private Light light0;
	private Light leftEyeSpotlight;
	private Camera camera;
	private Axes axes;

	private Robot robot1;
	private Room room;
	private AnimationScene animationScene;


	public AppScene(GL2 gl, Camera camera) {
		animationScene = new AnimationScene();
		light0 = new Light(GL2.GL_LIGHT0);  // Create a default light

		this.robot1=new Robot();
		this.room=new Room(gl);


		//create left eye spotlight
		float[]position={0,0,0,1};
		leftEyeSpotlight=new Light(GL2.GL_LIGHT1,position);
		float[] direction = {1f,-1f,-0.3f}; // direction from position to origin 
		leftEyeSpotlight.makeSpotlight(direction, 20f);

		this.camera = camera;
		axes = new Axes(8, 8, 8);
	}

	// called from SG1.reshape() if user resizes the window
	public void setCanvasSize(int w, int h) {
		canvaswidth=w;
		canvasheight=h;
	}


	public Light getLight() {
		return light0;
	}

	public Axes getAxes() {
		return axes;
	}

	public void reset() {
		animationScene.reset();
		rotate=0.0;
	}

	public void incRotate() {
		rotate=(rotate+INC_ROTATE)%360;
	}

	public void startAnimation() {
		animationScene.startAnimation();
	}

	public void pauseAnimation() {
		animationScene.pauseAnimation();
	}

	public void update() {
		incRotate();
		animationScene.update();

	}
	public void transformForRobot(GL2 gl)
	{	 

		gl.glTranslated(-2, 3, -2);
		gl.glRotatef(45, 0, 1, 0);
		gl.glRotatef(45, 1, 0, 0);
		gl.glRotatef(-45, 0, 1, 0);
	}

	private void doLight0(GL2 gl) {
		gl.glPushMatrix();
		gl.glRotated(rotate,0,1,0);
		light0.use(gl, glut, true);
		gl.glPopMatrix();
	}

	private void doLeftEyeLight(GL2 gl) {
		gl.glPushMatrix();
		animateRobot(gl);
		transformForRobot(gl);
		robot1.transformForLeftEye(gl);
		this.leftEyeSpotlight.use(gl, glut, true);
		gl.glPopMatrix();
	}



	public void render(GL2 gl) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		camera.view(glu);      // Orientate the camera
		doLight0(gl);          // Place the default light
		doLeftEyeLight(gl);

		if (axes.getSwitchedOn()) 
			axes.display(gl, glut);


		room.display();
		gl.glPushMatrix();
		animateRobot(gl);
		transformForRobot(gl);
		robot1.display(gl, glut);
		gl.glPopMatrix();
	}

	private void animateRobot(GL2 gl)
	{
		double cx = animationScene.getParam(AnimationScene.ROBOT_X_PARAM);
		double cy = animationScene.getParam(AnimationScene.ROBOT_Y_PARAM);
		double cz = animationScene.getParam(AnimationScene.ROBOT_Z_PARAM);
		gl.glTranslated(cx,cy,cz);
	}






}


