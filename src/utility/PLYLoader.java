package utility;


import utility.PLY.Element;
import utility.PLY.Property;
import utility.PLY.PropertyList;
import utility.PLY.TypeException;

import java.io.*;
import java.util.*;

/*
 * A Class for parsing PLY files.
 *  Every element and property is read in, however only the vertex and face elements are used to produce a model for rendering
 */
public class PLYLoader {

    public static Model loadPLYFile(File file) throws IOException, TypeException {
        Model model = new Model();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        boolean header = true;
        HashMap<String, Element> elementMap = new HashMap<>();
        ArrayList<Element> elementList = new ArrayList<>();
        assert(line.equals("ply\n"));
        line = bufferedReader.readLine();
        assert line.startsWith("format ascii 1.0");
        line = bufferedReader.readLine();
        while (line != null) {
            if (header) {
                switch (line.split(" ")[0]) {
                    case "comment":                           // Comment
                        // Do Nothing
                        break;
                    case "element":                                                 // Create a new element and add it to the list and Hashmap, so it can be found via name and index
                        Element temp = parseElement(line);
                        elementList.add(temp);
                        elementMap.put(temp.getName(), temp);
                        break;
                    case "property":
                        parseProperty(elementList.get(elementList.size() - 1), line);                   // Add property to last parsed elemement
                        break;
                    case "end_header":
                        header = false;
                        break;
                    default:
                        throw new IOException("Error parsing line on PLY file: " + line.split(" ")[0] + " | " + line);
                }
            } else {
                for(Element element : elementList) {                                                                        // After elements are read, parse data for each element
                    for (int i = 0; i < element.getLength(); i++) {
                        element.addData(line);
                        line = bufferedReader.readLine();
                    }
                }
                break;
            }
            line = bufferedReader.readLine();
        }

        bufferedReader.close();

        for(Element element : elementList) {            // Check each element to make sure it is the correct size
            if (!element.validate()) {
                System.out.println("ERROR");
            }
        }

        // Get the vertex elements and adds them to the model
        Element vertexElement = elementMap.get("vertex");
        ArrayList<Float> x = vertexElement.getPropertyMap().get("x").getData();
        ArrayList<Float> y = vertexElement.getPropertyMap().get("y").getData();
        ArrayList<Float> z = vertexElement.getPropertyMap().get("z").getData();

        for (int i = 0; i < x.size(); i++) {
            model.addVertex(new float[]{x.get(i), y.get(i), z.get(i)});
        }

        // Gets the face elements and adds them to teh model
        Element faceElement = elementMap.get("face");
        ArrayList<Integer[]> faces = faceElement.getPropertyMap().get("vertex_indices").getData();
        for (int i = 0; i < faces.size(); i++) {
            Object[] face = faces.get(i);
            model.addFace(Arrays.stream(Arrays.copyOf(face, face.length, Integer[].class)).mapToInt(Integer::intValue).toArray());                      // This is a bit of a mess, used to convert Integer[] to int[]
        }
        // Calculates normals
        model.calculateNormals();

        return model;
    }

    // Parses a property and adds it to the given element
    private static void parseProperty(Element element, String line) {
        String[] propertytString = line.split(" ");
        switch (propertytString[1]) {
            case "char":                                                                // These are all treated as ints. No data is lost, it's just a little less space efficient.
            case "uchar":
            case "short":
            case "ushort":
            case "int":
            case "uint":
                element.addProperty(propertytString[2], new Property<Integer>(Integer.class));
                break;
            case "float":
                element.addProperty(propertytString[2], new Property<Float>(Float.class));
                break;
            case "double":
                element.addProperty(propertytString[2], new Property<Double>(Double.class));
                break;
            case "list":
                parsePropertyList(element, propertytString);

        }
    }

    // Parses element string and returns element with name and length
    private static Element parseElement(String line) {
        String[] elementString = line.split(" ");
        return new Element(elementString[1], Integer.valueOf(elementString[2]));
    }

    // Special case for when a property is a list
    private static void parsePropertyList(Element element, String[] line) {
        switch (line[3]) {
            case "char":                                                                // These are all treated as ints. No data is lost, it's just a little less space efficient.
            case "uchar":
            case "short":
            case "ushort":
            case "int":
            case "uint":
                element.addProperty(line[4], new PropertyList<ArrayList, Integer>(ArrayList.class, Integer.class));
                break;
            case "float":
                element.addProperty(line[4], new PropertyList<ArrayList, Float>(ArrayList.class, Float.class));
                break;
            case "double":
                element.addProperty(line[4], new PropertyList<ArrayList, Double>(ArrayList.class, Double.class));
                break;
        }
    }
}
