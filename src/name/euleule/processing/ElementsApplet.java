package name.euleule.processing;

import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 06.04.15.
 */
public class ElementsApplet extends PApplet {

        // Number of elements used for rendering
        final int NUM_OBJECTS = 600;
        // Minimum size of elements
        final int D_MIN = 150;
        // Maximum size of elements
        final int D_MAX = 300;
        // Border width in px
        final int BORDER = 300;
        // Number of iterations
        final int MAX_ITERATIONS = 1;

        final int SIZE=6000;

        List<One> objects;
        List<List<One>> groups;
        PVector color;
        int iterations;
        String folder = null;

        /**
         * Set up scene.
         */
        public void setup() {
            size(SIZE, SIZE, P2D);
            frameRate(600);
            background(255);
            colorMode(HSB);
            strokeWeight(1);
            selectFolder("Select an output folder!", "folderSelected");
            iterations = 0;
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
        protected void reset() {
            objects = new ArrayList<One>();
            groups = new ArrayList<List<One>>();
            color = PVector.random3D();
            PVector direction = PVector.random2D();
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

                PVector color = o1.getColor().get();

                stroke(color.x, a.dist(b), color.z, alpha);
//            stroke(color.x, color.y, color.z, alpha);
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
        protected boolean isOutOfScreen(One one) {
            return Math.abs(one.getPos().x) + BORDER > width / 2 || Math.abs(one.getPos().y) + BORDER > height / 2;
        }

        /**
         * Check all elements for intersections.
         */
        protected void checkIntersections() {
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
        protected void addGroup(One o1, One o2) {
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
            PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationSeven"});
        }

}
