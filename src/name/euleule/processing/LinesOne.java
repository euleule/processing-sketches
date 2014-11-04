package name.euleule.processing;

import processing.core.PApplet;

/**
 * Create a picture of lines drawn between points on the adjacent sides of a rectangle.
 * <p/>
 * Controls:
 * mouse    adjust number of points on the axis
 * s        save the current image as jpg
 * r        change line color
 */
public class LinesOne extends CustomPApplet {

    int pointsX = 2;
    int pointsY = 2;

    public void setup() {
        size(900, 900);
        stroke(getRandomGrey());
        strokeWeight(2);
    }

    public void draw() {
        background(255);

        pointsX = max(mouseX / 30, 2);
        pointsY = max(mouseY / 30, 2);

        if (max(pointsX, pointsY) > 7) {
            strokeWeight(1);
        } else {
            strokeWeight(2);
        }

        for (int i = 0; i < pointsX; i++) {
            float src = getPointX(i);

            for (int j = 0; j < pointsX; j++) {
                float dest = getPointX(j);
                line(src + border, 0 + border, dest + border, width - border);
            }
        }

        for (int i = 0; i < pointsY; i++) {
            float src = getPointY(i);

            for (int j = 0; j < pointsY; j++) {
                float dest = getPointY(j);
                line(0 + border, src + border, height - border, dest + border);
            }

        }
    }

    private float getPointX(int n) {
        return (height - 2 * border) / (pointsX - 1) * n;
    }

    private float getPointY(int n) {
        return (width - 2 * border) / (pointsY - 1) * n;
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.LinesOne"});
    }
}



