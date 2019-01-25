package name.euleule.processing;

import processing.core.PApplet;

public class Dots extends PApplet {

    @Override
    public void settings() {
        size(1750, 1200);
    }
//    public void settings(){
//        size(3508, 2480);
//    }

    /**
     * Set up scene.
     */
    @Override
    public void setup() {
        background(255);
        strokeWeight(1);
        noiseSeed(System.currentTimeMillis());
    }

    /**
     * Draw scene.
     */
    @Override
    public void draw() {

        noiseDetail(100, 0.5f);
        int noiseXRange = 15;
        int noiseYRange = 15;

        loadPixels();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float noiseX = map(x, 0, width, 0, noiseXRange);
                float noiseY = map(y, 0, height, 0, noiseYRange);
                float n =noise(noiseX, noiseY) * 255;
                pixels[x + y * width] = color(n);
            }
        }
        updatePixels();
        save("/Users/robert/Desktop/Sketches/" + System.currentTimeMillis() + ".jpg");
        exit();
    }


    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.Dots"});
    }
}
