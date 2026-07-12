package org.townsimulator.systems;

import jecs.core.World;
import jecs.core.system.ECSSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.townsimulator.GlobalGrid;
import org.townsimulator.components.Movement;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;
import org.townsimulator.components.Task;
import org.townsimulator.components.TSSprite;
import org.townsimulator.graphics.CharacterSpriteAnimator;

import java.util.*;

import static org.townsimulator.components.Task.Status.FINISHED;
import static org.townsimulator.components.Task.Status.RUNNING;

import static org.townsimulator.utils.Constants.MAP_LENGTH;
import static org.townsimulator.utils.Constants.MAP_WIDTH;

public class MovementSystem extends ECSSystem {
    private static final Logger log = LoggerFactory.getLogger(MovementSystem.class);

    @Override
    public void run(World world, double deltaSeconds) {
        Map<String, Position.Component> positionMap = new HashMap<>();
        Map<Position.Component, Movement.Component> movementMap = new HashMap<>();
        Map<Position.Component, SpriteASCII.Component> asciiSpriteMap = new HashMap<>();
        Map<Position.Component, TSSprite.Component> spriteMap = new HashMap<>();

        for (int entityId : world.query(
                Position.Component.class, Movement.Component.class, SpriteASCII.Component.class)) {
            Position.Component pos = world.getComponent(entityId, Position.Component.class);
            Movement.Component mov = world.getComponent(entityId, Movement.Component.class);
            SpriteASCII.Component asciiSpr = world.getComponent(entityId, SpriteASCII.Component.class);
            if (pos == null || mov == null || asciiSpr == null) {
                continue;
            }
            String key = (int) pos.xPos + "," + (int) pos.yPos;
            positionMap.put(key, pos);
            movementMap.put(pos, mov);
            asciiSpriteMap.put(pos, asciiSpr);
            spriteMap.put(pos, world.getComponent(entityId, TSSprite.Component.class));
        }

        List<Runnable> actions = new ArrayList<>();
        Set<Position.Component> movedThisFrame = new HashSet<>();
        Set<String> reserved = new HashSet<>();
        Set<Set<Position.Component>> swapsPerformed = new HashSet<>();

        for (int entityId : world.query(
                Position.Component.class, Movement.Component.class, SpriteASCII.Component.class)) {
            Position.Component pos = world.getComponent(entityId, Position.Component.class);
            Movement.Component mov = world.getComponent(entityId, Movement.Component.class);
            SpriteASCII.Component asciiSprite = world.getComponent(entityId, SpriteASCII.Component.class);
            TSSprite.Component sprite = world.getComponent(entityId, TSSprite.Component.class);
            if (pos == null || mov == null || asciiSprite == null) {
                continue;
            }

            if (!mov.wantsToMove || movedThisFrame.contains(pos)) continue;

            if ((int) pos.xPos == (int) mov.xDst && (int) pos.yPos == (int) mov.yDst) {
                completeMovement(world, entityId, mov);
                continue;
            }

            if (mov.path == null || mov.pathIndex >= mov.path.size()) {
                mov.path = AStar.findPath((int) pos.xPos, (int) pos.yPos, (int) mov.xDst, (int) mov.yDst);
                mov.pathIndex = 0;
                if (mov.path.isEmpty()) {
                    mov.wantsToMove = false;
                    continue;
                }
            }

            int maxAdvance = (int) Math.max(Math.abs(mov.xVel), Math.abs(mov.yVel));
            if (maxAdvance == 0) maxAdvance = 1;
            maxAdvance = Math.min(maxAdvance, mov.path.size() - mov.pathIndex);

            int[] nextStep = null;
            int stepsTaken = 0;

            for (int s = 1; s <= maxAdvance; s++) {
                int[] step = mov.path.get(mov.pathIndex + s - 1);
                if (GlobalGrid.getInstance().isBlocked(step[0], step[1])) {
                    break;
                }
                nextStep = step;
                stepsTaken = s;
            }

            if (nextStep == null) {
                mov.path = AStar.findPath((int) pos.xPos, (int) pos.yPos, (int) mov.xDst, (int) mov.yDst);
                mov.pathIndex = 0;
                continue;
            }

            String nextKey = nextStep[0] + "," + nextStep[1];
            String currentKey = (int) pos.xPos + "," + (int) pos.yPos;

            Position.Component occupying = positionMap.get(nextKey);
            if (occupying != null && occupying != pos && occupying.blocksTile) {
                Movement.Component theirMov = movementMap.get(occupying);

                if (theirMov != null) {
                    if (theirMov.path == null || theirMov.pathIndex >= theirMov.path.size()) {
                        theirMov.path = AStar.findPath((int) occupying.xPos, (int) occupying.yPos,
                            (int) theirMov.xDst, (int) theirMov.yDst);
                        theirMov.pathIndex = 0;
                    }

                    if (!theirMov.path.isEmpty()) {
                        int[] theirNextStep = theirMov.path.get(theirMov.pathIndex);
                        if (theirNextStep[0] == (int) pos.xPos && theirNextStep[1] == (int) pos.yPos) {
                            Set<Position.Component> swapPair = new HashSet<>(Set.of(pos, occupying));
                            if (!swapsPerformed.contains(swapPair) && !reserved.contains(nextKey) && !reserved.contains(currentKey)) {
                                reserved.add(nextKey);
                                reserved.add(currentKey);
                                swapsPerformed.add(swapPair);

                                int finalStepsTaken = stepsTaken;
                                actions.add(() -> {
                                    GlobalGrid grid = GlobalGrid.getInstance();

                                    grid.cellAt((int) pos.xPos, (int) pos.yPos).spriteCharacter = ' ';
                                    grid.setBlocked((int) pos.xPos, (int) pos.yPos, false);
                                    grid.cellAt((int) occupying.xPos, (int) occupying.yPos).spriteCharacter = ' ';
                                    grid.setBlocked((int) occupying.xPos, (int) occupying.yPos, false);

                                    float posOldX = pos.xPos;
                                    float posOldY = pos.yPos;
                                    float occOldX = occupying.xPos;
                                    float occOldY = occupying.yPos;
                                    pos.xPos = occOldX;
                                    pos.yPos = occOldY;
                                    occupying.xPos = posOldX;
                                    occupying.yPos = posOldY;

                                    mov.pathIndex += finalStepsTaken;
                                    theirMov.pathIndex += 1;
                                    movedThisFrame.add(pos);
                                    movedThisFrame.add(occupying);

                                    SpriteASCII.Component theirAsciiSprite = asciiSpriteMap.get(occupying);

                                    grid.cellAt((int) pos.xPos, (int) pos.yPos).spriteCharacter = asciiSprite.spriteCharacter;
                                    grid.setBlocked((int) pos.xPos, (int) pos.yPos, pos.blocksTile);
                                    updateSpriteAfterMove(sprite, pos, posOldX, posOldY);

                                    grid.cellAt((int) occupying.xPos, (int) occupying.yPos).spriteCharacter = theirAsciiSprite.spriteCharacter;
                                    grid.setBlocked((int) occupying.xPos, (int) occupying.yPos, occupying.blocksTile);

                                    TSSprite.Component theirSprite = spriteMap.get(occupying);
                                    updateSpriteAfterMove(theirSprite, occupying, occOldX, occOldY);

                                    log.debug("Swapped {} with {}", pos, occupying);
                                });
                            }
                        }
                    }
                }
                continue;
            } else if (reserved.contains(nextKey)) {
                mov.path = AStar.findPath((int) pos.xPos, (int) pos.yPos, (int) mov.xDst, (int) mov.yDst);
                mov.pathIndex = 0;
                continue;
            }

            reserved.add(nextKey);
            int finalStepsTaken = stepsTaken;
            int[] finalNextStep = nextStep;
            actions.add(() -> {
                GlobalGrid grid = GlobalGrid.getInstance();

                grid.cellAt((int) pos.xPos, (int) pos.yPos).spriteCharacter = ' ';
                grid.setBlocked((int) pos.xPos, (int) pos.yPos, false);

                float oldX = pos.xPos;
                float oldY = pos.yPos;
                pos.xPos = finalNextStep[0];
                pos.yPos = finalNextStep[1];
                mov.pathIndex += finalStepsTaken;
                movedThisFrame.add(pos);

                grid.cellAt((int) pos.xPos, (int) pos.yPos).spriteCharacter = asciiSprite.spriteCharacter;
                updateSpriteAfterMove(sprite, pos, oldX, oldY);
                grid.setBlocked((int) pos.xPos, (int) pos.yPos, pos.blocksTile);

                if ((int) pos.xPos == (int) mov.xDst && (int) pos.yPos == (int) mov.yDst) {
                    completeMovement(world, entityId, mov);
                }
            });
        }

        actions.forEach(Runnable::run);
    }

    private static void updateSpriteAfterMove(TSSprite.Component sprite, Position.Component position,
                                              float oldX, float oldY) {
        if (sprite == null || position == null) {
            return;
        }
        CharacterSpriteAnimator.recordDelta(sprite, position.xPos - oldX, position.yPos - oldY);
        CharacterSpriteAnimator.syncSpritePosition(sprite, position);
    }

    private static void completeMovement(World world, int entityId, Movement.Component mov) {
        mov.wantsToMove = false;
        mov.path = null;
        mov.pathIndex = 0;

        Task.Component task = world.getComponent(entityId, Task.Component.class);
        if (task != null && task.status == RUNNING) {
            task.status = FINISHED;
        }
    }

    static class AStar {
        public static List<int[]> findPath(int sx, int sy, int gx, int gy) {
            PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(Node::f));
            Set<String> closed = new HashSet<>();
            open.add(new Node(sx, sy, 0, heuristic(sx, sy, gx, gy), null));

            while (!open.isEmpty()) {
                Node curr = open.poll();
                if (curr.x == gx && curr.y == gy) return reconstruct(curr);

                String key = curr.x + "," + curr.y;
                if (!closed.add(key)) continue;

                for (int[] d : directions()) {
                    int nx = curr.x + d[0];
                    int ny = curr.y + d[1];
                    if (!valid(nx, ny) || GlobalGrid.getInstance().isBlocked(nx, ny)) continue;

                    String nKey = nx + "," + ny;
                    if (closed.contains(nKey)) continue;

                    int g = curr.g + GlobalGrid.getInstance().cellAt(nx, ny).movementWeight;
                    int h = heuristic(nx, ny, gx, gy);
                    open.add(new Node(nx, ny, g, h, curr));
                }
            }
            return Collections.emptyList();
        }

        private static boolean valid(int x, int y) {
            return x >= 0 && y >= 0 && x < MAP_WIDTH && y < MAP_LENGTH;
        }

        private static List<int[]> reconstruct(Node node) {
            List<int[]> path = new ArrayList<>();
            Node current = node;
            while (current.parent != null) {
                path.add(0, new int[]{current.x, current.y});
                current = current.parent;
            }
            return path;
        }

        private static int heuristic(int x1, int y1, int x2, int y2) {
            return Math.abs(x1 - x2) + Math.abs(y1 - y2);
        }

        private static int[][] directions() {
            return new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        }

        private record Node(int x, int y, int g, int h, Node parent) {
            int f() {
                return g + h;
            }
        }
    }
}
