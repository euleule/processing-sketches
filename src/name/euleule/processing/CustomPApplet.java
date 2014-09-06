package name.euleule.processing;

import processing.core.PApplet;

public class CustomPApplet extends PApplet {
    float border = 80;
    String savePath = "/Users/robert/Desktop/Sketches/";

    protected float getRandomGrey() {
        int high = 232;
        int low = 32;
        return (random(low, high) + random(low, high)) / 2;
    }

    public void keyPressed() {
        if (key == 's') {
            System.out.println("Saving ...");
            save(savePath + System.currentTimeMillis() + ".jpg");
        } else if (key == 'r') {
            stroke(getRandomGrey());
        }
    }

    public void mousePressed() {
        redraw();
    }
}
