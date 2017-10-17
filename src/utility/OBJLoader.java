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
        Model model = new Model(-1);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        while (line != null) {
            switch (line.split(" ")[0]) {
                case "#":                           // Comment
                    // Do Nothing
                    break;
                case "vn":                          // Vertex Normal
                    model.addNormal(parseVertex(line));
                    break;
                case "v":                           // Vertex
                    model.addVertex(parseVertex(line));
                    break;
                case "f":                           // Face
                    int[] face = parseFace(line);
                    model.addFace(face);
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

        if (model.getNormals().size() == 0) {
            model.calculateNormals(-1);
        }



        return model;
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
