package org.townsimulator.shapes;

public class Rectangle extends Shape{

    private final float b;
    private final float h;

    public Rectangle(float b, float h) {
        this.b = b;
        this.h = h;
    }

    @Override
    public float getArea() {
        return b*h;
    }
}
