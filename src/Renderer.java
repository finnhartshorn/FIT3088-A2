import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import utility.Model;
import utility.OBJLoader;

import javax.swing.*;

import java.io.File;
import java.io.IOException;

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_NICEST;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

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
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH);

        try {
            model = OBJLoader.loadOBJFile(new File("airboat.obj"));
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

        gl.glTranslatef(0,0,-1);
        gl.glScalef(0.1f, 0.1f, 0.1f);

        model.draw(glAutoDrawable);

        gl.glFlush();

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}
