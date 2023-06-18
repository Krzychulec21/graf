package CGlab;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    String version = "0.02";

    public static void main(String[] args) throws IOException {



        Renderer mainRenderer = new Renderer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
//                Integer.parseInt(args[2]), args[3]);
//        Renderer mainRenderer = new Renderer(args[0], Integer.parseInt(args[1]),
//                Integer.parseInt(args[2]));
//        mainRenderer.clear();
//        mainRenderer.drawPoint(100, 100);
        //RandomColorRenderer mainRenderer = new RandomColorRenderer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        mainRenderer.drawLineBresenhamInt(20,20,150,340);
       // RandomColorRenderer mainRenderer = new RandomColorRenderer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        try {
//            Model model = new Model();
//            model.readOBJ("deer.obj");
//            mainRenderer.render(model);
            mainRenderer.save();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getVersion() {
	return this.version;
    }
}
