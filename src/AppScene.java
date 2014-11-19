/*
Author: Steve Maddock
Last updated: 24 October 2013
*/

import java.io.File;
import java.awt.image.*;
import javax.imageio.*;
import com.jogamp.opengl.util.awt.*;

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;

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
  
  private float robotx=0;
  private float robotz=0;

  public AppScene(GL2 gl, Camera camera) {
    light0 = new Light(GL2.GL_LIGHT0);  // Create a default light
    
    //create left eye spotlight
    float[]position={0,0,0,1};
    leftEyeSpotlight=new Light(GL2.GL_LIGHT1,position);
    float[] direction = {0,-1f,-0.2f}; // direction from position to origin 
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
    robotx+=0.1f;
    robotz+=0.1f;
    if(robotx>5)
    {
    	robotx=0;
    	robotz=0;
    }
    
   
    
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
	   transformForLeftEye(gl);
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
    
    drawRobot(gl);
    displayFloor(gl);

  }
  
  private void transformForRobot(GL2 gl)
  {	 
	  gl.glTranslatef(robotx, 0, robotz);
	  gl.glTranslated(-2, 4, -2);
//	  gl.glRotatef(45, 0, 1, 0);
//	  gl.glRotatef(45, 1, 0, 0);
//      gl.glRotatef(-45, 0, 1, 0);
  }
  
  private void transformForLeftEye(GL2 gl)
  {
	  float headradius=1;
	  float eyeheight=30;
	  float eyeInterval=60;
	  gl.glRotatef((float)(-45-0.5*eyeInterval), 0, 1, 0);//left eye 
	  gl.glRotatef(eyeheight, 0, 0, 1);//eye height
	  gl.glTranslatef(headradius, 0, 0);
  }
  private void transformForRightEye(GL2 gl)
  {
	  float headradius=1;
	  float eyeheight=30;
	  float eyeInterval=60;
	  gl.glRotatef((float)(-45+0.5*eyeInterval), 0, 1, 0);//right eye 
	  gl.glRotatef(eyeheight, 0, 0, 1);//eye height
	  gl.glTranslatef(headradius, 0, 0);
  }
  
  
  private void drawRobot(GL2 gl)
  {
	  gl.glPushMatrix();	  
	  transformForRobot(gl);
	  //draw head
	  setHeadMaterialProperty(gl);
	  int sphereslices = 100;
	  int spherestacks = 100;
	  float headradius=1;
	  gl.glPushMatrix();
	  glut.glutSolidSphere(headradius, sphereslices, spherestacks);
	  gl.glPopMatrix();
	  
	 
	 //draw eye
	  setEyeMaterialProperty(gl);
	  sphereslices = 50;
	  spherestacks = 50;
	  gl.glPushMatrix();
	
	  double eyeradius=0.18;
	  transformForLeftEye(gl);
	  glut.glutSolidSphere(eyeradius, sphereslices, spherestacks);
	  
	  gl.glPopMatrix();
	  
	  gl.glPushMatrix();
	  transformForRightEye(gl);
	  glut.glutSolidSphere(eyeradius, sphereslices, spherestacks);
	  gl.glPopMatrix();
	  

	  //draw shoulders and upper arms
	  setUpperArmMaterialProperty(gl); 
	  sphereslices = 100;
	  spherestacks = 100;
	  double shoulderRadius=0.5;
	  float upArmRadius=0.18f;
	  float upArmHeight=1.5f;
	  
	  gl.glPushMatrix();
	  gl.glRotatef(-60, 0, 1, 0);//right
	  gl.glPushMatrix();
	  gl.glTranslatef(0, 0,headradius);
	  glut.glutSolidSphere(shoulderRadius, sphereslices, spherestacks);//shoulders
	  glut.glutSolidCylinder(upArmRadius, upArmHeight, sphereslices, spherestacks);//upper arms
	  gl.glPopMatrix();
	  gl.glPopMatrix();
	  
	  gl.glPushMatrix();
	  gl.glRotatef(150, 0, 1, 0);//left
	  gl.glTranslatef(0, 0, headradius);
	  glut.glutSolidSphere(shoulderRadius, sphereslices, spherestacks);//shoulders
	  glut.glutSolidCylinder(upArmRadius, upArmHeight, sphereslices, spherestacks);//upper arms
	  gl.glPopMatrix();
	  
	  //draw elbows
	  setLowerArmMaterialProperty(gl);
	  float elbowRadius=0.3f;
	  float handElbowRadius=0.25f;
	  float lowerArmRadius=0.18f;
	  float lowerArmHeight=1f;
	  float fingerHeight=0.6f;
	  gl.glPushMatrix();
	  	gl.glRotatef(-60, 0, 1, 0);//right elbow
	  	gl.glTranslatef(0, 0, headradius+upArmHeight);
	  	glut.glutSolidSphere(elbowRadius, sphereslices, spherestacks);
	  	gl.glPushMatrix();
	  		gl.glRotatef(90, 0, 1, 0);//right lower arms
	  		glut.glutSolidCylinder(lowerArmRadius, lowerArmHeight, sphereslices, spherestacks);
	  		//hand elbows
	  		gl.glPushMatrix();
	  			gl.glTranslatef(0, 0, lowerArmHeight);
	  			setHandElbowMaterialProperty(gl);
	  			glut.glutSolidSphere(handElbowRadius, sphereslices, spherestacks);
	  				//finger
	  				gl.glPushMatrix();
	  				setHandMaterialProperty(gl);
	  					gl.glPushMatrix();
	  					gl.glRotatef(-60, 1, 0, 0);
	  					gl.glTranslatef(0, 0, fingerHeight/2);
	  					gl.glScalef(0.1f, 0.1f, fingerHeight);
	  					glut.glutSolidCube(1f);
	  					gl.glPopMatrix();
	  					
	  					gl.glPushMatrix();
	  					gl.glRotatef(120, 0, 0, 1);
	  					gl.glRotatef(-60, 1, 0, 0);
	  					gl.glTranslatef(0, 0, fingerHeight/2);
	  					gl.glScalef(0.1f, 0.1f, fingerHeight);
	  					glut.glutSolidCube(1f);
	  					gl.glPopMatrix();
	  					
	  					gl.glPushMatrix();
	  					gl.glRotatef(240, 0, 0, 1);
	  					gl.glRotatef(-60, 1, 0, 0);
	  					gl.glTranslatef(0, 0, fingerHeight/2);
	  					gl.glScalef(0.1f, 0.1f, fingerHeight);
	  					glut.glutSolidCube(1f);
	  					gl.glPopMatrix();
	  				gl.glPopMatrix();
	  			setLowerArmMaterialProperty(gl);
	  		gl.glPopMatrix();
	  	gl.glPopMatrix();
	  gl.glPopMatrix();
	  
	
	
	  gl.glPushMatrix();
	  	gl.glRotatef(150, 0, 1, 0);//left elbow
	  	gl.glTranslatef(0, 0, headradius+upArmHeight);
	  	glut.glutSolidSphere(elbowRadius, sphereslices, spherestacks);
	  	gl.glPushMatrix();
	  		gl.glRotatef(-90, 0, 1, 0);//right lower arms
	  		glut.glutSolidCylinder(lowerArmRadius, lowerArmHeight, sphereslices, spherestacks);
	  		//hand elbows
	  		gl.glPushMatrix();
	  			gl.glTranslatef(0, 0, lowerArmHeight);
	  			setHandElbowMaterialProperty(gl);
	  			glut.glutSolidSphere(handElbowRadius, sphereslices, spherestacks);
  				//finger
  				gl.glPushMatrix();
  				setHandMaterialProperty(gl);
  					gl.glPushMatrix();
  					gl.glRotatef(-60, 1, 0, 0);
  					gl.glTranslatef(0, 0, fingerHeight/2);
  					gl.glScalef(0.1f, 0.1f, fingerHeight);
  					glut.glutSolidCube(1f);
  					gl.glPopMatrix();
  					
  					gl.glPushMatrix();
  					gl.glRotatef(120, 0, 0, 1);
  					gl.glRotatef(-60, 1, 0, 0);
  					gl.glTranslatef(0, 0, fingerHeight/2);
  					gl.glScalef(0.1f, 0.1f, fingerHeight);
  					glut.glutSolidCube(1f);
  					gl.glPopMatrix();
  					
  					gl.glPushMatrix();
  					gl.glRotatef(240, 0, 0, 1);
  					gl.glRotatef(-60, 1, 0, 0);
  					gl.glTranslatef(0, 0, fingerHeight/2);
  					gl.glScalef(0.1f, 0.1f, fingerHeight);
  					glut.glutSolidCube(1f);
  					gl.glPopMatrix();
  				gl.glPopMatrix();
	  			setLowerArmMaterialProperty(gl);
	  		gl.glPopMatrix();
	  	gl.glPopMatrix();
	  gl.glPopMatrix();
	  
	  
	  
	  gl.glPopMatrix();
	
  }
  	
	private void setHandMaterialProperty(GL2 gl)
	{
	  float[] matAmbient = {0.27f, 0.188f, 0.215f, 1.0f};
	  float[] matDiffuse = {0.898f, 0.723f, 0.797f, 1.0f};
	  float[] matSpecular = {0.6f, 0.6f, 0.6f, 1.0f};
	  float[] matShininess = {100.0f};//0~128
	  float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
	  
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);
	}
	private void setHandElbowMaterialProperty(GL2 gl)
	{
	  float[] matAmbient = {0.0f, 0f, 0f, 1.0f};
	  float[] matDiffuse = {0.82f, 0.664f, 0.61f, 1.0f};
	  float[] matSpecular = {0.6f, 0.6f, 0.6f, 1.0f};
	  float[] matShininess = {100.0f};//0~128
	  float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
	  
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);
	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);
	}
  	private void setHeadMaterialProperty(GL2 gl)
  	{
  	  float[] matAmbient = {0.156f, 0.058f, 0.078f, 1.0f};
  	  float[] matDiffuse = {0.450f, 0.218f, 0.254f, 1.0f};
  	  float[] matSpecular = {0.6f, 0.6f, 0.6f, 1.0f};
  	  float[] matShininess = {100.0f};//0~128
  	  float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
  	  
  	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
  	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
  	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
  	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);
  	  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);
  	}
  	private void setEyeMaterialProperty(GL2 gl)
  	{
  		  float[] matAmbient = {0, 0, 0, 1.0f};
  		  float[] matDiffuse = {0, 0, 0, 1.0f};
  		  float[] matSpecular ={0.6f, 0.6f, 0.6f, 1.0f};
  		  float[] matShininess = {100.0f};//0~128
  		  float[] matEmission = {0.4f, 0.5f, 0.8f, 1.0f};
  		  
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);
  		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);
  	}
  
  private void setUpperArmMaterialProperty(GL2 gl)
  {
	  float[]matAmbient = {0.242f, 0.328f, 0.430f, 1.0f};
	  float[]matDiffuse = {0.310f, 0.410f, 0.531f, 1.0f};
	  float[]matSpecular = {0.6f, 0.6f, 0.6f, 1.0f};
	  float[]matShininess ={100.0f};//0~128
	  float[]matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
		  
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);
  }
  
  private void setLowerArmMaterialProperty(GL2 gl)
  {
	 float[]matAmbient = {0.289f, 0.453f, 0.266f, 1.0f};
	 float[] matDiffuse ={0.410f, 0.703f, 0.391f, 1.0f};
	 float[] matSpecular = {0.6f, 0.6f, 0.6f, 1.0f};
	 float[]  matShininess = {100.0f};//0~128
	 float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
	  
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);
		  gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);
  }
  
  private void displayFloor(GL2 gl) {
	  float[] matAmbient = {0.2f, 0.6f, 0.2f, 1.0f};
	  float[] matDiffuse = {0.2f, 0.6f, 0.2f, 1.0f};
	  float[] matSpecular = {0.6f, 0.6f, 0.6f, 1.0f};
	  float[] matShininess = {20.0f};
	  float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f}; gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0); gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0); gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0); gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0); gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0); gl.glPushMatrix(); // floor
	        gl.glScaled(5,0.025,5);
	        glut.glutSolidSphere(1.0f, 100,100);
	      gl.glPopMatrix();
	  }
  
  

}


