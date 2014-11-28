import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;


public class Robot {
	private final float DEFAULT_HEAD_RADIUS=1;
	private final float DEFAULT_EYE_INTERVAL=60;
	private final float DEFAULT_EYE_HEIGHT=30;
	private final double shoulderRadius=0.5;
	private float upArmRadius=0.18f;
	private float upArmHeight=1.5f;
	private float headRadius;
	private float eyeInterval;
	private float eyeHeight;
	private float lowerArmRadius=0.18f;
	private float lowerArmHeight=1f;
	
	private double rotateLeftShoulder=0;
	
	public Robot()
	{
		headRadius=DEFAULT_HEAD_RADIUS;
		eyeInterval=DEFAULT_EYE_INTERVAL;
		eyeHeight=DEFAULT_EYE_HEIGHT;
	}
	
	  public void transformForLeftEye(GL2 gl)
	  {
		  gl.glRotatef((float)(-45-0.5*this.eyeInterval), 0, 1, 0);//left eye 
		  gl.glRotatef(this.eyeHeight, 0, 0, 1);//eye height
		  gl.glTranslatef(this.headRadius, 0, 0);
	  }
	  private void transformForRightEye(GL2 gl)
	  {
		  gl.glRotatef((float)(-45+0.5*this.eyeInterval), 0, 1, 0);//right eye 
		  gl.glRotatef(this.eyeHeight, 0, 0, 1);//eye height
		  gl.glTranslatef(this.headRadius, 0, 0);
	  }
	  
	  public void display(GL2 gl,GLUT glut)
	  {
		  setLowerArmMaterialProperty(gl);
		  gl.glPushMatrix();
		  gl.glTranslated(0, 3, 0);
		  gl.glRotatef(90, 0, 1, 0);//right lower arms
	  	  glut.glutSolidCylinder(lowerArmRadius, lowerArmHeight, 50, 50);
	  	  gl.glPopMatrix();
		  
	  	  setUpperArmMaterialProperty(gl);
		  gl.glPushMatrix();
		  gl.glRotatef(-60, 0, 1, 0);//right upper arms
		  gl.glTranslatef(0, 3,this.headRadius);
		  glut.glutSolidCylinder(upArmRadius, upArmHeight, 50, 50);
		  gl.glPopMatrix();
		  
		  
		  gl.glPushMatrix();	  
		  //draw head
		  setHeadMaterialProperty(gl);
		  int sphereslices = 100;
		  int spherestacks = 100;
		 
		  gl.glPushMatrix();
		  glut.glutSolidSphere(this.headRadius, sphereslices, spherestacks);
		  gl.glPopMatrix();
		  
		 
		 //draw eye
		  setEyeMaterialProperty(gl);
		  sphereslices = 20;
		  spherestacks = 20;
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
		  sphereslices = 20;
		  spherestacks = 20;
		  double shoulderRadius=0.5;
		  float upArmRadius=0.18f;
		  float upArmHeight=1.5f;
		  
		  gl.glPushMatrix();
		  gl.glRotatef(-60, 0, 1, 0);//right
		  gl.glPushMatrix();
		  gl.glTranslatef(0, 0,this.headRadius);
		  glut.glutSolidSphere(shoulderRadius, sphereslices, spherestacks);//shoulders
		  glut.glutSolidCylinder(upArmRadius, upArmHeight, sphereslices, spherestacks);//upper arms
		  gl.glPopMatrix();
		  gl.glPopMatrix();
		  
		  gl.glPushMatrix();
		  gl.glRotatef(150, 0, 1, 0);//left
		  gl.glTranslatef(0, 0, this.headRadius);
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
		  	gl.glTranslatef(0, 0, this.headRadius+upArmHeight);
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
		  	gl.glTranslatef(0, 0, this.headRadius+upArmHeight);
		  	glut.glutSolidSphere(elbowRadius, sphereslices, spherestacks);
		  	gl.glPushMatrix();
		  		gl.glRotatef(-90, 0, 1, 0);//left lower arms
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
	  
	
	
	public float getHeadRadius() {
		return headRadius;
	}

	public void setHeadRadius(float headRadius) {
		this.headRadius = headRadius;
	}




	
	
}
