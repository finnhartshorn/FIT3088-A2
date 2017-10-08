package utility;


import utility.math.Face;
import utility.math.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                    model.addFace(parseFace(line));
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

        return model;
    }

    private static Vector3f parseVertex(String line) {
        String[] vector = line.split(" ");
        float xCoord = Float.valueOf(vector[1]);
        float yCoord = Float.valueOf(vector[2]);
        float zCoord = Float.valueOf(vector[3]);
        return new Vector3f(xCoord, yCoord, zCoord);
    }

    private static Face parseFace(String line) {               // Assumes no normals
        List<String> face = new ArrayList<>(Arrays.asList(line.split(" ")));
        face.remove(0);
        return new Face(face.stream().mapToInt(Integer::parseInt).toArray());
    }
}
