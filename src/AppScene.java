/*
 *  I declare that this code is my own work 
 *  Author: Wei Wuhao <wwei13@sheffield.ac.uk> 
 */


import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

public class AppScene {

	private GLU glu = new GLU();
	private GLUT glut = new GLUT();
	
	private float cutoffForRobotSp=15;
	
	private int canvaswidth=0, canvasheight=0;

	private Light worldLight;
	private Light leftEyeSpotlightForR1,rightEyeSpotlightForR1;
	private Light leftEyeSpotlightForR2,rightEyeSpotlightForR2;
	private Light ceilingSpotlight1,ceilingSpotlight2;
	private Camera camera;
	private Axes axes;

	private Robot robot1,robot2;
	private Room room;
	private Animation animationScene;


	public AppScene(GL2 gl, Camera camera) {
		animationScene = new Animation();
		worldLight = new Light(GL2.GL_LIGHT0);  // Create a default light

		this.robot1=new Robot();
		this.robot2=new Robot();
		this.room=new Room(gl);


		//create  eye spotlight for robots
		float[]position={0,0,0,1};
		leftEyeSpotlightForR1=new Light(GL2.GL_LIGHT1,position);
		float[] direction = {1f,-1f,-0.3f}; // direction from position to origin 
		leftEyeSpotlightForR1.makeSpotlight(direction, cutoffForRobotSp);
		rightEyeSpotlightForR1=new Light(GL2.GL_LIGHT2,position);
		direction =new float[]{1f,-1f,0.3f};
		rightEyeSpotlightForR1.makeSpotlight(direction, cutoffForRobotSp);
		
		position=new float[]{0,0,0,1};
		leftEyeSpotlightForR2=new Light(GL2.GL_LIGHT3,position);
		direction = new float[]{1f,-1f,-0.3f}; // direction from position to origin 
		leftEyeSpotlightForR2.makeSpotlight(direction, cutoffForRobotSp);
		rightEyeSpotlightForR2=new Light(GL2.GL_LIGHT4,position);
		direction =new float[]{1f,-1f,0.3f};
		rightEyeSpotlightForR2.makeSpotlight(direction, cutoffForRobotSp);
		
		
		
		//create ceiling spotlight
		float[]posCeiling={0,0,0,1};
		float[] directionCeiling = {0,-1f,0}; // direction from position to origin 
		ceilingSpotlight1=new Light(GL2.GL_LIGHT5,posCeiling);
		
		ceilingSpotlight1.makeSpotlight(directionCeiling, 20f);
		ceilingSpotlight2=new Light(GL2.GL_LIGHT6,posCeiling);
		ceilingSpotlight2.makeSpotlight(directionCeiling, 20f);

		this.camera = camera;
		axes = new Axes(8, 8, 8);
	}

	public void setLightIndensity(int indensity)
	{
		worldLight.setIndensity(indensity);
	}
	
	// called from SG1.reshape() if user resizes the window
	public void setCanvasSize(int w, int h) {
		canvaswidth=w;
		canvasheight=h;
	}


	public Light getLight() {
		return worldLight;
	}
	
	public Light[] getCeilingLight()
	{
		Light[] ceilingLights= new Light[2];
		ceilingLights[0]=this.ceilingSpotlight1;
		ceilingLights[1]=this.ceilingSpotlight2;
		return ceilingLights;
	}

	public Axes getAxes() {
		return axes;
	}

	public void reset() {
		animationScene.reset();
		
	}

	

	public void startAnimation() {
		animationScene.startAnimation();
	}

	public void pauseAnimation() {
		animationScene.pauseAnimation();
	}

	public void update() {
	
		animationScene.update();
		
		//update the robot animate params in order to 
		//animate parts of robot
		double animateR90=animationScene.getParam(Animation.ROBOT_90_R);
		double animateR60=animationScene.getParam(Animation.ROBOT_60_R);
		robot1.update(animateR60,animateR90);
		robot2.update(animateR90, animateR60);

	}
	public void render(GL2 gl) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		camera.view(glu);      // Orientate the camera
		doWorldLight(gl);          // Place the default light
		//
		doCeilingLight(gl);

		if (axes.getSwitchedOn()) 
			axes.display(gl, glut);

		gl.glPushMatrix();
		room.display();
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		transformForRobot1(gl);
		robot1.display(gl, glut);
		doEyeLight(robot1,leftEyeSpotlightForR1,rightEyeSpotlightForR1,gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		transformForRobot2(gl);
		robot2.display(gl, glut);
		doEyeLight(robot2, leftEyeSpotlightForR2, rightEyeSpotlightForR2, gl);
		gl.glPopMatrix();
		
		
		
		
	}
	
	/*
	 *transform for robots 
	 *
	 */
	
	public void transformForRobot1(GL2 gl)
	{
		double cx = animationScene.getParam(Animation.ROBOT_X_PARAM);
		double cy = animationScene.getParam(Animation.ROBOT_Y_PARAM);
		double cz = animationScene.getParam(Animation.ROBOT_Z_PARAM);
		double r = animationScene.getParam(Animation.RSELF_360_PARAM);

		gl.glTranslated(cx, cy, cz);
		gl.glTranslated(-Room.size/2+3, 6, -2);
		
		gl.glRotated(r, 0, 1, 0);
		gl.glRotated(45, 1, 0, 0);
		gl.glRotated(-45, 0, 1, 0);
	}
	public void transformForRobot2(GL2 gl)
	{
		double cx = animationScene.getParam(Animation.ROBOT1_X_PARAM);
		double cy = animationScene.getParam(Animation.ROBOT1_Y_PARAM);
		double cz = animationScene.getParam(Animation.ROBOT1_Z_PARAM);
		double r = animationScene.getParam(Animation.RSELF_360_PARAM);

		gl.glTranslated(cx, cy, cz);
		gl.glTranslated(-Room.size/2+3, 8, -8);
		gl.glRotated(r, 0, 1, 0);
		gl.glRotated(45, 1, 0, 0);
		gl.glRotated(-45, 0, 1, 0);
	}
	
	/*
	 * put different light in proper positions
	 */
	private void doWorldLight(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(0, Room.wallHeight-3,  Room.wallHeight-3);
		worldLight.use(gl, glut, true);
		gl.glPopMatrix();
	}

	private void doEyeLight(Robot robot,Light leftLight,Light rightLight,GL2 gl) {
		
			gl.glPushMatrix();
			robot.transformForLeftEye(gl);
			leftLight.use(gl, glut, true);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			robot.transformForRightEye(gl);
			rightLight.use(gl, glut, true);
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


	//set ceiling light outlook
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



}


