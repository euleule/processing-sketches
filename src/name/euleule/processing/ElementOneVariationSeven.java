package name.euleule.processing;

import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
public class ElementOneVariationSeven extends ElementsApplet{

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
        float c = random(0, 359);

        for (int i = 0; i < NUM_OBJECTS; i++) {
            float d = random(D_MIN, D_MAX);
            float x = random(-width / 2 + BORDER + d / 2, width / 2 - BORDER - d / 2);
            float y = random(-height / 2 + BORDER + d / 2, height / 2 - BORDER - d / 2);

            float m = c;
            float k = random(0, 3);
            if (k > 2)
                m = (c + 30) % 360;
            if (k > 1)
                m = (c - 30) % 360;
            One one = new One(x, y, d, new PVector(m, 255, 128));

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
        for (List<One> group : groups) {
            One o1 = group.get(0);
            One o2 = group.get(1);
            PVector a = o1.getPos();
            PVector b = o2.getPos();

            float alpha = min((D_MIN + D_MAX) * 2 / a.dist(b), 9);

            PVector color = o1.getColor().copy();

            stroke(color.x, a.dist(b), color.z, alpha);
            line(a.x, a.y, b.x, b.y);
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

        for (List<One> group : groups) {
            PVector bari = group.get(0).getPos().copy();
            bari.sub(group.get(1).getPos());
            bari.normalize();
            bari.div(10);
            group.get(0).getPos().sub(bari);
        }

        checkIntersections();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationSeven"});
    }
}
