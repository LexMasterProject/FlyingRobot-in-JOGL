import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;


public class Room {
	private ProceduralMeshFactory meshFactory;
	private Mesh floorMesh;
	private Render floorRender;
	private GL2 gl;
	
	private Texture floorTex;
	
	public Room(GL2 gl)
	{
		floorMesh = meshFactory.createPlane(40,40,80,80,1,1);
		this.gl=gl;
		
		//load texture
		floorTex=loadTexture(gl, "download.jpeg");
	}
	
	public void prepareForRender()
	{
		floorRender= new Render(floorMesh);
		floorRender.initialiseDisplayListWithTex(gl);
	}
	
	public void display()
	{
		floorTex.enable(gl);
		floorTex.bind(gl);
		floorTex.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE,GL2.GL_MODULATE);
		floorRender.renderDisplayList(gl);
		floorTex.disable(gl);
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
