package name.euleule.processing;

import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * - Create a number of One, random in size, limited to the parameters for their maximum and minimum diameter.
 * <p/>
 * - Distribute the elements evenly on the canvas.
 * <p/>
 * - Set a random color.
 * <p/>
 * - If two elements intersect each other, start drawing a line.
 * <p/>
 * - Increase lightness if the elements distance grows. Decrease lightness if the elements distance decreases.
 * <p/>
 * - When an element reaches the border of the canvas, remove the element.
 * <p/>
 * - Repeat until desired number of iterations is reached.
 */
public class ElementOneVariationEight extends ElementsApplet {

    // Number of elements used for rendering
    final int NUM_OBJECTS = 600;
    // Minimum size of elements
    final int D_MIN = 150;
    // Maximum size of elements
    final int D_MAX = 300;

    /**
     * Re-initialize the elements.
     */
    protected void reset() {
        objects = new ArrayList<One>();
        groups = new ArrayList<List<One>>();
        PVector direction = new PVector(0, 1);
        float c = random(360);

        for (int i = 0; i < NUM_OBJECTS; i++) {
            float d = random(D_MIN, D_MAX);
            float x = random(-width / 2 + BORDER + d / 2, width / 2 - BORDER - d / 2 - D_MAX);
            float y = random(-height / 2 + BORDER + d / 2, height / 2 - BORDER - d / 2);

            float m = c + (random(2) * 30);
            One one = new One(x, y, d, new PVector(m, 255, 180));
            one.setSize(random(D_MIN, D_MAX) * 4);

            one.setDirection(direction);
            objects.add(one);
        }
        iterations++;
    }

    /**
     * Draw scene.
     */
    public void draw() {
        update();
        translate(width / 2, height / 2);
        for (One one : objects) {

            float alpha = min(D_MAX * 15 / one.getStart().dist(one.getPos()), 50);

            PVector color = one.getColor().get();

            stroke(color.x, 255 * one.getSize() / one.getStart().dist(one.getPos()), color.z, alpha);
//            stroke(color.x, color.y, color.z, alpha);
            line(one.getPos().x, one.getPos().y, one.getPos().x + one.getDiameter(), one.getPos().y);
        }
    }

    /**
     * Update the element in the scene.
     */
    protected void update() {
        checkOutOfScreen();

        if (iterations > MAX_ITERATIONS) {
            exit();
        }

        if (objects.size() == 0) {
            if (folder != null) {
                save(folder + System.currentTimeMillis() + ".png");
            }
            reset();
        }

        for (One o : objects) {
            o.update();
        }


        Set<One> remove = new HashSet<One>();
        for (One one : objects) {
            if (isOutOfScreen(one)) {
                remove.add(one);
                continue;
            }

            if (one.getStart().y + one.getSize() < one.getPos().y || one.getPos().y > height / 2 - BORDER || one.getStart().y + one.getSize() / 2 + BORDER > height / 2) {
                remove.add(one);
            }
        }
        objects.removeAll(remove);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationEight"});
    }

}
