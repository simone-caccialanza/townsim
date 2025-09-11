package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;
import org.townsimulator.GlobalGrid;

import static org.townsimulator.utils.Constants.SPRITE_ASCII_COMPONENT_TABLE_INDEX;


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
            GlobalGrid.getInstance().getInstance().cellAt(x, y).spriteCharacter = spriteCharacter;
        }

        @Override
        protected void reset() {
            this.spriteCharacter = ' ';
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return SPRITE_ASCII_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
