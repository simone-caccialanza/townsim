package org.townsimulator.input;

import jecs.core.World;
import org.townsimulator.GlobalGrid;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;
import org.townsimulator.components.TSSprite;
import org.townsimulator.graphics.CharacterAnimState;
import org.townsimulator.graphics.CharacterSpriteAnimator;
import org.townsimulator.graphics.Direction4;

public final class PlayerMovementInput {

    public static final float X_STEP = 960f / 30f;
    public static final float Y_STEP = 640f / 20f;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private PlayerMovementInput() {
    }

    public static boolean apply(World world, int playerEntityId, Direction direction) {
        if (playerEntityId < 0 || direction == null) {
            return false;
        }

        Position.Component position = world.getComponent(playerEntityId, Position.Component.class);
        if (position == null) {
            return false;
        }

        float dx = 0f;
        float dy = 0f;
        switch (direction) {
            case UP -> dy = Y_STEP;
            case DOWN -> dy = -Y_STEP;
            case LEFT -> dx = -X_STEP;
            case RIGHT -> dx = X_STEP;
        }

        int oldX = (int) position.xPos;
        int oldY = (int) position.yPos;
        position.xPos += dx;
        position.yPos += dy;

        TSSprite.Component sprite = world.getComponent(playerEntityId, TSSprite.Component.class);
        if (sprite != null) {
            sprite.facing = Direction4.fromKeyboard(direction);
            CharacterSpriteAnimator.recordDelta(sprite, dx, dy);
            sprite.keyboardWalkTimer = CharacterSpriteAnimator.WALK_FRAME_DURATION * CharacterAnimState.WALK_FRAMES;
        }

        syncGrid(world, playerEntityId, position, oldX, oldY);
        CharacterSpriteAnimator.syncSpritePosition(sprite, position);
        return true;
    }

    private static void syncGrid(World world, int playerEntityId, Position.Component position, int oldX, int oldY) {
        GlobalGrid grid = GlobalGrid.getInstance();
        grid.cellAt(oldX, oldY).spriteCharacter = ' ';
        grid.setBlocked(oldX, oldY, false);

        SpriteASCII.Component ascii = world.getComponent(playerEntityId, SpriteASCII.Component.class);
        if (ascii != null) {
            grid.cellAt((int) position.xPos, (int) position.yPos).spriteCharacter = ascii.spriteCharacter;
        }
        grid.setBlocked((int) position.xPos, (int) position.yPos, position.blocksTile);
    }
}
