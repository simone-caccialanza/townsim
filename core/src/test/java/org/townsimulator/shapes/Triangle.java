package org.townsimulator.shapes;

public class Triangle extends Shape{

    private final float b;
    private final float h;

    public Triangle(float b, float h) {
        this.b = b;
        this.h = h;
    }

    @Override
    public float getArea() {
        return b*h/2;
    }
}
