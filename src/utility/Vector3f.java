package utility;

import java.util.ArrayList;

public class Vector3f {

    public static float[] calculateNormal(ArrayList<float[]> face) {
        float[] normalVector = { 0f, 0f, 0f };
        float[] a = { 0f, 0f, 0f };
        float[] b = { 0f, 0f, 0f };


        a[0] = face.get(1)[0] - face.get(0)[0];
        a[1] = face.get(1)[1] - face.get(0)[1];
        a[2] = face.get(1)[2] - face.get(0)[2];

        b[0] = face.get(2)[0] - face.get(0)[0];
        b[1] = face.get(2)[1] - face.get(0)[1];
        b[2] = face.get(2)[2] - face.get(0)[2];

        normalVector[0] = (a[1] * b[2]) - (a[2] * b[1]);
        normalVector[1] = (a[2] * b[0]) - (a[0] * b[2]);
        normalVector[2] = (a[0] * b[1]) - (a[1] * b[0]);

        normalVector = normalize(normalVector);

        return normalVector;
    }

    public static float[] normalize(float[] vector) {
        float length = (float)Math.sqrt((vector[0] * vector[0]) + (vector[1] * vector[1]) + (vector[2] * vector[2]));

        if (length == 0.0f) {
            length = 1.0f;
        }

        vector[0] /= length;
        vector[1] /= length;
        vector[2] /= length;

        return vector;
    }

    public static float[] sumVectors(float[] vector1, float[] vector2) {
        return new float[] { vector1[0] + vector2[0], vector1[0] + vector2[0], vector1[0] + vector2[0] };
    }

    public static float[] divideVector(float[] vector, float divisor) {
        return new float[] { vector[0]/divisor, vector[1]/divisor, vector[2]/divisor };
    }
}
