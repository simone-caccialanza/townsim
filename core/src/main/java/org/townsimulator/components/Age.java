package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import java.time.LocalDateTime;

import static org.townsimulator.utils.Constants.AGE_COMPONENT_TABLE_INDEX;

public final class Age {

    private Age() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {
        @LogField
        private LocalDateTime birthDate;

        public Component(Integer age) {
            this.birthDate = LocalDateTime.now().minusYears(age);
        }

        public Component(LocalDateTime birthDate) {
            this.birthDate = birthDate;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return AGE_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
