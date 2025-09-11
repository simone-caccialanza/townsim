package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static org.townsimulator.utils.Constants.FOOD_PROVIDER_COMPONENT_TABLE_INDEX;

public final class FoodProvider {

    private FoodProvider() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public int foodSupply;

        public Component(int foodSupply) {
            this.foodSupply = foodSupply;
        }

        @Override
        protected void reset() {
            this.foodSupply = 0;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return FOOD_PROVIDER_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
