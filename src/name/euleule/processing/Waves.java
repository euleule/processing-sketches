package name.euleule.processing;

import processing.core.PApplet;

public class Waves extends CustomPApplet {

    int cycles = 1;
    int waves = 20;

    int size = 900;

    boolean base = false;

    public void setup() {
        size(size, size);
        background(220);
        smooth();
        stroke(getLineColor());
        strokeWeight(2);
        noFill();
    }

    public void draw() {
        cycles = max(1, mouseX / 30);
        waves = max(1, mouseY / 20);

        if (waves > 2) {
            if (waves % 2 == 0) {
                ++waves;
            }
        }

        float w = (size - 2 * border) / cycles;
        float h = (size - 2 * border) / waves;
        background(200);

        translate(80, 80);

        if (!base && waves > 3) {
            translate(0, (waves - 1) * h / 4 * 2 / 3);
        }

        for (int i = 0; i < waves; i++) {
            for (int j = 0; j < cycles; j++) {
                if (base) {
                    drawCurve(h, w, (waves / 2) - abs(i - waves / 2));
                } else {
                    drawCurve(h, w, abs(i - waves / 2));
                }
                translate(w, 0);
            }

            if (base || waves < 4) {
                translate(-cycles * w, h);
            } else {
                translate(-cycles * w, h * 2 / 3);
            }
        }
    }

    private void drawCurve(float height, float width, float amp) {
        bezier(0, height / 2, width / 6, height / 2 - ((height / 2) * amp), width * 2 / 6, height / 2 - ((height / 2) * amp), width / 2, height / 2);
        bezier(width / 2, height / 2, width * 4 / 6, height / 2 + ((height / 2) * amp), width * 5 / 6, height / 2 + ((height / 2) * amp), width, height / 2);
    }

    public void keyPressed() {
        super.keyPressed();
        if (key == 'b') {
            base = !base;
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.Waves"});
    }
}
