package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;


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
    }
}
