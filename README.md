# Java3dRenderer
A 3d renderer with texturing and depth buffer. I used i to test generating a very rough infinite terrain.

It can render textures on to the triangle. When the camera goes through a triangle it splits it into 2 triangles generating a quad.
You can make objects and shift and rotate them and copy them as needed. The renderer runs on a seperate thread but only on one thread. 
Since this is all running on the cpu it is reccomended to run in a low resolution.

