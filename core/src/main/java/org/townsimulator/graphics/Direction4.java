package org.townsimulator.graphics;

public enum Direction4 {
    N(0, 0),
    S(1, 1),
    W(2, 2),
    E(2, 2);

    private final int sheetRow;
    private final int sheetColumn;

    Direction4(int sheetRow, int sheetColumn) {
        this.sheetRow = sheetRow;
        this.sheetColumn = sheetColumn;
    }

    public int sheetRow() {
        return sheetRow;
    }

    public int sheetColumn() {
        return sheetColumn;
    }

    public static Direction4 fromDelta(float dx, float dy) {
        if (Math.abs(dx) < 0.001f && Math.abs(dy) < 0.001f) {
            return null;
        }
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? E : W;
        }
        return dy > 0 ? N : S;
    }

    public static Direction4 fromKeyboard(org.townsimulator.input.PlayerMovementInput.Direction direction) {
        return switch (direction) {
            case UP -> N;
            case DOWN -> S;
            case LEFT -> W;
            case RIGHT -> E;
        };
    }
}
