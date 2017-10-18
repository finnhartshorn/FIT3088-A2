package utility;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.HashMap;


// Class for defining a Material, also contains some preset materials
public class Material {
    private float[] ambient;
    private float[] diffuse;
    private float[] specular;
    private float shininess;

    public static HashMap<String, Material> materials;
    static {
        materials = new HashMap<>();
        materials.put("Default", new Material(new float[] { 0.2f, 0.2f, 0.2f, 1.0f }, new float[] { 0.8f, 0.8f, 0.8f, 1.0f }, new float[] {0.0f, 0.0f, 0.0f, 1.0f },0f));
        materials.put("Gold", new Material(new float[] { 0.24725f, 0.1995f, 0.0745f, 1.0f }, new float[] { 0.75164f, 0.60648f, 0.22648f, 1.0f }, new float[] {0.628281f, 0.555802f, 0.366065f, 1.0f },0.4f));
        materials.put("Copper", new Material(new float[] { 0.19125f, 0.0735f, 0.0225f, 1.0f }, new float[] { 0.7038f,	0.27048f, 0.0828f, 1.0f }, new float[] { 0.256777f, 0.137622f, 0.086014f, 1.0f },0.1f));
        materials.put("White Plastic", new Material(new float[] { 0.05f, 0.05f, 0.05f, 1.0f }, new float[] { 0.55f,	0.55f, 0.55f, 1.0f }, new float[] { 0.70f, 0.70f, 0.70f, 1.0f },0.25f));
    }

    public Material(float[] ambient, float[] diffuse, float[] specular, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess * 128f;
    }

    // A helper function for apply a material
    public void apply(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, shininess);
    }

    public float[] getAmbient() {
        return ambient;
    }

    public void setAmbient(float[] ambient) {
        this.ambient = ambient;
    }

    public float[] getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(float[] diffuse) {
        this.diffuse = diffuse;
    }

    public float[] getSpecular() {
        return specular;
    }

    public void setSpecular(float[] specular) {
        this.specular = specular;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }
}
