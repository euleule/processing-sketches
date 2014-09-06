package name.euleule.processing;

import processing.core.PApplet;

public class RandomSquares extends CustomPApplet {

    public void setup() {
        size(900, 900);
        background(255);
        noLoop();
    }

    public void draw() {
        noStroke();
        fill(255, 128);
        rect(0,0,900,900);

        for (int i = 0; i < 5; i++) {
            if (random(0, 1) > 0.4) {
                line();
            } else {
                rect();
            }
        }
    }

    private void line() {
        strokeWeight(3);
        for (int i = 0; i < 10; i++) {
            stroke(getLineColor());
            line(rndX(), rndY(), rndX(), rndY());
        }
    }

    private void rect() {
        noStroke();
        for (int i = 0; i < 10; i++) {
            fill(getLineColor());
            float x = rndX();
            float y = rndY();
            rect(x, y, rndX() - x, rndY() - y);
        }
    }

    private float rndX() {
        return random(border, width - border);
    }

    private float rndY() {
        return random(border + 150, height - border - 150);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.RandomSquares"});
    }
}
