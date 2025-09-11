package org.townsimulator.shapes;

import java.util.Map;

public class ShapeWithType {
    public final float b;
    public final float h;
    public final ShapeType type;

    public ShapeWithType(float b, float h, ShapeType type) {
        this.b = b;
        this.h = h;
        this.type = type;
    }

    public static float getAreaSwitch(ShapeWithType[] shapes) {
        var area = 0f;
        for (ShapeWithType shape : shapes) {
            area += switch (shape.type) {
                case TRIANGLE -> shape.b * shape.h / 2;
                case RECTANGLE -> shape.b * shape.h;
                default -> 0;
            };

        }
        return area;
    }

    public static float getAreaCoefficients(ShapeWithType[] shapes, Map<ShapeType,Float> coefficients){
        var area = 0f;
        for (ShapeWithType shape : shapes) {
            area += coefficients.get(shape.type) * shape.b * shape.h;
        }
        return area;
    }

    public static float getAreaCoefficientsArray(ShapeWithType[] shapes, Float[] coefficients){
        var area = 0f;
        for (ShapeWithType shape : shapes) {
            area += coefficients[shape.type.index] * shape.b * shape.h;
        }
        return area;
    }
}
