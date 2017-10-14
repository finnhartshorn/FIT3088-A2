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

    public void normalize() {
        double length = Math.sqrt((x * x) + (y * y) + (z * z));

        if (length == 0.0d) {
            length = 1.0d;
        }

        x /= length;
        y /= length;
        z /= length;
    }





//    // calculate the length of the vector
//    float len = (float)(sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z)));
//
//    // avoid division by 0
//    if (len == 0.0f)
//    len = 1.0f;
//
//    // reduce to unit size
//    v.x /= len;
//    v.y /= len;
//    v.z /= len;


}
