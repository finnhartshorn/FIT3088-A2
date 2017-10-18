import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

public class SwingGLCanvas implements ActionListener {
    private JFrame mainFrame;
    private Renderer renderer;
    private FPSAnimator animator;
    private JFileChooser fileChooser;

    public static void main(String[] args) { new SwingGLCanvas().setup(); }


    private void setup() {
        final GLProfile glProfile = GLProfile.get(GLProfile.GL2);                  // Use OpenGL2
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        final GLCanvas canvas = new GLCanvas(glCapabilities);
        renderer = new Renderer();

        // Adds event listeners
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer);
        canvas.addMouseListener(renderer);

        canvas.setSize(1074, 768);

        animator = new FPSAnimator(canvas, 60, true);

        mainFrame = new JFrame("FIT3088 A2 - Finn Hartshorn 25939556");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainFrame.getContentPane().add(canvas);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PLY Files", "ply", "PLY"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("OBJ files", "obj", "OBJ"));

        // Kill
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if(animator.isStarted()) {
                    animator.stop();
                }
                System.exit(0);
            }
        });


        mainFrame.setSize(mainFrame.getContentPane().getPreferredSize());

        createMenus();

        mainFrame.setVisible(true);

        animator.start();
    }

    // Creates menus, and adds this class as a listener
    private void createMenus() {
        final JMenuBar menuBar = new JMenuBar();    // Create menu bar

        // Create menus
        JMenu fileMenu = new JMenu("File");
        JMenu renderMenu = new JMenu("Render");
        JMenu materialSubMenu = new JMenu("Material");
        JMenu projectionSubMenu = new JMenu("Projection");
        JMenu renderingSubMenu = new JMenu("Rendering");
        JMenu normalsSubMenu = new JMenu("Normals");
        JMenu lightsSubMenu = new JMenu("Lights");

        // Create open menu items
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setActionCommand("Open");
        openMenuItem.setMnemonic(KeyEvent.VK_O);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setActionCommand("Exit");

        // Create render menu items


        // Create material Submenu items
        JRadioButtonMenuItem defaultMaterial = new JRadioButtonMenuItem("Default", true);
        JRadioButtonMenuItem goldMaterial = new JRadioButtonMenuItem("Gold");
        JRadioButtonMenuItem copperMaterial = new JRadioButtonMenuItem("Copper");
        JRadioButtonMenuItem whitePlasticMaterial = new JRadioButtonMenuItem("White Plastic");
        ButtonGroup materialGroup = new ButtonGroup();
        materialGroup.add(defaultMaterial);
        materialGroup.add(goldMaterial);
        materialGroup.add(copperMaterial);
        materialGroup.add(whitePlasticMaterial);

        // Create projection submenu items
        JRadioButtonMenuItem perspetive = new JRadioButtonMenuItem("Perspective", true);
        JRadioButtonMenuItem orthographic = new JRadioButtonMenuItem("Orthographic");
        ButtonGroup projectionGroup = new ButtonGroup();
        projectionGroup.add(perspetive);
        projectionGroup.add(orthographic);

        // Create rendering submenu items
        JRadioButtonMenuItem surface = new JRadioButtonMenuItem("Surface", true);
        JRadioButtonMenuItem wireframe = new JRadioButtonMenuItem("Wireframe");
        ButtonGroup renderingGroup = new ButtonGroup();
        renderingGroup.add(surface);
        renderingGroup.add(wireframe);

        // Create normals submenu items
        JRadioButtonMenuItem vector = new JRadioButtonMenuItem("Per-Vector", true);
        JRadioButtonMenuItem polygon = new JRadioButtonMenuItem("Per-Face");
        ButtonGroup normalsGroup = new ButtonGroup();
        normalsGroup.add(vector);
        normalsGroup.add(polygon);

        // Create lights submenu items
        JCheckBoxMenuItem frontLightToggle = new JCheckBoxMenuItem("Front Light", true);
        JCheckBoxMenuItem behindLightToggle = new JCheckBoxMenuItem("Behind Light", false);
        JCheckBoxMenuItem aboveLightToggle = new JCheckBoxMenuItem("Above Light", false);

        // Add this as a listener to all menu actions
        openMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);
        defaultMaterial.addActionListener(this);
        goldMaterial.addActionListener(this);
        copperMaterial.addActionListener(this);
        whitePlasticMaterial.addActionListener(this);
        perspetive.addActionListener(this);
        orthographic.addActionListener(this);
        surface.addActionListener(this);
        wireframe.addActionListener(this);
        vector.addActionListener(this);
        polygon.addActionListener(this);
        frontLightToggle.addActionListener(this);
        behindLightToggle.addActionListener(this);
        aboveLightToggle.addActionListener(this);

        // Add items to their respective menus
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);

        materialSubMenu.add(defaultMaterial);
        materialSubMenu.add(goldMaterial);
        materialSubMenu.add(copperMaterial);
        materialSubMenu.add(whitePlasticMaterial);
        renderMenu.add(materialSubMenu);

        projectionSubMenu.add(perspetive);
        projectionSubMenu.add(orthographic);
        renderMenu.add(projectionSubMenu);

        renderingSubMenu.add(surface);
        renderingSubMenu.add(wireframe);
        renderMenu.add(renderingSubMenu);

        normalsSubMenu.add(vector);
        normalsSubMenu.add(polygon);
        renderMenu.add(normalsSubMenu);

        lightsSubMenu.add(aboveLightToggle);
        lightsSubMenu.add(frontLightToggle);
        lightsSubMenu.add(behindLightToggle);
        renderMenu.add(lightsSubMenu);

        // Add menus to the main menuBar
        menuBar.add(fileMenu);
        menuBar.add(renderMenu);

        // Set the menubar
        mainFrame.setJMenuBar(menuBar);
    }


    // Handles actions and calls the required methods of renderer
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Default":
            case "Gold":
            case "Copper":
            case "White Plastic":
                renderer.setMaterial(e.getActionCommand());
                break;
            case "Perspective":
                renderer.setPerspective(true);
                break;
            case "Orthographic":
                renderer.setPerspective(false);
                break;
            case "Surface":
                renderer.setSurfaceRendering(true);
                break;
            case "Wireframe":
                renderer.setSurfaceRendering(false);
                break;
            case "Per-Vector":
                renderer.setVertexNormals(true);
                break;
            case "Per-Face":
                renderer.setVertexNormals(false);
                break;
            case "Front Light":
                renderer.toggleLight(1);
                break;
            case "Behind Light":
                renderer.toggleLight(2);
                break;
            case "Above Light":
                renderer.toggleLight(3);
                break;
            case "Open":
                int returnValue = fileChooser.showOpenDialog(mainFrame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    renderer.loadFile(file);
                }
                break;
            case "Exit":
                mainFrame.dispose();
                if(animator.isStarted()) {
                    animator.stop();
                }
                System.exit(0);
        }
    }
}
