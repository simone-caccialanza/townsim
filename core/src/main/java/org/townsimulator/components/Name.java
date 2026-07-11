package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import java.util.List;


public final class Name {

    private Name() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {
        @LogField
        private String name;

        @LogField
        private List<String> middleNames;

        @LogField
        private String lastName;

        public Component(String name, String lastName) {
            this.name = name;
            this.lastName = lastName;
        }

        @Override
        public void reset() {
            name = "";
            middleNames.clear();
            lastName = "";
        }
    }
}
