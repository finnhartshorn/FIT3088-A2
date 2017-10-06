import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

public class Renderer implements GLEventListener {

    public static void main(String[] args) {
        new Renderer().setup();
    }

    private void setup() {
        final GLProfile glProfile = GLProfile.get(GLProfile.GL3);                  // Use OpenGL3
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
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
//        GL4 gl = glAutoDrawable.getGL().getGL4();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}
