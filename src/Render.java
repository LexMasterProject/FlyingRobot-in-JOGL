/**
 * The Render class is used for rendering Mesh objects
 * Set up for teaching purposes. A better structure may have used inheritance to define the 
 * the different rendering modes. However, it is useful to have them all here to demonstrate their 
 * differences. 
 * Note that the list of vertices, triangles and normals are in a simple array format
 * in contrast to the more complex Mesh structure. 
 * The use of arrays here is to support efficient rendering, whereas a Mesh structure is typically structured
 * to support ease of editing.
 * If the Mesh structure was to be updated, e.g. a vertex moved, then then relevant data would
 * need to be copied here again, before rendering would produce the correct result.
 *
 * @author    Dr Steve Maddock
 * @version   3.0 (29/07/2013)
 */

import javax.media.opengl.*;

 
public class Render {
  private Mesh mesh;
  private Vertex[] verticesV;
  private double[] verticesd;
  private double[] normals;
  private int[] triangles;
  private int dlist;

  /**
   * Constructor. Copies the vertex, triangle and normal data from the Mesh structure.
   * If the Mesh structure were to change, e.g. a vertex moved, then the data would
   * need to be copied again.
   * The use of simple arrays makes the rendering process more efficient.
   */  
  public Render(Mesh m) {
    reset(m);
  }
  
  public void reset(Mesh m) {
    mesh = m;
    verticesd=m.getVertexList();
    verticesV = m.getVertices();
    normals = m.getNormalList();
    triangles = m.getTriangleList();
    dlist = 0;
  }

  /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
  /* Immediate mode sending individual triangles */
  /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
  
  /**
   * Render a mesh using immediate mode.
   * 
   * @param  gl  the OpenGL context.
   */ 
 
  public void renderImmediateMode(GL2 gl) {
    Material material = mesh.getMaterial();
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, material.getAmbient(), 0);    
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, material.getDiffuse(), 0);    
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, material.getSpecular(), 0);
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, material.getEmission(), 0);
    gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material.getShininess());

    gl.glBegin(GL2.GL_TRIANGLES);
      for (int t=0; t<triangles.length/3; t++)
        for (int i=0; i<3; i++) {
          int index = triangles[t*3+i]*3;
          gl.glNormal3d(normals[index], normals[index+1], normals[index+2]);
          gl.glVertex3d(verticesd[index], verticesd[index+1], verticesd[index+2]);
        }
    gl.glEnd();
  }

  public void renderImmediateModeWithTex(GL2 gl) {
	  	float[] matAmbientDiffuse = {1,1,1, 1.0f};
	    float[] matSpecular = {0.5f,0.5f,0.5f, 1.0f};
	    float[] matShininess = {16.0f};
	    float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbientDiffuse, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);

	    gl.glBegin(GL2.GL_TRIANGLES);
	      for (int t=0; t<triangles.length/3; t++)
	        for (int i=0; i<3; i++) {
	          int index = triangles[t*3+i];
//	          gl.glNormal3d(normals[index], normals[index+1], normals[index+2]);
//	          gl.glVertex3d(verticesd[index], verticesd[index+1], verticesd[index+2]);
	          gl.glTexCoord2dv(verticesV[index].getTextureCoord(), 0);
	          gl.glNormal3dv(verticesV[index].getNormal(), 0);
	          gl.glVertex3dv(verticesV[index].getPosition(), 0);
	        }
	    gl.glEnd();
	  }
  
  public void initialiseDisplayListWithTex(GL2 gl) {
	    dlist = gl.glGenLists(1);
	    gl.glNewList(dlist, GL2.GL_COMPILE);
	      renderImmediateModeWithTex(gl);
	    gl.glEndList(); 
	  }
  /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
  /* Display List */
  /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
  
   /**
   * Render a mesh using a display list.
   * 
   * @param  gl  the OpenGL context.
   */ 
   
  public void initialiseDisplayList(GL2 gl) {
    dlist = gl.glGenLists(1);
    gl.glNewList(dlist, GL2.GL_COMPILE);
      renderImmediateMode(gl);
    gl.glEndList(); 
  }
  
  public void renderDisplayList(GL2 gl) {
    gl.glCallList(dlist);
  }

  
  /**
   * Render a mesh using immediate mode.
   * 
   * @param  gl  the OpenGL context.
   */ 
 
  public void wireframeImmediateMode(GL2 gl, boolean lightingOn) {
    Material material = mesh.getMaterial();
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, material.getAmbient(), 0);    
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, material.getDiffuse(), 0);    
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, material.getSpecular(), 0);
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, material.getEmission(), 0);
    gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material.getShininess());

    if (!lightingOn) gl.glDisable(GL2.GL_LIGHTING);
    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
    gl.glBegin(GL2.GL_TRIANGLES);
      for (int t=0; t<triangles.length/3; t++)
        for (int i=0; i<3; i++) {
          int index = triangles[t*3+i]*3;
          gl.glNormal3d(normals[index], normals[index+1], normals[index+2]);
          gl.glVertex3d(verticesd[index], verticesd[index+1], verticesd[index+2]);
        }
    gl.glEnd();
    if (!lightingOn) gl.glEnable(GL2.GL_LIGHTING);
    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
  }
    
}
