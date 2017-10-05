package utility.math;

public class Vector3f {

    public float x;
    public float y;
    public float z;

    public Vector3f() {
        x = y = z = 0;
    }

    public Vector3f(Vector3f sourceVector) {
        x = sourceVector.x;
        y = sourceVector.y;
        z = sourceVector.z;
    }

    public Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
