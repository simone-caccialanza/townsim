package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import java.time.LocalDateTime;


public final class Age {

    private Age() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {
        @LogField
        private final LocalDateTime birthDate;

        public Component(Integer age) {
            this.birthDate = LocalDateTime.now().minusYears(age);
        }

        public Component(LocalDateTime birthDate) {
            this.birthDate = birthDate;
        }
    }
}
