package utility;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static utility.Vector3f.calculateNormal;
import static utility.Vector3f.divideVector;
import static utility.Vector3f.sumVectors;

public class OBJLoader {

    public static Model loadOBJFile(File file) throws IOException{
        Model model = new Model();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        while (line != null) {
            switch (line.split(" ")[0]) {
                case "#":                           // Comment
                    // Do Nothing
                    break;
                case "vn":                          // Vertex Normal
                    // Also do nothing
                    break;
                case "v":                           // Vertex
                    model.addVertex(parseVertex(line));
                    break;
                case "f":                           // Face
                    int[] face = parseFace(line);
                    if (face.length > 3) {                                      // Polygons are turned into triangles using a triangle fan, not really necessary, but it simplifies some of the later opengl logic
                        model.addFaces(createFan(face));
                    } else {
                        model.addFace(face);
                    }
                    break;
                case "g":
                    break;
                case "s":
                    break;
                case "":
                    break;
                default:
                    throw new IOException("Error parsing line on OBJ file: " + line.split(" ")[0] + " | " + line);
            }
            line = bufferedReader.readLine();
        }

        bufferedReader.close();

        List<float[]> normals = model.getNormals();

        for (int i = 0; i < model.getVertices().size(); i++) {
            normals.add(new float[] {0,0,0});
        }

        for (int[] face : model.getFaces()) {            // Index face, and calculate face normal and vector normals
            ArrayList<float[]> faceVertices = new ArrayList<>();
            for(int vertex : face) {                                         // Convert vertex indices to actual vertices
                faceVertices.add(model.getVertices().get(vertex-1));
            }

            float[] faceNormal = calculateNormal(faceVertices);
            model.addFaceNormal(faceNormal);

            for (int vertex : face) {
                float[] currentNormal = model.getNormals().get(vertex - 1);
                normals.set(vertex-1, sumVectors(faceNormal, currentNormal));
            }
        }

        for (int i = 0; i < normals.size(); i++ ) {
            normals.set(i, divideVector(normals.get(i), 3f));
        }
        model.setNormals(normals);



        return model;
    }

    private static ArrayList<int[]> createFan(int[] face) {
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

    private static float[] parseVertex(String line) {
        String[] vectorStr = line.split(" ");
        return new float[]{Float.valueOf(vectorStr[1]), Float.valueOf(vectorStr[2]), Float.valueOf(vectorStr[3])};
    }

    private static int[] parseFace(String line) {               // Assumes no normals
        List<String> face = new ArrayList<>(Arrays.asList(line.split(" ")));
        face.remove(0);
        return face.stream().mapToInt(Integer::parseInt).toArray();
    }
}
