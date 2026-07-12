package org.townsimulator.graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import jecs.core.World;
import org.townsimulator.components.Movement;
import org.townsimulator.components.Position;
import org.townsimulator.components.TSSprite;
import org.townsimulator.components.manager.PlayerSpriteAtlas;

public final class CharacterSpriteAnimator {

    public static final float IDLE_FRAME_DURATION = 0.45f;
    public static final float WALK_FRAME_DURATION = 0.12f;
    public static final float SPRITE_DISPLAY_SIZE = 32f;

    private CharacterSpriteAnimator() {
    }

    public static void update(World world, float deltaSeconds) {
        PlayerSpriteAtlas atlas = PlayerSpriteAtlas.get();

        for (int entityId : world.query(TSSprite.Component.class, Position.Component.class)) {
            TSSprite.Component sprite = world.getComponent(entityId, TSSprite.Component.class);
            Position.Component position = world.getComponent(entityId, Position.Component.class);
            Movement.Component movement = world.getComponent(entityId, Movement.Component.class);
            if (sprite == null || position == null) {
                continue;
            }

            boolean moving = (movement != null && movement.wantsToMove) || sprite.keyboardWalkTimer > 0f;
            if (sprite.keyboardWalkTimer > 0f) {
                sprite.keyboardWalkTimer = Math.max(0f, sprite.keyboardWalkTimer - deltaSeconds);
            }
            CharacterAnimState targetState = moving ? CharacterAnimState.WALK : CharacterAnimState.IDLE;
            if (sprite.animState != targetState) {
                sprite.animState = targetState;
                sprite.frameIndex = 0;
                sprite.frameTimer = 0f;
            }

            if (movement != null && movement.wantsToMove) {
                float dx = movement.xDst - position.xPos;
                float dy = movement.yDst - position.yPos;
                Direction4 toward = Direction4.fromDelta(dx, dy);
                if (toward != null) {
                    sprite.facing = toward;
                }
            } else {
                Direction4 deltaDirection = Direction4.fromDelta(sprite.lastDeltaX, sprite.lastDeltaY);
                if (deltaDirection != null) {
                    sprite.facing = deltaDirection;
                }
            }

            float frameDuration = sprite.animState == CharacterAnimState.WALK
                    ? WALK_FRAME_DURATION
                    : IDLE_FRAME_DURATION;
            sprite.frameTimer += deltaSeconds;
            if (sprite.frameTimer >= frameDuration) {
                sprite.frameTimer -= frameDuration;
                sprite.frameIndex = (sprite.frameIndex + 1) % sprite.animState.frameCount();
            }

            applyFrame(sprite, atlas);
            syncSpritePosition(sprite, position);
        }
    }

    public static void recordDelta(TSSprite.Component sprite, float dx, float dy) {
        if (sprite == null) {
            return;
        }
        if (Math.abs(dx) >= 0.001f || Math.abs(dy) >= 0.001f) {
            sprite.lastDeltaX = dx;
            sprite.lastDeltaY = dy;
        }
    }

    public static void syncSpritePosition(TSSprite.Component sprite, Position.Component position) {
        if (sprite == null || sprite.activeSprite == null || position == null) {
            return;
        }
        sprite.activeSprite.setPosition(
                position.xPos + SPRITE_DISPLAY_SIZE / 2f,
                position.yPos
        );
    }

    public static float tileCenterOffset() {
        return SPRITE_DISPLAY_SIZE / 2f;
    }

    public static void applyFrame(TSSprite.Component sprite, PlayerSpriteAtlas atlas) {
        var region = atlas.getRegion(sprite.facing, sprite.animState, sprite.frameIndex);
        if (sprite.activeSprite == null) {
            sprite.activeSprite = new Sprite(region);
            sprite.activeSprite.setSize(SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE);
            sprite.activeSprite.setOrigin(SPRITE_DISPLAY_SIZE / 2f, 0f);
        } else {
            sprite.activeSprite.setRegion(region);
        }
        sprite.activeSprite.setFlip(sprite.facing == Direction4.E, false);
    }
}
