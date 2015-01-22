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
public class ElementOneVariationThreePrint extends PApplet {

    // Set the path to the directory the images should be rendered to
    final String PATH = "/Users/robert/Desktop/Print/";
    // Set the targeted resolution of the high res image. Should be > 12k
    final int TARGET_RESOLUTION = 1600;

    // Number of elements used for rendering
    final int NUM_OBJECTS = 80;
    // Minimum size of elements
    final int D_MIN = 50;
    // Maximum size of elements
    final int D_MAX = 150;
    // Border width in px
    final int BORDER = 200;

    // Number of iterations
    final int MAX_ITERATIONS = 2;

    List<One> objects;
    List<List<One>> groups;
    PVector color;
    int iterations = 0;
    private String fileName;
    private PGraphics pgPrint;

    final int screenResolution = 1600;
    final int printResolution = TARGET_RESOLUTION;
    float scale = (float) printResolution / screenResolution;

    /**
     * Set up scene.
     */
    public void setup() {
        size(screenResolution, screenResolution, P2D);

        background(255);
        pgPrint = createGraphics(printResolution, printResolution, P2D);
        pgPrint.beginDraw();
        pgPrint.background(255);
        pgPrint.strokeWeight(scale);
        pgPrint.translate(printResolution / 2, printResolution / 2);

        fileName = PATH + "1_3_" + System.currentTimeMillis();

        color = PVector.random3D();
        color.mult(2);

        strokeWeight(1);
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
            PVector dir = new PVector(0, random(-1, 1));
            dir.normalize();
            float x = random(-width / 2 + BORDER, width / 2 - BORDER);
            float y = -1;
            if (dir.y < 0) {
                y = 1;
            }
            One one = new One(x, y, d, color);
            one.setDirection(dir);
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
            color.mult(a.dist(b) * (iterations - 1) / MAX_ITERATIONS);

            stroke(color.x, color.y, color.z, alpha);
            line(a.x, a.y, b.x, b.y);

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
            pgPrint.save(fileName + "_highres.jpg");
            exit();
        }

        if (groups.size() == 0) {
            objects.clear();
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

        // get groups for elements out of screen
        List<List<One>> removeGroup = new ArrayList<List<One>>();
        for (List<One> group : groups) {
            if (isOutOfScreen(group)) {
                removeGroup.add(group);
            }
        }

        groups.removeAll(removeGroup);
    }

    /**
     * Check if the element is out of screen.
     *
     * @param list Elements that needs to be checked.
     * @return boolean
     */
    private boolean isOutOfScreen(List<One> list) {
        int maxDistance = 0;
        float pos = 0;
        for (One one : list) {
            maxDistance += one.getDiameter();
            pos = Math.max(pos, Math.abs(one.getPos().y));
        }
        return maxDistance < pos;
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
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationThreePrint"});
    }
}
