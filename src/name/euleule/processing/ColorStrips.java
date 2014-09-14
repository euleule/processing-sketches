package name.euleule.processing;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Create a picture of a colored strip.
 * <p/>
 * Controls:
 * mouse    adjust number of points on the axis
 * c        toggle multiple colors
 * f        toggle fade out
 * l        toggle lines
 * s        save the current image as jpg
 */
public class ColorStrips extends CustomPApplet {
    PVector background = null;
    boolean changeColor = false;
    boolean fade = true;
    boolean lines = true;

    public void setup() {
        size(900, 900);
        strokeWeight(3);
        noLoop();
    }

    public void draw() {

        if (lines) {
            stroke(getRandomGrey());
        } else {
            noStroke();
        }

        // set initial background
        rndBackgroundColor();
        background(background.x, background.y, background.z);

        // init vars
        float c1 = getRandomGrey();
        float c2 = getRandomGrey();
        float length = random(70, 150);

        // set first point
        PVector previous = new PVector(rndX(), rndY(length));

        for (int i = 0; i < 10; i++) {
            if (changeColor) {
                rndBackgroundColor();
            }

            for (int j = 0; j < 10; j++) {
                if (fade) {
                    fill(background.x, background.y, background.z, 16);
                    rect(0, 0, 900, 900);
                }

                PVector current = new PVector(rndX(), rndY(length));

                if (previous.x > current.x) {
                    fill(c1);
                } else {
                    fill(c2);
                }

                quad(previous.x, previous.y, current.x, current.y, current.x, current.y + length, previous.x, previous.y + length);
                previous = current;
            }
        }
    }

    public void keyPressed() {
        if (key == 'c') {
            changeColor = !changeColor;
            redraw();
        } else if (key == 'f') {
            fade = !fade;
            redraw();
        } else if (key == 'l') {
            lines = !lines;
            redraw();
        } else {
            super.keyPressed();
        }
    }

    private void rndBackgroundColor() {
        background = new PVector(random(0, 255), random(0, 255), random(0, 255));
    }

    private float rndX() {
        return random(border, width - border);
    }

    /**
     * Get a random point on the y axis, adjusted to image proportion.
     */
    private float rndY(float length) {
        return random(border, height - border - length);
    }


    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ColorStrips"});

    }
}
