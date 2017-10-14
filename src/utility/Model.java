package utility;

import com.jogamp.opengl.GL2;
//import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GLAutoDrawable;
import utility.math.Face;
import utility.math.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL2.GL_POLYGON;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;

public class Model {
    private final List<Vector3f> vertices = new ArrayList<Vector3f>();
    private final List<Face> faces = new ArrayList<Face>();
    private List<Vector3f> normals = new ArrayList<Vector3f>();

    public Model() {}

    public void addVertex(Vector3f vertex) {
        vertices.add(vertex);
    }

    public void addFace(Face face) {
        faces.add(face);
        normals.add(calculateNormal(face));
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() {
        return faces;
    }

    private void drawFace(GLAutoDrawable glAutoDrawable, int faceIndex) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        int[] vertexIndices = faces.get(faceIndex).getVertexIndices();
        switch( vertexIndices.length) {
            case 3:
                gl.glBegin(GL_TRIANGLES);
//                Vector3f normal = normals.get(faceIndex);
//                gl.glNormal3f(normal.x, normal.y, normal.z);
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
        IntStream.range(0, faces.size()).forEach(n -> drawFace(glAutoDrawable, n));
    }

    public Vector3f calculateNormal(Face face) {
        Vector3f normalVector = new Vector3f();
        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();

        List<Vector3f> faceVectors = new ArrayList<Vector3f>();
        Arrays.stream(face.getVertexIndices()).forEach(i -> faceVectors.add(vertices.get(i-1)));


        switch (face.getVertexIndices().length) {
            case 3:
                a.x = faceVectors.get(0).x - faceVectors.get(1).x;
                a.y = faceVectors.get(0).y - faceVectors.get(1).y;
                a.z = faceVectors.get(0).z - faceVectors.get(1).z;

                a.x = faceVectors.get(1).x - faceVectors.get(2).x;
                a.y = faceVectors.get(1).y - faceVectors.get(2).y;
                a.z = faceVectors.get(1).z - faceVectors.get(2).z;

                normalVector.x = (a.y * b.z) - (a.z * b.y);
                normalVector.y = (a.z * b.x) - (a.x * b.z);
                normalVector.z = (a.x * b.y) - (a.y * b.x);

                normalVector.normalize();
                break;
        }

        return normalVector;
    }
}
