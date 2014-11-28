import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;


public class Room {
	private Mesh floorMesh,wallMesh;
	private Render floorCeilingRender,wallRender;
	private GL2 gl;
	private Texture floorTex,wallTex,ceilingTex;
	
	public static final double size=40;
	public static final double wallHeight=20;
	
	
	public Room(GL2 gl)
	{
		this.gl=gl;
	
		floorMesh = ProceduralMeshFactory.createPlane(size,size,(int)(2*size),(int)(2*size),1,1);
		wallMesh= ProceduralMeshFactory.createPlane(wallHeight,size,80,80,1,1);
		
		//load texture
		floorTex=loadTexture(gl, "floor.jpg");
		floorTex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		floorTex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		wallTex=loadTexture(gl, "wall.jpg");
		wallTex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		wallTex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		
		ceilingTex=loadTexture(gl, "ceiling.jpg");
		
		createRenderObjects();

	}
	

	
	private void createRenderObjects()
	{
		floorCeilingRender= new Render(floorMesh);
		floorCeilingRender.initialiseDisplayListWithTex(gl);
		
		wallRender=new Render(wallMesh);
		wallRender.initialiseDisplayListWithTex(gl);	
	}
	
	public void display()
	{
		drawFloor();
		drawWalls();	
		drawCeiling();
	}
	
	private void drawCeiling()
	{
		ceilingTex.enable(gl);
		ceilingTex.bind(gl);
		ceilingTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		
		gl.glMatrixMode(GL2.GL_TEXTURE); 
		gl.glLoadIdentity(); 
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
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
		
	
		gl.glPushMatrix();
		floorCeilingRender.renderDisplayList(gl);
		gl.glPopMatrix();
		floorTex.disable(gl);
	}
	
	private void drawWalls()
	{	
		gl.glMatrixMode(GL2.GL_TEXTURE); 
		gl.glLoadIdentity(); 
	//	gl.glScaled(1, 3, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		wallTex.enable(gl);
		wallTex.bind(gl);
		wallTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		
		gl.glPushMatrix();
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
		
		gl.glPopMatrix();
		
		wallTex.disable(gl);
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
