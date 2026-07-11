package org.townsimulator.components;


import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;


public final class Position {

    private Position() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {
        @LogField
        public float xPos;
        @LogField
        public float yPos;
        public boolean blocksTile = false;

        public Component(float xPos, float yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }

        public Component(float xPos, float yPos, boolean blocksTile) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.blocksTile = blocksTile;
        }

        public Component() {
            this.xPos = 0;
            this.yPos = 0;
        }

        @Override
        public void reset() {
            xPos = 0;
            yPos = 0;
        }
    }
}
