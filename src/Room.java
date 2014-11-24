import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;


public class Room {
	private Mesh floorMesh,leftMesh;
	private Render floorRender,leftRender;
	private GL2 gl;
	
	private Texture floorTex,leftTex;
	
	public Room(GL2 gl)
	{
		this.gl=gl;

		floorMesh = ProceduralMeshFactory.createPlane(40,40,80,80,1,1);
		leftMesh= ProceduralMeshFactory.createPlane(10,40,80,80,1,1);
		
		//load texture
		floorTex=loadTexture(gl, "floor.jpg");
		leftTex=loadTexture(gl, "wall.jpeg");
		
		createRenderObjects();

	}
	
	private void createRenderObjects()
	{
		floorRender= new Render(floorMesh);
		floorRender.initialiseDisplayListWithTex(gl);
		
		leftRender=new Render(leftMesh);
		leftRender.initialiseDisplayListWithTex(gl);	
	}
	
	public void display()
	{
		gl.glPushMatrix();
		floorTex.enable(gl);
		floorTex.bind(gl);
		floorTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		floorRender.renderDisplayList(gl);
		floorTex.disable(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(-20, 5, 0);
		gl.glRotatef(-90, 0, 0, 1);
		leftTex.enable(gl);
		leftTex.bind(gl);
		leftTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		leftRender.renderDisplayList(gl);
		leftTex.disable(gl);
		gl.glPopMatrix();
		
		
		//floorRender.wireframeImmediateMode(gl, true);
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
