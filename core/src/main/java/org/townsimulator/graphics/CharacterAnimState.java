package org.townsimulator.graphics;

public enum CharacterAnimState {
    IDLE,
    WALK;

    public static final int IDLE_FRAMES = 2;
    public static final int WALK_FRAMES = 4;

    public int frameCount() {
        return this == IDLE ? IDLE_FRAMES : WALK_FRAMES;
    }

    public int frameOffset() {
        return this == IDLE ? 0 : IDLE_FRAMES;
    }
}
