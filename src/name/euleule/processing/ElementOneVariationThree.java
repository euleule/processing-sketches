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
public class ElementOneVariationThree extends PApplet {

    // Number of elements used for rendering
    final int NUM_OBJECTS = 80;
    // Minimum size of elements
    final int D_MIN = 50;
    // Maximum size of elements
    final int D_MAX = 150;
    // Border width in px
    final int BORDER = 200;
    final int COLORED_BORDER_RATIO = 0;
    // Number of iterations
    final int MAX_ITERATIONS = 5;

    List<One> objects;
    List<List<One>> groups;
    PVector color;
    int iterations = 0;
    String folder = null;

    /**
     * Set up scene.
     */
    public void setup() {
        size(1600, 1600, P2D);
        
        background(255, 250, 240);
//        stroke(245);
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                if (random(0, 3) > 2) {
//                    point(i, j);
//                }
//            }
//        }

        image(loadImage("/Users/robert/Desktop/back.png"), 0, 0);

        selectFolder("Select an output folder!", "folderSelected");
        color = PVector.random3D();

        if (COLORED_BORDER_RATIO > 0) {
            float borderWidth = BORDER / COLORED_BORDER_RATIO;
            noStroke();
            PVector c = color.get();
            c.normalize();
            c.mult(96);
            fill(c.x, c.y, c.z);
            rect(0, 0, width, borderWidth);
            rect(0, 0, borderWidth, height);
            rect(0, height - borderWidth, width, borderWidth);
            rect(width - borderWidth, 0, borderWidth, height);
        }

        strokeWeight(1);
        reset();
    }

    /**
     * Callback function for folder selection dialogue.
     *
     * @param selection Folder selected.
     */
    public void folderSelected(File selection) {
        if (selection != null) {
            folder = selection.getAbsolutePath() + File.separator;
        }
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

            PVector color = o1.getColor().get();
            color.mult(a.dist(b) * (iterations - 1) / MAX_ITERATIONS);

            stroke(color.x, color.y, color.z, alpha);
            line(a.x, a.y, b.x, b.y);
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
            if (folder != null) {
                save(folder + System.currentTimeMillis() + ".jpg");
            }
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
//        if (o1.getDirection().y > 0 && o2.getDirection().y > 0 || o1.getDirection().y < 0 && o2.getDirection().y < 0) {
        group.add(o1);
        group.add(o2);
        groups.add(group);
//        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationThree"});
    }
}
