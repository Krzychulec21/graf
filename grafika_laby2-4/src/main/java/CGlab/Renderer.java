package CGlab;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Renderer {

    public enum LineAlgo {NAIVE, BRESENHAM, BRESENHAM_INT;}

    public BufferedImage render;
    public final int h = 200;
    public final int w = 200;

    private String filename;
    private LineAlgo lineAlgo = LineAlgo.NAIVE;

    public Renderer(String filename) {
        this.render = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
    }

    public Renderer(String path, int w, int h) {
        this.filename = path;
        this.render = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

//        drawTriangle(new Vec2f(0, 0), new Vec2f(100, 50), new Vec2f(70, 90));
//        drawTriangle(new Vec2f(10.0F, 10.0F), new Vec2f(400.0F, 470.0F), new Vec2f(230.0F, 530.0F));
    }

    public Renderer(String path, int w, int h, String algo) {
        switch (algo.toLowerCase()) {
            case "line_naive":
                this.lineAlgo = LineAlgo.NAIVE;
                break;
            case "bresenham":
                this.lineAlgo = LineAlgo.BRESENHAM;
                break;
            case "bresenham_int":
                this.lineAlgo = LineAlgo.BRESENHAM_INT;
                break;
        }
        this.filename = path;
        this.render = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.clear();
        drawLine(3, 5, 15, 0, this.lineAlgo);
    }

    public void drawPoint(int x, int y) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);
        render.setRGB(x, y, white);
    }

    public void drawPoint(int x, int y, int color) {
        render.setRGB(x, y, color);
    }

    public void drawLine(int x0, int y0, int x1, int y1, LineAlgo lineAlgo) {
        if (lineAlgo == LineAlgo.NAIVE) drawLineNaive(x0, y0, x1, y1);
        if (lineAlgo == LineAlgo.BRESENHAM) drawLineBresenham(x0, y0, x1, y1);
        if (lineAlgo == LineAlgo.BRESENHAM_INT) drawLineBresenhamInt(x0, y0, x1, y1);
    }

    public void drawLineNaive(int x0, int y0, int x1, int y1) {
        // przesuniecia x oraz y
        double dx = x1 - x0;
        double dy = y1 - y0;
        double m = dy / dx;  // wspolczynnik m jest to stusnek przesuniec wzdluż osi OY
        double y = y0;

        for (int i = x0; i < x1; i++) {
            drawPoint(i, (int) Math.round(y));
            y += m;
        }
    }

    public void drawLineBresenham(int x0, int y0, int x1, int y1) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1 - x0;
        int dy = y1 - y0;
        float m = Math.abs(dy / (float) (dx));
        float epsilon = 0;
        System.out.println("m = " + m);

        int y = y0;

        for (int x = x0; x <= x1; x++) {
            render.setRGB(x, y, white);
            epsilon += m;
            if (epsilon > 0.5) {
                y += (y1 > y0 ? 1 : -1);
                epsilon -= 1.;
            }
        } // Oktanty: 8, 7
    }

    public void drawLineBresenhamInt(int x0, int y0, int x1, int y1) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1 - x0;
        int dy = y1 - y0;
        int epsilon = 0;
        int m = Math.abs(2 * dy);
        int y = y0;

        for (int x = x0; x < x1; x++) {
            render.setRGB(x, y, white);
            epsilon += m;
            if (epsilon > dx) {
                y += (y1 > y0 ? 1 : -1);
                epsilon -= 2 * dx;
            }
        }
    }

    public void save() throws IOException {
        File outputfile = new File(filename);
        render = Renderer.verticalFlip(render);
        ImageIO.write(render, "png", outputfile);
    }

    public void clear() {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int black = (0) | (255 << 24);
                render.setRGB(x, y, black);
            }
        }
    }

    public static BufferedImage verticalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return flippedImage;
    }

    public Vec3f barycentric(Vec2f A, Vec2f B, Vec2f C, Vec2f P) {
        Vec3f v1 = new Vec3f((B.x - A.x), (C.x - A.x), (A.x - P.x));
        Vec3f v2 = new Vec3f((B.y - A.y), (C.y - A.y), (A.y - P.y));
        Vec3f cross = countCross(v1, v2);

        Vec2f uv = new Vec2f(cross.x / cross.z, cross.y / cross.z);

        return new Vec3f(uv.x, uv.y, 1 - uv.x - uv.y);  // wspolrzedne barycentryczne
    }

    public Vec3f barycentric(Vec2i A, Vec2i B, Vec2i C, Vec2i P) {
        Vec3f v1 = new Vec3f((B.x - A.x), (C.x - A.x), (A.x - P.x));
        Vec3f v2 = new Vec3f((B.y - A.y), (C.y - A.y), (A.y - P.y));
        Vec3f cross = countCross(v1, v2);

        Vec2f uv = new Vec2f(cross.x / cross.z, cross.y / cross.z);

        return new Vec3f(uv.x, uv.y, 1 - uv.x - uv.y);  // wspolrzedne barycentryczne
    }

    public void drawTriangle(Vec2f A, Vec2f B, Vec2f C) {
        int w = this.render.getWidth();
        int h = this.render.getHeight();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Vec2f P = new Vec2f(x, y);
                Vec3f bar = barycentric(A, B, C, P);
                if ((bar.x > 0 && bar.x < 1) &&
                        (bar.y > 0 && bar.y < 1) &&
                        (bar.z > 0 && bar.z < 1)) {
                    drawPoint(x, y);
                }
            }
        }
    }

    public void drawTriangle(Vec2i A, Vec2i B, Vec2i C, int color) {
        int w = this.render.getWidth();
        int h = this.render.getHeight();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Vec2i P = new Vec2i(x, y);
                Vec3f bar = barycentric(A, B, C, P);
                if ((bar.x > 0 && bar.x < 1) &&
                        (bar.y > 0 && bar.y < 1) &&
                        (bar.z > 0 && bar.z < 1)) {
                    drawPoint(x, y, color);
                }
            }
        }
    }

    private Vec3f countCross(Vec3f v1, Vec3f v2) {
        // v1.y*v2.z - v1.z*v2.y; v1.z*v2.x - v1.x*v2.z; v1.x*v2.y - v1.y*v2.x
        float x = (v1.y * v2.z) - (v1.z * v2.y);
        float y = (v1.z * v2.x) - (v1.x * v2.z);
        float z = (v1.x * v2.y) - (v1.y * v2.x);

        return new Vec3f(x, y, z);
    }

}
