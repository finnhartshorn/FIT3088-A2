package utility;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static utility.Vector3f.calculateNormal;
import static utility.Vector3f.divideVector;
import static utility.Vector3f.sumVectors;


public class Model {
    private int offset;
    private List<float[]> vertices = new ArrayList<float[]>();
    private List<int[]> faces = new ArrayList<int[]>();
    private List<float[]> normals = new ArrayList<float[]>();
    private List<float[]> faceNormals = new ArrayList<>();

    public Model(int offset) { this.offset = offset; }

    public void addVertex(float[] vertex) {
        vertices.add(vertex);
    }

    public void addFace(int[] face) {
        if (face.length > 3) {
            ArrayList<int[]> temp = createFan(face);
            addFaces(temp);
        } else {
            faces.add(face);
        }
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
                float[] n = normals.get(vertexIndex-offset);
                gl.glNormal3f(n[0], n[1], n[2]);
            }
            float[] v = vertices.get(vertexIndex-offset);
            gl.glVertex3f(v[0], v[1], v[2]);
        }
        gl.glEnd();
    }

    public void draw(GLAutoDrawable glAutoDrawable, boolean perVertexNormals) {
        IntStream.range(0, faces.size()).forEach(n -> drawFace(glAutoDrawable, n, perVertexNormals));
    }

    public static ArrayList<int[]> createFan(int[] face) {
        ArrayList<int[]> triangleList = new ArrayList<>();

        Stack<Integer> faceStack = new Stack<>();
        faceStack.addAll(Arrays.stream(face).boxed().collect(Collectors.toList()));
        int v0 = faceStack.pop();
        int v1 = faceStack.pop();

        while (!faceStack.isEmpty()) {
            int v2 = faceStack.pop();
            triangleList.add(new int[] {v0, v1, v2});
            v1 = v2;
        }

        return triangleList;
    }

    public void calculateNormals(int offset) {
        while (vertices.size() > normals.size()) {
            normals.add(new float[] {0.0f,0.0f,0.0f});
        }

        for (int[] face : faces) {            // Index face, and calculate face normal and vector normals
            ArrayList<float[]> faceVertices = new ArrayList<>();
            for(int vertex : face) {                                         // Convert vertex indices to actual vertices
                faceVertices.add(vertices.get(vertex-offset));
            }

            float[] faceNormal = calculateNormal(faceVertices);
            addFaceNormal(faceNormal);

            for (int vertex : face) {
                float[] currentNormal = normals.get(vertex - offset);
                normals.set(vertex-offset, sumVectors(faceNormal, currentNormal));
            }
        }
        for (int i = 0; i < normals.size(); i++) {
            normals.set(i, divideVector(normals.get(i), 3.0f));
        }
    }

}
