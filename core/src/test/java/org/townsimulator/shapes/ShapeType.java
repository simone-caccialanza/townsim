package org.townsimulator.shapes;

public enum ShapeType {
    TRIANGLE(0), RECTANGLE(1);

    public Integer index;

    ShapeType(Integer index) {
        this.index = index;
    }
}
