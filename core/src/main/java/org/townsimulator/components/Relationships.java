package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;

import static org.townsimulator.utils.Constants.RELATIONSHIPS_COMPONENT_TABLE_INDEX;

public final class Relationships {

    private Relationships() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return RELATIONSHIPS_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
