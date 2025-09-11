package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static org.townsimulator.utils.Constants.HAPPINESS_COMPONENT_TABLE_INDEX;

public final class Happiness {

    private Happiness() {
    }

    public static final int UPPER_BOUND = 100;
    public static final int LOWER_BOUND = 0;

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public int happiness;

        public Component() {
            this.happiness = UPPER_BOUND / 2;
        }

        public Component(int happiness) {
            this.happiness = happiness;
        }

        public void increase(int amount) {
            this.happiness += amount;
            if (this.happiness < LOWER_BOUND) {
                this.happiness = LOWER_BOUND;
            } else if (this.happiness > UPPER_BOUND) {
                this.happiness = UPPER_BOUND;
            }
        }

        @Override
        protected void reset() {
            happiness = UPPER_BOUND / 2;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return HAPPINESS_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
