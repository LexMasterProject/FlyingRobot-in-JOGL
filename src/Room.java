import javax.media.opengl.GL2;


public class Room {
	private ProceduralMeshFactory meshFactory;
	private Mesh floorMesh;
	private Render floorRender;
	private GL2 gl;
	
	public Room(GL2 gl)
	{
		floorMesh = meshFactory.createPlane(40,40,40,40,1,1);
		this.gl=gl;
	}
	
	public void prepareForRender()
	{
		floorRender= new Render(floorMesh);
		floorRender.initialiseDisplayList(gl);
	}
	
	public void display()
	{
		//floorRender.renderDisplayList(gl);
		floorRender.wireframeImmediateMode(gl, true);
	}
	public Mesh getFloor() {
		return floorMesh;
	}

	public void setFloor(Mesh floorMesh) {
		this.floorMesh = floorMesh;
	}
	
	
}
