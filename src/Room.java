import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

/*
 *  I declare that this code is my own work 
 *  Author: Wei Wuhao <wwei13@sheffield.ac.uk> 
 */
public class Room {
	private Mesh floorMesh,wallMesh,obCubeMesh;
	private Render floorCeilingRender,wallRender,obCubeRender;
	private GL2 gl;
	private Texture floorTex,wallTex,ceilingTex,obTex;
	
	public static final double size=40;
	public static final double wallHeight=20;
	private GLU glu = new GLU();
	
	public Room(GL2 gl)
	{
		this.gl=gl;
	
		floorMesh = ProceduralMeshFactory.createPlane(size,size,(int)(4*size),(int)(4*size),1,1);
		wallMesh= ProceduralMeshFactory.createPlane(wallHeight,size,80,80,1,1);
		obCubeMesh=ProceduralMeshFactory.createHardCube();
		
		
		
		//load texture
		floorTex=loadTexture(gl, "floor.jpg");
		floorTex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		floorTex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		wallTex=loadTexture(gl, "wall.jpg");
		wallTex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		wallTex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		ceilingTex=loadTexture(gl, "ceiling.jpg");
		obTex=loadTexture(gl, "marble.jpg");
		
		
		createRenderObjects();

	}
	
	
	private void createRenderObjects()
	{
		floorCeilingRender= new Render(floorMesh);
		floorCeilingRender.initialiseDisplayListWithTex(gl);
		
		wallRender=new Render(wallMesh);
		wallRender.initialiseDisplayListWithTex(gl);	
		
		obCubeRender=new Render(obCubeMesh);
		obCubeRender.initialiseDisplayListWithTex(gl);	
		
		
	}
	
	
	
	public void display()
	{
		drawFloor();
		drawWalls();	
		drawCeiling();
		drawObstacles();
	}
	
	private void drawCeiling()
	{
		ceilingTex.enable(gl);
		ceilingTex.bind(gl);
		ceilingTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		
		gl.glPushMatrix();
		gl.glTranslated(0, wallHeight, 0);
		gl.glRotated(180, 1, 0, 0);
		floorCeilingRender.renderDisplayList(gl);
		gl.glPopMatrix();
		ceilingTex.disable(gl);
	}
	private void drawFloor()
	{
		floorTex.enable(gl);
		floorTex.bind(gl);
		floorTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		
		gl.glMatrixMode(GL2.GL_TEXTURE); 
		gl.glLoadIdentity(); 
		gl.glScaled(8,8,8); 
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		floorCeilingRender.renderDisplayList(gl);
		floorTex.disable(gl);
		
		gl.glMatrixMode(GL2.GL_TEXTURE); 
		gl.glLoadIdentity(); 
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	private void drawWalls()
	{	
		wallTex.enable(gl);
		wallTex.bind(gl);
		wallTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
	
		//draw left wall
		gl.glPushMatrix();
		gl.glTranslatef(-(float)size/2, (float)wallHeight/2, 0);
		gl.glRotatef(-90, 0, 0, 1);
		wallRender.renderDisplayList(gl);
		gl.glPopMatrix();
	
		
		//draw right wall
		gl.glPushMatrix();
		gl.glTranslatef((float)size/2, (float)wallHeight/2, 0);
		gl.glRotatef(90, 0, 0, 1);
		wallRender.renderDisplayList(gl);
		gl.glPopMatrix();
		
		
		//draw forward wall
		gl.glPushMatrix();
		gl.glTranslatef(0, (float)wallHeight/2, -(float)size/2);
		gl.glRotatef(90, 0, 1, 0);
		gl.glRotatef(90, 0, 0, 1);
		wallRender.renderDisplayList(gl);
		gl.glPopMatrix();
		
		//draw back wall
		gl.glPushMatrix();
		gl.glTranslatef(0, (float)wallHeight/2, (float)size/2);
		gl.glRotatef(-90, 0, 1, 0);
		gl.glRotatef(90, 0, 0, 1);
		wallRender.renderDisplayList(gl);
		gl.glPopMatrix();
		
		
		
		wallTex.disable(gl);
	}
	
	private void drawObstacles()
	{
		obTex.enable(gl);
		obTex.bind(gl);
		obTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		
		
		 GLUquadric quadric = glu.gluNewQuadric();
		 glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		 glu.gluQuadricTexture(quadric, true);         // texture all objects drawn using this variable
		 glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
			  
				//left 
				gl.glPushMatrix();
				gl.glTranslated(-8, 5, -8);
				gl.glScaled(4, 4, 4);
				obCubeRender.renderDisplayList(gl);
				gl.glPopMatrix();
				//middle
				gl.glPushMatrix();
				gl.glTranslated(-6, 2.5, 8);
				gl.glRotated(30, 0, 1, 0);
				gl.glScaled(10, 5, 1);
				obCubeRender.renderDisplayList(gl);
				gl.glPopMatrix();
				//right
				gl.glPushMatrix();
				gl.glTranslated(10, 5, 0);
				
				gl.glScaled(10, 5, 1);
				obCubeRender.renderDisplayList(gl);
				gl.glPopMatrix();
				obTex.disable(gl);
	
	}
	

	

	public Mesh getFloor() {
		return floorMesh;
	}

	public void setFloor(Mesh floorMesh) {
		this.floorMesh = floorMesh;
	}
	
	
	private Texture loadTexture(GL2 gl, String filename) { Texture tex = null;
	try {
	File f = new File(filename);
	// tex = TextureIO.newTexture(new File(filename), false);
	BufferedImage img = ImageIO.read(f); ImageUtil.flipImageVertically(img);
	tex = AWTTextureIO.newTexture(GLProfile.getDefault(), img, false);
	tex.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
	tex.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST); }
	catch(Exception e) {
	System.out.println("Error loading texture " + filename);
	}
	return tex; }
	
	
}
