package org.townsimulator.input;

import jecs.core.World;
import org.townsimulator.GlobalGrid;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;
import org.townsimulator.components.TSSprite;

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

        int oldX = (int) position.xPos;
        int oldY = (int) position.yPos;

        switch (direction) {
            case UP -> position.yPos += Y_STEP;
            case DOWN -> position.yPos -= Y_STEP;
            case LEFT -> position.xPos -= X_STEP;
            case RIGHT -> position.xPos += X_STEP;
        }

        syncGrid(world, playerEntityId, position, oldX, oldY);
        syncTextureSprite(world, playerEntityId, position);
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

    private static void syncTextureSprite(World world, int playerEntityId, Position.Component position) {
        TSSprite.Component sprite = world.getComponent(playerEntityId, TSSprite.Component.class);
        if (sprite != null && sprite.activeSprite != null) {
            sprite.activeSprite.setPosition(position.xPos, position.yPos);
        }
    }
}
