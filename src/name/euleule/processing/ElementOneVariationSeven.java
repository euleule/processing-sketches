package name.euleule.processing;

import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * - Create a number of One, random in size, limited to the parameters for their maximum and minimum diameter.
 * <p>
 * - Distribute the elements evenly on the canvas.
 * <p>
 * - Set a random color.
 * <p>
 * - If two elements intersect each other, start drawing a line.
 * <p>
 * - Increase lightness if the elements distance grows. Decrease lightness if the elements distance decreases.
 * <p>
 * - When an element reaches the border of the canvas, remove the element.
 * <p>
 * - Repeat until desired number of iterations is reached.
 */
public class ElementOneVariationSeven extends PApplet {

    // Number of elements used for rendering
    final int NUM_OBJECTS = 600;
    // Minimum size of elements
    final int D_MIN = 25;
    // Maximum size of elements
    final int D_MAX = 50;

    // Border width in px
    final int BORDER = 50;
    // Number of iterations
    final int MAX_ITERATIONS = 1;

    final int SIZE = 1700;

    List<One> objects;
    List<List<One>> groups;
    PVector color;
    int iterations;

    @Override
    public void settings() {
        size(SIZE, SIZE);
    }

    /**
     * Set up scene.
     */
    @Override
    public void setup() {
        background(255);
        colorMode(HSB);
        strokeWeight(2);
        iterations = 0;
        reset();
    }

    /**
     * Re-initialize the elements.
     */
    protected void reset() {
        objects = new ArrayList<>();
        groups = new ArrayList<>();
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
    @Override
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

        if (objects.size() == 0) {
            noLoop();
            save("/Users/robert/Desktop/Sketches/" + System.currentTimeMillis() + ".jpg");
            exit();
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

    /**
     * Check if any elements are out of screen. If they are, remove them from the scene.
     */
    protected void checkOutOfScreen() {
        // check for items out of screen
        List<One> remove = new ArrayList<>();
        for (One o : objects) {
            if (isOutOfScreen(o)) {
                remove.add(o);
            }
        }

        // get groups for elements out of screen
        List<List<One>> removeGroup = new ArrayList<>();
        for (One o : remove) {
            for (List<One> group : groups) {
                if (group.get(0).equals(o) || group.get(1).equals(o)) {
                    removeGroup.add(group);
                }
            }
            objects.remove(o);
        }

        groups.removeAll(removeGroup);
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

    /**
     * Check all elements for intersections.
     */
    protected void checkIntersections() {
        List<One> list = new ArrayList<>();
        list.addAll(objects);

        for (One o : objects) {
            for (One l : list) {
                if (o.equals(l)) {
                    continue;
                }

                if (o.intersect(l)) {
                    addGroup(o, l);
                }
            }
        }
    }

    /**
     * Add a group of two intersecting elements.
     *
     * @param o1 One
     * @param o2 One
     */
    protected void addGroup(One o1, One o2) {
        // check if there a group for the elements exists
        for (List<One> group : groups) {
            if (group.get(0).equals(o1) && group.get(1).equals(o2) || group.get(0).equals(o2) && group.get(1).equals(o1)) {
                // a group exists, do nothing
                return;
            }
        }

        // create new group
        List<One> group = new ArrayList<>();
        group.add(o1);
        group.add(o2);
        groups.add(group);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationSeven"});
    }
}
