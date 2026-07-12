package org.townsimulator.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;
import org.townsimulator.graphics.Direction4;

public final class TSSprite {

    private TSSprite() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public Direction4 facing = Direction4.S;

        public float lastDeltaX;
        public float lastDeltaY;

        @LogField
        public Sprite activeSprite;

        @Override
        protected void reset() {
            facing = Direction4.S;
            lastDeltaX = 0f;
            lastDeltaY = 0f;
            activeSprite = null;
        }
    }
}
