package name.euleule.processing;

import processing.core.PApplet;

/**
 * Create a picture out of randomizes lines and rectangles. Each click adds a new layer to the piece, while the older
 * ones slowly fade out.
 * <p/>
 * Controls:
 *  click    add a new layer
 *  s        save the current image as jpg
 */
public class RectOne extends CustomPApplet {

    public void setup() {
        size(900, 900);
        background(255);
        noLoop();
    }

    public void draw() {
        // fade out previous layers
        noStroke();
        fill(255, 64);
        rect(0, 0, 900, 900);

        // add randomized elements
        for (int i = 0; i < 10; i++) {
            if (random(0, 1) > 0.5) {
                randomLine();
            } else {
                randomRect();
            }
        }
    }

    private void randomLine() {
        strokeWeight(3);
        stroke(getRandomGrey());
        line(rndX(), rndY(), rndX(), rndY());
    }

    private void randomRect() {
        noStroke();
        fill(getRandomGrey());
        float x = rndX();
        float y = rndY();
        rect(x, y, rndX() - x, rndY() - y);
    }

    private float rndX() {
        return random(border, width - border);
    }

    /**
     * Get a random point on the y axis, adjusted to image proportion.
     */
    private float rndY() {
        return random(border + 150, height - border - 150);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.RectOne"});
    }
}
