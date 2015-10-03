package name.euleule.processing;

import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * - Create a number of One, random in size, limited to the parameters for their maximum and minimum diameter.
 * - Distribute the elements evenly on the canvas.
 * - Set a random color.
 * - If two elements intersect each other, start drawing a line.
 * - Increase lightness if the elements distance grows. Decrease lightness if the elements distance decreases.
 * - When an element reaches the border of the canvas, remove the element.
 * - Repeat until desired number of iterations is reached.
 */
public class ElementOneVariationNine extends PApplet {

    // Number of elements use for rendering
    final int NUM_OBJECTS = 200;
    // Minimum size of elements
    final int D_MIN = 25;
    // Maximum size of elements
    final int D_MAX = 50;
    final int BORDER = 50;

    final int SIZE = 1000;
    final int MAX_ITERATIONS = 2;
    int direction = -1;

    List<One> objects = new ArrayList<>();
    PVector color;
    int iterations;

    String savePath = "/Users/robert/Desktop/Sketches/";

    @Override
    public void settings() {
        size(SIZE, SIZE);
    }

    /**
     * Set up scene.
     */
    @Override
    public void setup() {
        frameRate(120);
        background(255);
        colorMode(HSB);
        strokeWeight(1);
        iterations = 0;
    }

    public void keyPressed() {
        if (key == 's') {
            System.out.println("Saving ...");
            save(savePath + System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * Re-initialize the elements.
     */
    void reset() {
        objects.clear();
        direction *= -1;

        PVector direction = new PVector(0, this.direction);
        float c = random(0, 359);

        for (int i = 0; i < NUM_OBJECTS; i++) {
            float d = random(D_MIN, D_MAX);
            float x = random(-width / 2 + BORDER + d / 2, width / 2 - BORDER - d / 2);
            float y = random(-height / 2 + BORDER + d / 2, height / 2 - BORDER - d / 2);

            float m = c + random(0, 60);

            One one = new One(x, y, d, new PVector(m, 255, 128));

            one.setDirection(direction);
            objects.add(one);
        }
        iterations++;
    }

    /**
     * Draw scene.
     */
    @Override
    public void draw() {
        update();
        translate(width / 2, height / 2);
        for (One o1 : objects) {
            for (One o2 : objects) {
                if (o1.intersect(o2)) {
                    PVector a = o1.getPos();
                    PVector b = o2.getPos();

                    float alpha = map(a.dist(b), 0, D_MAX * 2, 0, 100);

                    PVector color = o1.getColor().copy();

                    float saturation = map(a.dist(b), 0, D_MAX * 2, 0, 128);
                    stroke(color.x, saturation, color.z, alpha);

                    line(a.x, a.y, b.x, b.y);
                }
            }
        }
    }

    /**
     * Update the element in the scene.
     */
    protected void update() {
        checkOutOfScreen();

        if (iterations > MAX_ITERATIONS) {
            background(255);
            iterations = 0;
            reset();
        }

        if (objects.size() == 0) {
            save(savePath + System.currentTimeMillis() + ".png");
            reset();
        }

        objects.forEach(One::update);
    }

    /**
     * Check if any elements are out of screen. If they are, remove them from the scene.
     */
    protected void checkOutOfScreen() {
        // check for items out of screen
        List<One> remove = objects.stream().filter(this::isOutOfScreen).collect(Collectors.toList());

        remove.forEach(objects::remove);
    }

    /**
     * Check if the element is out of screen.
     *
     * @param one Element that needs to be checked.
     * @return boolean
     */
    protected boolean isOutOfScreen(One one) {
        return Math.abs(one.getPos().x) + BORDER > width / 2 || Math.abs(one.getPos().y) + BORDER > height / 2;
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationNine"});
    }
}
