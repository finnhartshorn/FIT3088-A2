package utility;

import com.jogamp.opengl.GL2;
//import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GLAutoDrawable;
import utility.math.Face;
import utility.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL2.GL_POLYGON;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;

public class Model {
    private final List<Vector3f> vertices = new ArrayList<Vector3f>();
    private final List<Face> faces = new ArrayList<Face>();

    public Model() {}

    public void addVertex(Vector3f vertex) {
        vertices.add(vertex);
    }

    public void addFace(Face face) {
        faces.add(face);
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() {
        return faces;
    }

    private void drawFace(GLAutoDrawable glAutoDrawable, Face face) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        int[] vertexIndices = face.getVertexIndices();
        switch( vertexIndices.length) {
            case 3:
                gl.glBegin(GL_TRIANGLES);
                break;
            case 4:
                gl.glBegin(GL_QUADS);
                break;
            default:
                gl.glBegin(GL_POLYGON);
        }
        gl.glColor3f(0.5f, 0.5f, 0.5f);
        for (int vertexIndex: vertexIndices) {
            Vector3f v = vertices.get(vertexIndex-1);
            gl.glVertex3f(v.x, v.y, v.z);
        }
        gl.glEnd();
    }

    public void draw(GLAutoDrawable glAutoDrawable) {
        for(Face face: faces) {
            drawFace(glAutoDrawable, face);
        }
    }
}
