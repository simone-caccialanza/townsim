package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static org.townsimulator.utils.Constants.BUILDING_COMPONENT_TABLE_INDEX;

public final class Building {

    private Building() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public String name;

        public Component(String name) {
            this.name = name;
        }

        public Component() {
            this.name = "";
        }

        @Override
        protected void reset() {
            this.name = "";
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return BUILDING_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
