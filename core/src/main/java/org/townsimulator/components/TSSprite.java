package org.townsimulator.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;
import org.townsimulator.graphics.CharacterAnimState;
import org.townsimulator.graphics.Direction4;

public final class TSSprite {

    private TSSprite() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public Direction4 facing = Direction4.S;

        @LogField
        public CharacterAnimState animState = CharacterAnimState.IDLE;

        public int frameIndex;
        public float frameTimer;
        public float lastDeltaX;
        public float lastDeltaY;
        public float keyboardWalkTimer;

        @LogField
        public Sprite activeSprite;

        @Override
        protected void reset() {
            facing = Direction4.S;
            animState = CharacterAnimState.IDLE;
            frameIndex = 0;
            frameTimer = 0f;
            lastDeltaX = 0f;
            lastDeltaY = 0f;
            keyboardWalkTimer = 0f;
            activeSprite = null;
        }
    }
}
