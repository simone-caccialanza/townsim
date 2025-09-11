package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static org.townsimulator.utils.Constants.THIRST_COMPONENT_TABLE_INDEX;

public final class Thirst {

    private Thirst() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {
        @LogField
        public float thirst;
        public float ratio;

        public Component() {
            this.thirst = 0;
            this.ratio = 1.0f;
        }

        public Component(float ratio) {
            this.thirst = 0;
            this.ratio = ratio;
        }

        public Component(float ratio, float thirst) {
            this.thirst = thirst;
            this.ratio = ratio;
        }


        @Override
        protected void reset() {
            this.thirst = 0;
            this.ratio = 1.0f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return THIRST_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
