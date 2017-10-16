import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import utility.Model;
import utility.OBJLoader;

import javax.swing.*;

//import java.awt.event.KeyListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;


public class Renderer implements GLEventListener, KeyListener, MouseListener {

    private GLU glu;
    private Model model;
    private boolean perspective = true;
    private float xRotation = 0f;
    private float yRotation = 0f;
    private float zRotation = 0f;
    private float rotationFactor = 0.8f;
    private float zoomFactor = 0f;
    private boolean light = true;
    private Point mousePrevCoords;
    private boolean leftMouseDown = false;
    private boolean shiftPressed = false;
    private boolean ctrlPressed = false;
    private float[] translation = {0f, 0f, -2f};

    private float[] ambient = {1.0f, 1.0f, 1.0f , 1.0f};
    private float[] diffuse = {1.0f, 1.0f, 1.0f , 1.0f};
    private float[] lightPosition = {0.0f, 0.0f, 5.0f, 1.0f};

    public static void main(String[] args) {
        new Renderer().setup();
    }

    private void setup() {
        final GLProfile glProfile = GLProfile.get(GLProfile.GL2);                  // Use OpenGL2
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);       //

        final GLCanvas canvas = new GLCanvas(glCapabilities);
        Renderer renderer = new Renderer();

        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer);
        canvas.addMouseListener(renderer);

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

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition, 0);

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

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition, 0);

        if (leftMouseDown) {
            Point mouseCoords = MouseInfo.getPointerInfo().getLocation();
            float xDelta = (mouseCoords.x - mousePrevCoords.x) * (rotationFactor/2);
            float yDelta = (mouseCoords.y - mousePrevCoords.y) * (rotationFactor/2);
            System.out.println(xDelta);
            if (shiftPressed) {
                translation[0] += (xDelta)/100;
                translation[1] -= (yDelta)/100;
            } else if (ctrlPressed) {
                translation[2] += (yDelta)/100;
            } else {
                yRotation += (xDelta);
                xRotation += (yDelta);
            }
            mousePrevCoords = mouseCoords;
        }

        gl.glTranslatef(translation[0], translation[1], translation[2]);
        gl.glScalef(0.1f, 0.1f, 0.1f);

        gl.glRotatef(xRotation, 1f, 0f, 0f);
        gl.glRotatef(yRotation, 0f, 1f, 0f);
        gl.glRotatef(zRotation, 0f, 0f, 1f);

        model.draw(glAutoDrawable);

        gl.glFlush();
    }

    public void _reshape(GLAutoDrawable glAutoDrawable, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        if (height <= 0) { height = 1; }
        final float h=(float)width/(float)height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
//        gl.glPushMatrix();
        gl.glLoadIdentity();

        if (perspective) {
            glu.gluPerspective(45.0, h, 0.1, 100.0);
        } else {
            gl.glOrtho(-1,1,-1,1,-5,5);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int xStart, int yStart, int width, int height) {
        _reshape(glAutoDrawable, width, height);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                yRotation -= rotationFactor;
                break;
            case KeyEvent.VK_D:
                yRotation += rotationFactor;
                break;
            case KeyEvent.VK_X:
                xRotation -= rotationFactor;
                break;
            case KeyEvent.VK_W:
                xRotation += rotationFactor;
                break;
            case KeyEvent.VK_Z:
                zRotation -= rotationFactor;
                break;
            case KeyEvent.VK_E:
                zRotation += rotationFactor;
                break;
            case KeyEvent.VK_ADD:                                   // Zooming works for both the -/+ and -/=
            case KeyEvent.VK_EQUALS:
                zoomFactor += 0.1f;
                break;
            case KeyEvent.VK_SUBTRACT:
            case KeyEvent.VK_MINUS:
                zoomFactor -= 0.1f;
                break;
            case KeyEvent.VK_SHIFT:
                shiftPressed = true;
                break;
            case KeyEvent.VK_CONTROL:
                ctrlPressed = true;
                break;
            case KeyEvent.VK_P:
                perspective = !perspective;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                shiftPressed = false;
                break;
            case KeyEvent.VK_CONTROL:
                ctrlPressed = false;
                break;

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton()== MouseEvent.BUTTON1) {
            mousePrevCoords = e.getLocationOnScreen();
            leftMouseDown = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton()== MouseEvent.BUTTON1) {
            leftMouseDown = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
