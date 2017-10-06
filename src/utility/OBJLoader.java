package utility;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
                    // Parse vertex and add to model
                    break;
                case "f":                           // Face
                    // Parse face
                    break;
                default:
                    throw new IOException("Error parsing line on OBJ file: " + line);
            }
            line = bufferedReader.readLine();
        }



        return model;
    }
}
