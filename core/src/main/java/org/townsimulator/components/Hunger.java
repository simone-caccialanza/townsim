package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static org.townsimulator.utils.Constants.HUNGER_COMPONENT_TABLE_INDEX;

public final class Hunger {

    private Hunger() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public float hunger;
        public float ratio;
        public boolean isHungry = false;

        public Component() {
            this.hunger = 0;
            this.ratio = 1.0f;
        }

        public Component(float ratio) {
            this.hunger = 0;
            this.ratio = ratio;
        }

        public Component(float ratio, float hunger) {
            this.hunger = hunger;
            this.ratio = ratio;
        }


        @Override
        protected void reset() {
            this.hunger = 0;
            this.ratio = 1.0f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return HUNGER_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
