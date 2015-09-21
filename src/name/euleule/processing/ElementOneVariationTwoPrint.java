package name.euleule.processing;

import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

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
public class ElementOneVariationTwoPrint extends PApplet {

    // Number of elements used for rendering
    final int NUM_OBJECTS = 30;
    // Minimum size of elements
    final int D_MIN = 100;
    // Maximum size of elements
    final int D_MAX = 200;
    // Border width in px
    final int BORDER = 100;
    // Number of iterations
    final int MAX_ITERATIONS = 6;

    final int screenResolution = 900;
    final int printResolution = 12000;
    float scale = (float) printResolution / screenResolution;
    List<One> objects;
    List<List<One>> groups;
    PVector color;
    int iterations = 0;
    String fileName = "";

    PGraphics pgPrint = null;

    /**
     * Set up scene.
     */
    public void setup() {
        size(screenResolution, screenResolution, P2D);
        fileName = "/Users/robert/Desktop/Print/1_2_" + System.currentTimeMillis();

        background(255);

        pgPrint = createGraphics(printResolution, printResolution);
        pgPrint.beginDraw();
        pgPrint.background(255);

        color = PVector.random3D();

        strokeWeight(1);
        pgPrint.strokeWeight(scale);
        pgPrint.translate(printResolution / 2, printResolution / 2);
        reset();
    }

    /**
     * Re-initialize the elements.
     */
    private void reset() {
        objects = new ArrayList<One>();
        groups = new ArrayList<List<One>>();

        for (int i = 0; i < NUM_OBJECTS; i++) {
            float d = random(D_MIN, D_MAX);
            float x = random(-width / 2 + BORDER, width / 2 - BORDER);
            float y = random(-height / 2 + BORDER, height / 2 - BORDER);
            One one = new One(x, y, d, color);
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

            float alpha = min((float) ((D_MIN + D_MAX) * 2) / a.dist(b), 9);

            PVector color = o1.getColor().get();
            color.mult(a.dist(b) * iterations / MAX_ITERATIONS);

            stroke(color.x, color.y, color.z, alpha);
            line(a.x, a.y, b.x, b.y);

//            color.mult(1.2f);
            pgPrint.stroke(color.x, color.y, color.z, alpha);
            pgPrint.line(a.x * scale, a.y * scale, b.x * scale, b.y * scale);
        }
    }

    /**
     * Update the element in the scene.
     */
    private void update() {
        for (One o : objects) {
            o.update();
        }
        checkIntersections();

        checkOutOfScreen();

        if (iterations > MAX_ITERATIONS) {
            save(fileName + ".jpg");

            pgPrint.endDraw();
            pgPrint.save(fileName + "_highes.jpg");
            exit();
        }

        if (objects.size() == 0) {
            reset();
        }

        // save each frame
//        if(folder != null) {
//            saveFrame(folder + "/frames/frame-######.png");
//        }
    }

    /**
     * Check if any elements are out of screen. If they are, remove them from the scene.
     */
    private void checkOutOfScreen() {
        // check for items out of screen
        List<One> remove = new ArrayList<One>();
        for (One o : objects) {
            if (isOutOfScreen(o)) {
                remove.add(o);
            }
        }

        // get groups for elements out of screen
        List<List<One>> removeGroup = new ArrayList<List<One>>();
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
    private boolean isOutOfScreen(One one) {
        return (one.getPos().dist(new PVector(0, 0)) + BORDER > width / 2);
    }

    /**
     * Check all elements for intersections.
     */
    private void checkIntersections() {
        List<One> list = new ArrayList<One>();
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
    private void addGroup(One o1, One o2) {
        // check if there a group for the elements exists
        for (List<One> group : groups) {
            if (group.get(0).equals(o1) && group.get(1).equals(o2) || group.get(0).equals(o2) && group.get(1).equals(o1)) {
                // a group exists, do nothing
                return;
            }
        }

        // create new group
        List<One> group = new ArrayList<One>();
        group.add(o1);
        group.add(o2);
        groups.add(group);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationTwoPrint"});
    }
}
