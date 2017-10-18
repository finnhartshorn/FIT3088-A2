package utility.PLY;

// Custom exception for unexpected types when parsing a PLY file
public class TypeException extends Exception {
    public TypeException(String message) {
        super(message);
    }
}