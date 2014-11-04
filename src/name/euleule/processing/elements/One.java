package name.euleule.processing.elements;

import processing.core.PVector;

/**
 * A circular element that moves in a straight line into a random direction.
 */
public class One {
    private PVector pos;
    private PVector direction;
    private float diameter;
    private PVector color;

    /**
     * @param x        x position of the element
     * @param y        y position of the element
     * @param diameter diameter of the element
     * @param color    color of the element
     */
    public One(float x, float y, float diameter, PVector color) {
        this.pos = new PVector(x, y);
        this.diameter = diameter;
        this.color = color;
        direction = PVector.random2D();
    }

    /**
     * Check whether the element intersects with another element.
     *
     * @param o Other element
     * @return boolean
     */
    public boolean intersect(One o) {
        if (o == null || this.equals(o)) {
            return false;
        }
        float distance = pos.dist(o.getPos());
        return (diameter / 2 - o.getDiameter() / 2 <= distance && diameter / 2 + o.getDiameter() / 2 >= distance);
    }

    /**
     * Update elements position.
     */
    public void update() {
        pos.x += direction.x;
        pos.y += direction.y;
    }

    /**
     * Get position
     *
     * @return PVector
     */
    public PVector getPos() {
        return pos;
    }

    /**
     * Get diameter
     *
     * @return float
     */
    public float getDiameter() {
        return diameter;
    }

    /**
     * Get color
     *
     * @return PVector
     */
    public PVector getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        One one = (One) o;

        if (Float.compare(one.diameter, diameter) != 0) return false;
        if (color != null ? !color.equals(one.color) : one.color != null) return false;
        if (!direction.equals(one.direction)) return false;
        if (pos != null ? !pos.equals(one.pos) : one.pos != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pos != null ? pos.hashCode() : 0;
        result = 31 * result + direction.hashCode();
        result = 31 * result + (diameter != +0.0f ? Float.floatToIntBits(diameter) : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }
}
