package name.euleule.processing;

import processing.core.PApplet;

public class P201603181 extends PApplet {
    String savePath = "/Users/robert/Desktop/Sketches/";

    final int SIZE = 3000;

    @Override
    public void settings() {
        size(SIZE, SIZE);
        int d = displayDensity();
        pixelDensity(d);
    }

    @Override
    public void keyPressed() {
        if (key == 's') {
            System.out.println("Saving ...");
            save(savePath + System.currentTimeMillis() + ".jpg");
        } else if (key == 'r') {
            background(255);
            redraw();
        }
    }

    /**
     * Set up scene.
     */
    @Override
    public void setup() {
        background(255);
        noLoop();
    }

    /**
     * Draw scene.
     */
    @Override
    public void draw() {
        float color = 0;
        int numberOfLines = (SIZE / 10) - 10;
        System.out.println(numberOfLines);
        float colorStep = numberOfLines / 255.f;
        System.out.println(colorStep);

        for (int n = 0; n < numberOfLines; n++) {
            fill(color);
            float max = 0;
            beginShape();
            for (int i = 0; i <= SIZE; i += 50) {
                float k = 25 + n * 10 + noise(0.01f * i, 0.1f * n) * 100;
                max = max(k, max);
                curveVertex(i, 25 + n * 10 + noise(0.01f * i, 0.1f * n) * 100);
            }
            curveVertex(SIZE, max + 50);
            curveVertex(50, max + 50);
            curveVertex(50, 25 + n * 10 + noise(0.01f * 1, 0.1f * n) * 100);
            endShape();
            color += colorStep;
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.P201603181"});
    }
}
