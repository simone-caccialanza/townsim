package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static org.townsimulator.utils.Constants.HEALTH_COMPONENT_TABLE_INDEX;

public final class Health {

    private Health() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {
        @LogField
        public int currentHealth;
        @LogField
        public int maxHealth;

        public Component(int currentHealth, int maxHealth) {
            this.currentHealth = currentHealth;
            this.maxHealth = maxHealth;
        }

        public Component(int maxHealth) {
            this.currentHealth = maxHealth;
            this.maxHealth = maxHealth;
        }

        @Override
        protected void reset() {
            this.currentHealth = this.maxHealth;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return HEALTH_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
