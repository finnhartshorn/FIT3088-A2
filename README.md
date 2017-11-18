# FIT3088-A2

## Computer Graphics Assignment 2:
Running the program requires a working JOGL installation, Swing has been used for the gui components so the program should work on any jogl supported platform, however only windows as tested.
The main class is SwingGLCanvas.class

### Features
  - If there is a file called "bun_zipper.ply" in the working directory, it will be loaded automatically.
  - Both obj and ply files can be loaded via File-Open, models other than "bun_zipper.ply" may appear off screen or at strange scales
  - Supports any correct ply file, all standard and user defined elements and properties will be parsed, however elements not required for a model are then lost as they are not needed.
  - The model material can be changed to gold, copper or white plastic. The default is a bland white
  - Normals can be changed to per-vertex or per-face
  - Wireframe or surface rendering can be toggled
  - There are lights positioned behind, above and in-front that can be toggled, default is just in-front light enabled
  - Projection can be changed between perspective and orthographic
  - All the above options are triggered via the "Render" dropdown menu
  - Rotation can be done in the X direction ("A" and "D" keys), in the Y direction ("X" and "W" keys) and in the Z direction ("Z" and "E" keys)
  - Rotation can also be done via the mouse whilst holding down the left mouse button.
  - Translation in the X and Y direction is done by holding the left mouse button and shift, the model will move with the mouse.
  - Translation in the Z direction is done via holding ctrl and moving the mouse up or down
  
  ### Some Screenshots
  
  #### Basic Render
  ![Imgur](https://i.imgur.com/xBfkVJS.png)
  #### WireFrame and Orthographic Projection
  ![Imgur](https://i.imgur.com/5JZreqB.png)
  #### Gold material, perspective projection and Lite from above
  ![Imgur](https://i.imgur.com/cpXwxco.png)
  #### Bronze material and per-face normals
  ![Imgur](https://i.imgur.com/gRXlXK8.png)
 
