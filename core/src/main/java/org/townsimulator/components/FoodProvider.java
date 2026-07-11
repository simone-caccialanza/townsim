package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;


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
    }
}
