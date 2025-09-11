package org.townsimulator.components;


import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static org.townsimulator.utils.Constants.POSITION_COMPONENT_TABLE_INDEX;

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

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return POSITION_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
