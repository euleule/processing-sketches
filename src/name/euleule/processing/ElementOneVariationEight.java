package name.euleule.processing;

import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class ElementOneVariationEight extends PApplet {


    // TODO What does this sketch?

    // Number of elements used for rendering
    final int NUM_OBJECTS = 250;

    // Minimum size of elements
    final int D_MIN = 20;
    // Maximum size of elements
    final int D_MAX = 50;
    // Border width in px
    final int BORDER = 50;
    // Number of iterations
    final int MAX_ITERATIONS = 1;

    final int SIZE = 1000;

    List<One> objects;
    List<List<One>> groups;
    PVector color;
    int iterations;
    String folder = "/Users/robert/Desktop/Sketches/";

    @Override
    public void settings(){
        size(SIZE, SIZE);
    }

    /**
     * Re-initialize the elements.
     */
    protected void reset() {
        objects = new ArrayList<>();
        groups = new ArrayList<>();
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

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationEight"});
    }

    /**
     * Set up scene.
     */
    public void setup() {
        background(255);
        colorMode(HSB);
        strokeWeight(1);
        iterations = 0;
        reset();
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

            float alpha = map(a.dist(b), 0, D_MAX * 2, 0, 100);

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

}
