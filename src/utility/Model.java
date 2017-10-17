package utility;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.jogamp.opengl.GL.GL_TRIANGLES;


public class Model {
    private List<float[]> vertices = new ArrayList<float[]>();
    private List<int[]> faces = new ArrayList<int[]>();
    private List<float[]> normals = new ArrayList<float[]>();
    private List<float[]> faceNormals = new ArrayList<>();

    public Model() {}

    public void addVertex(float[] vertex) {
        vertices.add(vertex);
    }

    public void addFace(int[] face) {
        faces.add(face);
    }

    public void addFaces(List<int[]> faceList) {
        faces.addAll(faceList);
    }

    public void addNormal(float[] normal) { normals.add(normal); }

    public List<float[]> getVertices() {
        return vertices;
    }

    public float[] getVertex(int index) {
        return vertices.get(index);
    }

    public List<int[]> getFaces() {
        return faces;
    }

    public List<float[]> getNormals() { return normals; }

    public void setVertices(List<float[]> vertices) {
        this.vertices = vertices;
    }

    public void setFaces(List<int[]> faces) {
        this.faces = faces;
    }

    public void setNormals(List<float[]> normals) {
        this.normals = normals;
    }

    public void addFaceNormal(float[] faceNormal) {
        faceNormals.add(faceNormal);
    }

    public List<float[]> getFaceNormals() {
        return faceNormals;
    }

    public void setFaceNormals(List<float[]> faceNormals) {
        this.faceNormals = faceNormals;
    }

    private void drawFace(GLAutoDrawable glAutoDrawable, int faceIndex) {
        drawFace(glAutoDrawable, faceIndex, true);
    }

    private void drawFace(GLAutoDrawable glAutoDrawable, int faceIndex, boolean perVertexNormals) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        int[] vertexIndices = faces.get(faceIndex);

        gl.glBegin(GL_TRIANGLES);

        gl.glColor3f(0.5f, 0.5f, 0.5f);
        if (!perVertexNormals) {
            float[] n = faceNormals.get(faceIndex);
            gl.glNormal3f(n[0], n[1], n[2]);
        }

        for (int vertexIndex: vertexIndices) {
            if (perVertexNormals) {
                float[] n = normals.get(vertexIndex-1);
                gl.glNormal3f(n[0], n[1], n[2]);
            }
            float[] v = vertices.get(vertexIndex-1);
            gl.glVertex3f(v[0], v[1], v[2]);
        }
        gl.glEnd();
    }

    public void draw(GLAutoDrawable glAutoDrawable, boolean perVertexNormals) {
        IntStream.range(0, faces.size()).forEach(n -> drawFace(glAutoDrawable, n, perVertexNormals));
    }

}
