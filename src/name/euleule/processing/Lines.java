package name.euleule.processing;

import processing.core.PApplet;

public class Lines extends CustomPApplet {

    int pointsX = 1;
    int pointsY = 1;

    public void setup() {
        size(900, 900);
        stroke(getLineColor());
        strokeWeight(2);
    }

    public void draw() {
        background(255);

        pointsX = max(mouseX / 30, 1);
        pointsY = max(mouseY / 30, 1);

        if (max(pointsX, pointsY) > 7) {
            strokeWeight(1);
        } else {
            strokeWeight(2);
        }

        for (int i = 0; i < pointsX + 1; i++) {
            float srcX = (height - 2 * border) / pointsX * i;

            for (int j = 0; j < pointsX + 1; j++) {
                float destX = (height - 2 * border) / pointsX * j;
                line(srcX + border, 0 + border, destX + border, width - border);
            }
        }

        for (int i = 0; i < pointsY + 1; i++) {
            float srcY = (width - 2 * border) / pointsY * i;

            for (int j = 0; j < pointsY + 1; j++) {

                float destY = (width - 2 * border) / pointsY * j;
                line(0 + border, srcY + border, height - border, destY + border);
            }

        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.Lines"});
    }
}



