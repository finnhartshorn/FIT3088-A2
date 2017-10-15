import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import utility.Model;
import utility.OBJLoader;

import javax.swing.*;

import java.io.File;
import java.io.IOException;


public class Renderer implements GLEventListener {

    private GLU glu;
    private Model model;

    public static void main(String[] args) {
        new Renderer().setup();
    }

    private void setup() {
        final GLProfile glProfile = GLProfile.get(GLProfile.GL2);                  // Use OpenGL2
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);       //

        final GLCanvas canvas = new GLCanvas(glCapabilities);
        Renderer renderer = new Renderer();

        canvas.addGLEventListener(renderer);
//        canvas.addKeyListener(renderer);

        canvas.setSize(1074, 768);

        final FPSAnimator animator = new FPSAnimator(canvas, 60, true);

        final JFrame frame = new JFrame("Test");

        frame.getContentPane().add(canvas);

        // Kill
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if(animator.isStarted()) {
                    animator.stop();
                }
                System.exit(0);
            }
        });
        frame.setSize(frame.getContentPane().getPreferredSize());

        frame.setVisible(true);

        animator.start();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);      // Enable Textures
        gl.glShadeModel(GL2.GL_SMOOTH);

        float[] ambient = {1.0f, 1.0f, 1.0f , 1.0f};
        float[] diffuse = {1.0f, 1.0f, 1.0f , 1.0f};
        float[] lightPositon = {0.0f, 0.0f, 5.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPositon, 0);

        gl.glEnable(GL2.GL_LIGHT1);

        try {
            model = OBJLoader.loadOBJFile(new File("airboat.obj"));
            System.out.println("Obj file loaded");
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glLoadIdentity();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glTranslatef(0,0,-2);
        gl.glScalef(0.1f, 0.1f, 0.1f);

        model.draw(glAutoDrawable);

        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int xStart, int yStart, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        if (height <= 0) { height = 1; }
        final float h=(float)width/(float)height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, h, 0.1, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
