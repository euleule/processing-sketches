package name.euleule.processing.elements;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class Flower extends PApplet {

    public PVector getPosition() {
        return position;
    }

    private PVector position;
    private PVector start;
    private PVector direction;
    private float diameter;
    private PVector color;
    private float size;

    List<One> elements = new ArrayList<>();

    public Flower(PVector position, int diameter, PVector color, PVector direction) {
        this.position = position;
        int n = (int) random(5, 10);
        this.diameter = diameter;
        this.color = color;
        this.direction = direction;

        for (int i = 0; i < n; i++) {
            One one = new One(random(0, diameter), random(diameter/2, diameter), random(diameter / 8, diameter / 4), color);
            one.setDirection(direction);
            elements.add(one);
        }
    }

    public List<One> getElements() {
        return elements;
    }

    /**
     * Update the element in the scene.
     */
    public void update() {
        position.add(direction);

        for (One o1 : elements) {
            for (One o2 : elements) {
                PVector bari = o1.getPos().copy();

                bari.sub(o2.getPos());
                bari.normalize();
                bari.div(50);
                o1.getPos().sub(bari);
            }
        }
    }
}
