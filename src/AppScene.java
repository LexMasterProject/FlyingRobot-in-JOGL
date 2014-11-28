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

	private Light worldLight;
	private Light leftEyeSpotlight;
	private Light ceilingSpotlight1,ceilingSpotlight2;
	private Camera camera;
	private Axes axes;

	private Robot robot1;
	private Room room;
	private Animation animationScene;


	public AppScene(GL2 gl, Camera camera) {
		animationScene = new Animation();
		worldLight = new Light(GL2.GL_LIGHT0);  // Create a default light

		this.robot1=new Robot();
		this.room=new Room(gl);


		//create left eye spotlight
		float[]position={0,0,0,1};
		leftEyeSpotlight=new Light(GL2.GL_LIGHT1,position);
		float[] direction = {1f,-1f,-0.3f}; // direction from position to origin 
		leftEyeSpotlight.makeSpotlight(direction, 20f);
		
		//create ceiling spotlight
		float[]posCeiling1={0,0,0,1};
		ceilingSpotlight1=new Light(GL2.GL_LIGHT2,posCeiling1);
		float[] direction1 = {0,-1f,0}; // direction from position to origin 
		ceilingSpotlight1.makeSpotlight(direction1, 20f);
		
		float[]posCeiling2={0,0,0,1};
		ceilingSpotlight2=new Light(GL2.GL_LIGHT3,posCeiling1);
		float[] direction2 = {0,-1f,0}; // direction from position to origin 
		ceilingSpotlight2.makeSpotlight(direction1, 20f);

		this.camera = camera;
		axes = new Axes(8, 8, 8);
	}

	// called from SG1.reshape() if user resizes the window
	public void setCanvasSize(int w, int h) {
		canvaswidth=w;
		canvasheight=h;
	}


	public Light getLight() {
		return worldLight;
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
		double cx = animationScene.getParam(Animation.ROBOT_X_PARAM);
		double cy = animationScene.getParam(Animation.ROBOT_Y_PARAM);
		double cz = animationScene.getParam(Animation.ROBOT_Z_PARAM);
		double r = animationScene.getParam(Animation.ROBOT_RSELF_PARAM);

//		gl.glTranslated(cx, cy, cz);
//		gl.glTranslated(-Room.size/2+3, 3, -2);
		
		gl.glTranslated(0, 3, 0);
//		gl.glRotated(r, 0, 1, 0);
//		gl.glRotated(45, 1, 0, 0);
//		gl.glRotated(-45, 0, 1, 0);
	}

	private void doWorldLight(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(0, Room.wallHeight-3, 0);
		worldLight.use(gl, glut, true);
		gl.glPopMatrix();
	}

	private void doLeftEyeLight(GL2 gl) {
		gl.glPushMatrix();
		transformForRobot(gl);
		robot1.transformForLeftEye(gl);
		this.leftEyeSpotlight.use(gl, glut, true);
		gl.glPopMatrix();
	}
	
	private void doCeilingLight(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(-8, Room.wallHeight, 0);
        setCeilingLightProperty(gl);		
		ceilingSpotlight1.use(gl, glut, true); 
		glut.glutSolidSphere(0.8, 20,20);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslated(8, Room.wallHeight, 0);
        setCeilingLightProperty(gl);		
		ceilingSpotlight2.use(gl, glut, true); 
		glut.glutSolidSphere(0.8, 20,20);
		gl.glPopMatrix();
	}
	private void setCeilingLightProperty(GL2 gl)
  	{
  		  float[] matAmbient = {0.6f, 0.6f, 0.6f, 1.0f};
  		  float[] matDiffuse = {0.99f, 0.84f, 0, 1.0f};
  		  float[] matSpecular ={0.6f, 0.6f, 0.6f, 1.0f};
  		  float[] matShininess = {100.0f};//0~128
  		  float[] matEmission = {0.99f, 0.84f, 0.8f, 1.0f};
  		  
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);

  	}



	public void render(GL2 gl) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		camera.view(glu);      // Orientate the camera
		doWorldLight(gl);          // Place the default light
		doLeftEyeLight(gl);
		doCeilingLight(gl);

		if (axes.getSwitchedOn()) 
			axes.display(gl, glut);

		room.display();
		gl.glPushMatrix();
		
		transformForRobot(gl);
		robot1.display(gl, glut);
		gl.glPopMatrix();
	}







}


