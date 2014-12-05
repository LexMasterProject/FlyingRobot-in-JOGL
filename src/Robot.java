import javax.media.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;



/*
 *  I declare that this code is my own work 
 *  Author: Wei Wuhao <wwei13@sheffield.ac.uk> 
 */
public class Robot {

	//resolution for drawing different parts of robot
	//mainly use resolution to substitute slice and stacks 
	private int lowResolution=20;
	private int midResolution=50;
	private int highResolution=100;

	//different params to set initial robot status
	private float headRadius=1;
	private float eyeInterval=60;
	private float eyeHeight=30;
	private double shoulderRadius=0.5;
	private float upArmRadius=0.18f;
	private float upArmHeight=1.5f;
	private float lowerArmRadius=0.18f;
	private float lowerArmHeight=1f;
	private double eyeradius=0.18;
	private float elbowRadius=0.3f;
	private float handElbowRadius=0.25f;
	private float fingerHeight=0.6f;

	//animate params for rotating robot arms 
	private double aniRotate90=0;
	private double aniRotate60=0;

	public Robot()
	{
	}

    /*
     * Use scene graph to display robots.
     * The indentation show the hierarchy of the robot.
     * To make the hierarchy clear,specific transform for each part of robot
     * is embedded in draw functions.
     */
public void display(GL2 gl,GLUT glut)
{  
	boolean showRobot=true;	
	if(showRobot)
	{
		gl.glPushMatrix();
		drawHead(gl, glut);
			gl.glPushMatrix();
			drawRightEye(gl,glut);
			gl.glPopMatrix();

			gl.glPushMatrix();
			drawLeftEye(gl, glut);
			gl.glPopMatrix();
			//the right parts of body
			gl.glPushMatrix();
			drawRightShoulder(gl, glut);
		  		drawRightUpArm(gl, glut);
		  			drawElbow(gl, glut);
		  				drawRightLowArm(gl, glut);
		  					drawPalm(gl, glut);
		  						drawThreeFingers(gl, glut);
		  	gl.glPopMatrix();
		  	//the left parts of body
		  	gl.glPushMatrix();
		  	drawLeftShoulder(gl, glut);
		  		drawLeftUpArm(gl, glut);
		  			drawElbow(gl, glut);
		  				drawLeftLowArm(gl, glut);
		  					drawPalm(gl, glut);
		  						drawThreeFingers(gl, glut);
		  	gl.glPopMatrix();
		 gl.glPopMatrix();
	}
	else
	{
		/*
		 * testing code for animate parts of robot
		 */
		gl.glPushMatrix();
		drawOneFinger(gl, glut);
		gl.glPopMatrix();
	}

}
	
	//update robot animate params
	public void update(double animateR60,double animateR90)
	{
		this.aniRotate60=animateR60;
		this.aniRotate90=animateR90;
	}
	
	/*
	 * public transform for eyes for sync spolight
	 */
	public void transformForLeftEye(GL2 gl)
	{
		gl.glRotatef((float)(-45-0.5*this.eyeInterval), 0, 1, 0);//left eye 
		gl.glRotatef(this.eyeHeight, 0, 0, 1);//eye height
		gl.glTranslatef(this.headRadius, 0, 0);
	}
	public void transformForRightEye(GL2 gl)
	{
		gl.glRotatef((float)(-45+0.5*this.eyeInterval), 0, 1, 0);//right eye 
		gl.glRotatef(this.eyeHeight, 0, 0, 1);//eye height
		gl.glTranslatef(this.headRadius, 0, 0);
	}

	/*
	 * draw different parts of robot
	 */
	private void drawHead(GL2 gl,GLUT glut)
	{
		setHeadMaterialProperty(gl);
		glut.glutSolidSphere(this.headRadius, highResolution, highResolution);
	}
	private void drawLeftEye(GL2 gl,GLUT glut)
	{
		setEyeMaterialProperty(gl);
		transformForLeftEye(gl);
		glut.glutSolidSphere(eyeradius, lowResolution, lowResolution);
	}
	private void drawRightEye(GL2 gl,GLUT glut)
	{
		setEyeMaterialProperty(gl);
		transformForRightEye(gl);
		glut.glutSolidSphere(eyeradius, lowResolution, lowResolution);
	}

	private void drawRightShoulder(GL2 gl,GLUT glut)
	{
		setUpperArmMaterialProperty(gl); 
		gl.glRotatef(-60, 0, 1, 0);
		gl.glTranslatef(0, 0,this.headRadius);
		glut.glutSolidSphere(shoulderRadius, midResolution, midResolution);//shoulders
	}

	private void drawRightUpArm(GL2 gl,GLUT glut)
	{
		
		setUpperArmMaterialProperty(gl);

		gl.glRotated(this.aniRotate60, 0, 1, 0);
		glut.glutSolidCylinder(upArmRadius, upArmHeight, midResolution, midResolution);//upper arms
	}
	private void drawRightLowArm(GL2 gl,GLUT glut)
	{
		gl.glRotatef(90, 0, 1, 0);
		gl.glRotated(this.aniRotate90, 1, 0, 0);
		glut.glutSolidCylinder(lowerArmRadius, lowerArmHeight, midResolution, midResolution);
	}
	
	private void drawLeftUpArm(GL2 gl,GLUT glut)
	{
		setUpperArmMaterialProperty(gl); 
		gl.glRotated(this.aniRotate90, 0, 1, 0);
		glut.glutSolidCylinder(upArmRadius, upArmHeight, midResolution, midResolution);//upper arms
	}

	private void drawLeftLowArm(GL2 gl,GLUT glut)
	{
		gl.glRotatef(-90, 0, 1, 0);
		gl.glRotated(this.aniRotate60, 1, 0, 0);
		glut.glutSolidCylinder(lowerArmRadius, lowerArmHeight, midResolution, midResolution);
	}
	private void drawLeftShoulder(GL2 gl,GLUT glut)
	{
		setUpperArmMaterialProperty(gl);   
		gl.glRotatef(150, 0, 1, 0);//left
		gl.glTranslatef(0, 0, this.headRadius);
		glut.glutSolidSphere(shoulderRadius, midResolution, midResolution);//shoulders

	}
	private void drawElbow(GL2 gl,GLUT glut)
	{
		setLowerArmMaterialProperty(gl);
		gl.glTranslatef(0, 0, upArmHeight);
		glut.glutSolidSphere(elbowRadius, midResolution, midResolution);
	}
	

	private void drawPalm(GL2 gl,GLUT glut)
	{
		setHandMaterialProperty(gl);
		gl.glTranslatef(0, 0, lowerArmHeight);
		glut.glutSolidSphere(handElbowRadius, midResolution, midResolution);
	}
	private void drawOneFinger(GL2 gl,GLUT glut)
	{
		gl.glRotated(this.aniRotate60-40, 1, 0, 0);
		gl.glTranslatef(0, 0, fingerHeight/2);
		gl.glScalef(0.1f, 0.1f, fingerHeight);
		glut.glutSolidCube(1f);
	}
	
	private void drawThreeFingers(GL2 gl,GLUT glut)
	{   
		gl.glPushMatrix();
		drawOneFinger(gl, glut);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glRotatef(120, 0, 0, 1);
		drawOneFinger(gl, glut);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glRotatef(240, 0, 0, 1);
		drawOneFinger(gl, glut);
		gl.glPopMatrix();
	}
	
	/*
	 * set material properties for different parts of robot
	 */

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
	public static void setEyeMaterialProperty(GL2 gl)
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

}
