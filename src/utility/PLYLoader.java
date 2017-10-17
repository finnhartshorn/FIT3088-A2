package utility;


import utility.PLY.Element;
import utility.PLY.Property;
import utility.PLY.PropertyList;
import utility.PLY.TypeException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static utility.Vector3f.calculateNormal;
import static utility.Vector3f.divideVector;
import static utility.Vector3f.sumVectors;

public class PLYLoader {

    public static Model loadPLYFile(File file) throws IOException, TypeException {
        Model model = new Model(0);
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
                    case "element":
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
                System.out.println("Finished Header");
                for(Element element : elementList) {                                                                        // After elements are read, parse data for each element
                    System.out.println(element.getName() + " : " + element.getPropertyList().size());
                    for (int i = 0; i < element.getLength(); i++) {
                        element.addData(line);
                        line = bufferedReader.readLine();
                    }
                }
                break;
            }
            line = bufferedReader.readLine();
        }

        System.out.println("Finished Reading PLY File");
        bufferedReader.close();

        for(Element element : elementList) {
            if (!element.validate()) {
                System.out.println("ERROR");
            }
        }

        Element vertexElement = elementMap.get("vertex");
        ArrayList<Float> x = vertexElement.getPropertyMap().get("x").getData();
        ArrayList<Float> y = vertexElement.getPropertyMap().get("y").getData();
        ArrayList<Float> z = vertexElement.getPropertyMap().get("z").getData();

        for (int i = 0; i < x.size(); i++) {
            model.addVertex(new float[]{x.get(i), y.get(i), z.get(i)});
        }

        System.out.println("Vertices Loaded");

        Element faceElement = elementMap.get("face");
        ArrayList<Integer[]> faces = faceElement.getPropertyMap().get("vertex_indices").getData();
        for (int i = 0; i < faces.size(); i++) {
            Object[] face = faces.get(i);
            model.addFace(Arrays.stream(Arrays.copyOf(face, face.length, Integer[].class)).mapToInt(Integer::intValue).toArray());                      // This is a bit of a mess, used to convert Integer[] to int[]
        }

        model.calculateNormals(0);

        return model;

    }

    private static void parseProperty(Element element, String line) {
        String[] propertytString = line.split(" ");
        System.out.println("Property: " + propertytString[1] + " : "+ propertytString[2]);
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

    private static Element parseElement(String line) {
        String[] elementString = line.split(" ");
        System.out.println("Element: " + elementString[1] + " : "+ elementString[2]);
        return new Element(elementString[1], Integer.valueOf(elementString[2]));
    }

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

//    private static float[] parseVertex(String line) {
//        String[] vectorStr = line.split(" ");
//        return new float[]{Float.valueOf(vectorStr[1]), Float.valueOf(vectorStr[2]), Float.valueOf(vectorStr[3])};
//    }
//
//    private static int[] parseFace(String line) {               // Assumes no normals
//        List<String> face = new ArrayList<>(Arrays.asList(line.split(" ")));
//        face.remove(0);
//        return face.stream().mapToInt(Integer::parseInt).toArray();
//    }
}
