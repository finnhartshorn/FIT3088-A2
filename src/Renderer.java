import com.jogamp.opengl.*;

import com.jogamp.opengl.glu.GLU;

import utility.Material;
import utility.Model;
import utility.OBJLoader;
import utility.PLY.TypeException;
import utility.PLYLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;


public class Renderer implements GLEventListener, KeyListener, MouseListener {

    private GLU glu;
    private GL2 gl;
    private Model model;
    private float xRotation = 0f;
    private float yRotation = 0f;
    private float zRotation = 0f;
    private float rotationFactor = 1.6f;                        // How fast the keys and mouse will cause things to rotate
    private float zoomFactor = 1.0f;

    // Booleans for a number of different gui options
    private boolean perspective = true;
    private boolean surfaceRendering = true;
    private boolean vertexNormals = true;
    private boolean aboveLight = false;
    private boolean infrontLight = true;
    private boolean behindLight = false;

    private Material currentMaterial = Material.materials.get("Default");
    private Point mousePrevCoords;
    private boolean leftMouseDown = false;
    private boolean shiftPressed = false;
    private boolean ctrlPressed = false;

    // Stores current translation
    private float[] translation = {0f, -0.8f, -2.5f};


    // Some lighting presets
    private float ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    private float diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    private float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    private float[] aboveLightPosition = {0.0f, 5.0f, 0.0f, 1.0f};
    private float[] infrontLightPosition = {0.0f, 0.0f, 5.0f, 1.0f};
    private float[] behindLightPosition = {0.0f, 0.0f, -5.0f, 1.0f};
    private float lmodel_ambient[] = { 0.4f, 0.4f, 0.4f, 1.0f };
    private float local_view[] = { 0.0f };


    // Initialises OpenGL
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Initialise ambient light
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);

        // Defines some lights, only the front light is enabled by default, all lights can be toggled via the gui
        initLight(glAutoDrawable, GL2.GL_LIGHT1, ambient, specular, diffuse, infrontLightPosition, true);
        initLight(glAutoDrawable, GL2.GL_LIGHT2, ambient, specular, diffuse, behindLightPosition, false);
        initLight(glAutoDrawable, GL2.GL_LIGHT3, ambient, specular, diffuse, aboveLightPosition, false);

        loadFile(new File("bun_zipper.ply"));               // Looks for bun_zipper.ply in working directory by default
    }

    private void initLight(GLAutoDrawable glAutoDrawable, int light, float[] ambient, float[] specular, float[] diffuse, float[] lightPosition, boolean activate) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glLightfv(light, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(light, GL2.GL_SPECULAR, specular, 0);
        gl.glLightfv(light, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(light, GL2.GL_POSITION, lightPosition, 0);

        if (activate) {
            gl.glEnable(light);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) { }


    // Loads and decodes a file into a Model, based on the file extension, returns null on failure and prints out an error message
    public void loadFile(File file) {
        if (file.getName().endsWith(".ply")) {
            try {
                model = PLYLoader.loadPLYFile(file);
            } catch(IOException | TypeException e) {
                System.out.println(e.toString());
                model = null;
            }
        } else if (file.getName().endsWith(".obj")) {
            try {
                model = OBJLoader.loadOBJFile(file);
            } catch(IOException e) {
                System.out.println(e.toString());
                model = null;
            }
        }

    }

    // Draws model to screen, also handles transformations and light/rendering toggles
    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glLoadIdentity();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        if (infrontLight) {
            gl.glEnable(GL2.GL_LIGHT1);
        } else {
            gl.glDisable(GL2.GL_LIGHT1);
        }

        if (behindLight) {
            gl.glEnable(GL2.GL_LIGHT2);
        } else {
            gl.glDisable(GL2.GL_LIGHT2);
        }

        if (aboveLight) {
            gl.glEnable(GL2.GL_LIGHT3);
        } else {
            gl.glDisable(GL2.GL_LIGHT3);
        }

        if (surfaceRendering) {
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        } else {
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        }

        currentMaterial.apply(glAutoDrawable);

        if (leftMouseDown) {                                                            // If the left mouse is down, the mouse movement since the last frame is calculated and used to rotate or translate the scene
            Point mouseCoords = MouseInfo.getPointerInfo().getLocation();
            float xDelta = (mouseCoords.x - mousePrevCoords.x) * (rotationFactor/8);
            float yDelta = (mouseCoords.y - mousePrevCoords.y) * (rotationFactor/8);
            if (shiftPressed) {
                translation[0] += (xDelta)/100;
                translation[1] -= (yDelta)/100;
            } else if (ctrlPressed) {
                translation[2] += (yDelta)/100;
            } else {
                yRotation += (xDelta*2);
                xRotation += (yDelta*2);
            }
            mousePrevCoords = mouseCoords;
        }

        int[] windowSize = {0,0,0,0};                                       // Windows size is needed for the reshape method
        gl.glGetIntegerv( GL2.GL_VIEWPORT, windowSize, 0);
        _reshape(glAutoDrawable, windowSize[2], windowSize[3]);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glTranslatef(translation[0], translation[1], translation[2]);

        rotate(gl, xRotation, yRotation, zRotation, 0f, -0.8f, 0f);

        gl.glScalef(8f, 8f, 8f);

        if (model != null) {
            model.draw(glAutoDrawable, vertexNormals);
        }
        gl.glFlush();
    }

    // Inner method for reshape, is called when the window is resized and once each frame in order to handle changes in perspective
    private void _reshape(GLAutoDrawable glAutoDrawable, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        if (height <= 0) { height = 1; }                // Without this the program crashes when
        final float h=(float)width/(float)height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (perspective) {
            glu.gluPerspective(45.0*zoomFactor, h, 0.1, 100.0);
        } else {
            gl.glOrtho(-1*zoomFactor*h,1*zoomFactor*h,-1*zoomFactor,1*zoomFactor,-5,5);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int xStart, int yStart, int width, int height) {
        _reshape(glAutoDrawable, width, height);
    }

    // Helper method for rotation
    private void rotate(GL2 gl, float xRotation, float yRotation, float zRotation, float xOffset, float yOffset, float zOffset) {
        gl.glTranslatef(-xOffset, -yOffset, -zOffset);
        gl.glRotatef(xRotation, 1f, 0f, 0f);
        gl.glRotatef(yRotation, 0f, 1f, 0f);
        gl.glRotatef(zRotation, 0f, 0f, 1f);
        gl.glTranslatef(xOffset, yOffset, zOffset);
    }

    @Override
    public void keyTyped(KeyEvent e) { }


    // Handles keys being pressed to rotate and zoom and apply modifiers
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
                xRotation += rotationFactor;
                break;
            case KeyEvent.VK_W:
                xRotation -= rotationFactor;
                break;
            case KeyEvent.VK_Z:
                zRotation += rotationFactor;
                break;
            case KeyEvent.VK_E:
                zRotation -= rotationFactor;
                break;
            case KeyEvent.VK_ADD:
            case KeyEvent.VK_EQUALS:
                if (zoomFactor > 0.1f) {
                    zoomFactor -= 0.1f;
                }
                break;
            case KeyEvent.VK_SUBTRACT:
            case KeyEvent.VK_MINUS:
                if (zoomFactor < 3.9f) {
                    zoomFactor += 0.1f;
                }
                break;
            case KeyEvent.VK_SHIFT:
                shiftPressed = true;
                break;
            case KeyEvent.VK_CONTROL:
                ctrlPressed = true;
                break;
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
    public void mouseClicked(MouseEvent e) { }

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
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    public void setMaterial(String material) {
        this.currentMaterial = Material.materials.get(material);
    }

    public void setPerspective(boolean perspective) {
        this.perspective = perspective;
    }

    public void setSurfaceRendering(boolean surfaceRendering) {
        this.surfaceRendering = surfaceRendering;
    }

    public void setVertexNormals(boolean vertexNormals) {
        this.vertexNormals = vertexNormals;
    }

    // Toggles a light
    public void toggleLight(int lightNum) {
        switch(lightNum) {
            case 1:
                infrontLight = !infrontLight;
                break;
            case 2:
                behindLight = !behindLight;
                break;
            case 3:
                aboveLight = !aboveLight;
                break;
        }
    }
}
