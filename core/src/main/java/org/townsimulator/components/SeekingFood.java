package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;

import static org.townsimulator.utils.Constants.AGE_COMPONENT_TABLE_INDEX;

public final class SeekingFood {

    private SeekingFood() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        public static int tableIndex() {
            return AGE_COMPONENT_TABLE_INDEX;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
