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
    private JFileChooser fileChooser;

    public static void main(String[] args) { new SwingGLCanvas().setup(); }


    private void setup() {
        final GLProfile glProfile = GLProfile.get(GLProfile.GL2);                  // Use OpenGL2
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        final GLCanvas canvas = new GLCanvas(glCapabilities);
        renderer = new Renderer();

        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer);
        canvas.addMouseListener(renderer);

        canvas.setSize(1074, 768);

        final FPSAnimator animator = new FPSAnimator(canvas, 60, true);

        mainFrame = new JFrame("FIT3080 A2 - Finn Hartshorn 25939556");

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

    private void createMenus() {
        final JMenuBar menuBar = new JMenuBar();    // Create menu bar

        // Create menus
        JMenu fileMenu = new JMenu("File");
        JMenu renderMenu = new JMenu("Render");
        JMenu materialSubMenu = new JMenu("Material");
        JMenu projectionSubMenu = new JMenu("Projection");
        JMenu renderingSubMenu = new JMenu("Rendering");
        JMenu normalsSubMenu = new JMenu("Normals");

        // Create open menu items
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setActionCommand("Open");
        openMenuItem.setMnemonic(KeyEvent.VK_O);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setActionCommand("Exit");

        // Create render menu items
        JCheckBoxMenuItem lightToggle = new JCheckBoxMenuItem("Light", true);


        // Create material Submenu items
        JRadioButtonMenuItem goldMaterial = new JRadioButtonMenuItem("Gold", true);
        JRadioButtonMenuItem copperMaterial = new JRadioButtonMenuItem("Copper");
        JRadioButtonMenuItem whitePlasticMaterial = new JRadioButtonMenuItem("White Plastic");
        ButtonGroup materialGroup = new ButtonGroup();
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

        // Add this as a listener to all menu actions
        openMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);
        goldMaterial.addActionListener(this);
        copperMaterial.addActionListener(this);
        whitePlasticMaterial.addActionListener(this);
        perspetive.addActionListener(this);
        orthographic.addActionListener(this);
        surface.addActionListener(this);
        wireframe.addActionListener(this);
        vector.addActionListener(this);
        polygon.addActionListener(this);
        lightToggle.addActionListener(this);

        // Add items to their respective menus
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);

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

        renderMenu.add(lightToggle);

        // Add menus to the main menuBar
        menuBar.add(fileMenu);
        menuBar.add(renderMenu);

        mainFrame.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        switch (e.getActionCommand()) {
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
            case "Light":
                renderer.toggleLight();
                break;
            case "Open":
                int returnValue = fileChooser.showOpenDialog(mainFrame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    System.out.println(fileChooser.getCurrentDirectory());
                    System.out.println(file.getName());
                }

        }
    }
}
