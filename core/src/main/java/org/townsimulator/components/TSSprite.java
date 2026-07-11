package org.townsimulator.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

public final class TSSprite {

    private TSSprite() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public Sprite[] sprites;

        @LogField
        public Sprite activeSprite;

        public Component(int size) {
            this.sprites = new Sprite[size];
        }

        public void setSprites(Sprite[] sprites) {
            this.sprites = sprites;
            this.activeSprite = sprites.length > 0 ? sprites[0] : null;
        }

        @Override
        protected void reset() {
            this.sprites = new Sprite[0];
            this.activeSprite = null;
        }
    }
}
