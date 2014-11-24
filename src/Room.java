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
	private Render floorRender,wallRender;
	private GL2 gl;
	private Texture floorTex,wallTex;
	
	private double size;
	private double wallHeight;
	
	public Room(GL2 gl)
	{
		this.gl=gl;
		reset();
		floorMesh = ProceduralMeshFactory.createPlane(size,size,(int)(2*size),(int)(2*size),1,1);
		wallMesh= ProceduralMeshFactory.createPlane(wallHeight,size,80,80,1,1);
		
		//load texture
		floorTex=loadTexture(gl, "floor.jpg");
		wallTex=loadTexture(gl, "wall.jpeg");
		createRenderObjects();

	}
	
	private void reset()
	{
		wallHeight=20;
		size=40;
	}
	
	private void createRenderObjects()
	{
		floorRender= new Render(floorMesh);
		floorRender.initialiseDisplayListWithTex(gl);
		
		wallRender=new Render(wallMesh);
		wallRender.initialiseDisplayListWithTex(gl);	
	}
	
	public void display()
	{
		drawFloor();
		drawWalls();	
	}
	
	private void drawFloor()
	{
		floorTex.enable(gl);
		floorTex.bind(gl);
		floorTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		gl.glPushMatrix();
		floorRender.renderDisplayList(gl);
		gl.glPopMatrix();
		floorTex.disable(gl);
	}
	
	private void drawWalls()
	{
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
