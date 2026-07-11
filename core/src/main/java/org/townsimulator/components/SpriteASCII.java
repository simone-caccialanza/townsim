package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;
import org.townsimulator.GlobalGrid;



public final class SpriteASCII {

    private SpriteASCII() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public Character spriteCharacter;

        public Component(char spriteCharacter) {
            this.spriteCharacter = spriteCharacter;
        }

        public Component(char spriteCharacter, int x, int y) {
            this.spriteCharacter = spriteCharacter;
            GlobalGrid.getInstance();
            GlobalGrid.getInstance().cellAt(x, y).spriteCharacter = spriteCharacter;
        }

        @Override
        protected void reset() {
            this.spriteCharacter = ' ';
        }
    }
}
