package org.townsimulator.components;


import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import java.util.List;

import static org.townsimulator.utils.Constants.MOVEMENT_COMPONENT_TABLE_INDEX;

public final class Movement {

    private Movement() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {
        @LogField
        public float xVel;
        @LogField
        public float yVel;
        @LogField
        public float xDst;
        @LogField
        public float yDst;
        public List<int[]> path = null;
        public int pathIndex = 0;

        public boolean wantsToMove;

        public Component(float xVel, float yVel, float xDst, float yDst) {
            this.xVel = xVel;
            this.yVel = yVel;
            this.xDst = xDst;
            this.yDst = yDst;
            this.wantsToMove = true;
        }

        public Component(float xVel, float yVel) {
            this.xVel = xVel;
            this.yVel = yVel;
            this.wantsToMove = false;
        }

        public Component() {
            this.wantsToMove = false;
        }

        @Override
        public void reset() {
            xVel = 0;
            yVel = 0;
            xDst = 0;
            yDst = 0;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return MOVEMENT_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
