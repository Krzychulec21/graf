package CGlab;

import java.util.concurrent.ThreadLocalRandom;

public class RandomColorRenderer extends Renderer {

    public RandomColorRenderer(String filename) {
        super(filename);
    }

    public RandomColorRenderer(String filename, int w, int h) {
        super(filename, w, h);
    }

    public void render(Model model) {
        for (Vec3i face : model.getFaceList()) {

            Vec2i[] screen_coords = new Vec2i[3];
            Vec3f[] world_coords = new Vec3f[3];

            world_coords[0] = model.getVertex(face.x);
            world_coords[1] = model.getVertex(face.y);
            world_coords[2] = model.getVertex(face.z);

            for (int j = 0; j < 3; j++) {
                screen_coords[j] = new Vec2i((int) ((world_coords[j].x + 1.0) * render.getWidth() / 2.0),
                        (int) ((world_coords[j].y + 1.0) * render.getHeight() / 2.0) - render.getHeight() / 2);
            }

            int randColor = ThreadLocalRandom.current().nextInt(0, 0x00ffffff) | 0xff000000;
            drawTriangle(screen_coords[0], screen_coords[1], screen_coords[2], randColor);
        }
    }
}

///
public void drawtriangle(Vec31 A, Vec31 B, Vec31 C, int color) {
float minx = Integer. MAX_VALUE, maxX = Integer. MIN_VALUE, minY = Integer. MAX_VALUE, maxY = Integer.MIN_VALUE;
if (A.x > maxX) maxx = A. x;
if (A.x < minX) minx = A.X;
if (A.y > maxY) maxY = A. y;
if (A.y < minY) minY = A.y;
if (B.x > maxX) maxx = B.X;
if (B.x < minX) minx = B.x;
if (B.y > maxY) maxY = B.Y;
if (B.y < minY) minY = B.y;
if (C.x > maxX) maxx = C.x;
if (C.x < minX) minx = C.x;
if (C.y > maxY) maxY = C.y;
if (C.y < minY) minY = C.y;
for (int i=(int)minx; i<maxx; i++) {
for (int ] = (int) minY; i < maxY; i++) {
Vec2f P = new Vec2f (1, 1;
Vec3f vector = barycentric(A, B, C, P);
if (vector.x >= 0 && vector.y >= 0 && vector. >= 0 && vector.x <= 1 && vector.y <= 1 && vector.z <= 1) K
double zbuffor = A.z * vector.× + B. * vector.y + C.z * vector. z;
if (buffor < bufforTable[i][i]) {
zbufforTable[i][i] = zbuffor;
render.setRGB(1, 1, color);}}}
