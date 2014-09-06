package name.euleule.processing;

import processing.core.PApplet;

public class CustomPApplet extends PApplet {
    float border = 80;
    String path = "/Users/robert/Desktop/Sketches/";

    protected float getLineColor() {
        int high = 232;
        int low = 32;
        return (random(low, high) + random(low, high)) / 2;
    }

    public void keyPressed() {
        if (key == 's') {
            System.out.println("Saving ...");
            save(path + System.currentTimeMillis() + ".jpg");
        } else if (key == 'r') {
            stroke(getLineColor());
        }
    }

    public void mousePressed(){
        redraw();
    }
}
