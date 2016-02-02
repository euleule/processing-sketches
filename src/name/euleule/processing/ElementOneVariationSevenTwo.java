package name.euleule.processing;

import name.euleule.processing.elements.Flower;
import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class ElementOneVariationSevenTwo extends PApplet {

    // Number of elements used for rendering
    final int NUM_OBJECTS = 17;
    // Minimum size of elements
    final int D_MIN = 60;
    // Maximum size of elements
    final int D_MAX = 240;

    // Border width in px
    final int BORDER = 0;
    // Number of iterations

    final int SIZE = 1200;

    List<Flower> flowers = new ArrayList<>();
    PVector color;

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
        frameRate(240);
        reset();
    }

    /**
     * Re-initialize the elements.
     */
    protected void reset() {
        PVector direction = new PVector(0, 1);
        float c = random(0, 359);
        System.out.println(c);

        for (int i = 0; i < NUM_OBJECTS; i++) {
            float d = random(D_MIN, D_MAX);
            float x = random(-width / 2 + BORDER + d / 2 - (D_MAX + D_MIN), width / 2 - BORDER - d / 2 - (D_MAX + D_MIN));
            float y = random(-height / 2 + BORDER + d / 2 - (D_MAX + D_MIN), height / 2 - BORDER - d / 2 - (D_MAX + D_MIN));

            Flower flower = new Flower(new PVector(x, y), D_MAX + D_MIN, new PVector(c + random(0, 60), 255, 128), direction);

            flowers.add(flower);
        }
    }

    /**
     * Draw scene.
     */
    @Override
    public void draw() {
        update();
        translate(width / 2, height / 2);

        for (Flower flower : flowers) {
            PVector fPos = flower.getPosition();
            for (One o1 : flower.getElements()) {
                for (One o2 : flower.getElements()) {
                    PVector a = o1.getPos();
                    PVector b = o2.getPos();

                    float alpha = min((D_MIN + D_MAX) * 2 / a.dist(b), 9);

                    if (o1.getColor() != null) {
                        PVector color = o1.getColor().copy();

                        stroke(color.x, a.dist(b)*1.75f, color.z, alpha);
                        line(fPos.x + a.x, fPos.y + a.y, fPos.x + b.x, fPos.y + b.y);
                    }
                }
            }
        }
    }

    /**
     * Update the element in the scene.
     */
    protected void update() {
        new ArrayList<>(flowers).stream().filter(this::isOutOfScreen).forEach(flowers::remove);

        if (flowers.size() == 0) {
            noLoop();
            save("/Users/robert/Desktop/" + System.currentTimeMillis() + ".jpg");
            exit();
        }

        flowers.forEach(Flower::update);
    }

    /**
     * Check if the element is out of screen.
     *
     * @param one Element that needs to be checked.
     * @return boolean
     */
    protected boolean isOutOfScreen(Flower one) {
        return Math.abs(one.getPosition().x) + BORDER > width / 2 || Math.abs(one.getPosition().y) + BORDER > height / 2;
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.ElementOneVariationSevenTwo"});
    }
}
