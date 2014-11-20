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
  


  public AppScene(GL2 gl, Camera camera) {
    light0 = new Light(GL2.GL_LIGHT0);  // Create a default light
    
     this.robot1=new Robot();
    
    //create left eye spotlight
    float[]position={0,0,0,1};
    leftEyeSpotlight=new Light(GL2.GL_LIGHT1,position);
    float[] direction = {1f,-1f,-0.3f}; // direction from position to origin 
    leftEyeSpotlight.makeSpotlight(direction, 10f);
    
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
    rotate=0.0;
  }

  public void incRotate() {
    rotate=(rotate+INC_ROTATE)%360;
  }

  
  public void update() {
    incRotate();
    
  }
  public void transformForRobot(GL2 gl)
  {	 
  	 
	  gl.glTranslated(-2, 4, -2);
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
  
  private void doLight1(GL2 gl) {
	  gl.glPushMatrix();
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
    doLight1(gl);

    if (axes.getSwitchedOn()) 
      axes.display(gl, glut);
    
    gl.glPushMatrix();
    transformForRobot(gl);
    robot1.display(gl, glut);
    gl.glPopMatrix();
    displayFloor(gl);

  }
  

  private void displayFloor(GL2 gl) {
	  float[] matAmbient = {0.2f, 0.6f, 0.2f, 1.0f};
	  float[] matDiffuse = {0.2f, 0.6f, 0.2f, 1.0f};
	  float[] matSpecular = {0.6f, 0.6f, 0.6f, 1.0f};
	  float[] matShininess = {20.0f};
	  float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f}; gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0); gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0); gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0); gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0); gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0); gl.glPushMatrix(); // floor
	        gl.glScaled(10,0.025,10);
	        glut.glutSolidSphere(1.0f, 100,100);
	      gl.glPopMatrix();
	  }
  
  

}


