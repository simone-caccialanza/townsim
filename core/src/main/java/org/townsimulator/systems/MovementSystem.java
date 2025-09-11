package org.townsimulator.systems;

import jecs.core.Archetype;
import jecs.core.ArchetypeManager;
import jecs.core.system.ECSSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.townsimulator.GlobalGrid;
import org.townsimulator.components.Movement;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;

import java.util.*;
import java.util.stream.Collectors;

import static jecs.core.utils.GlobalConstants.MAP_LENGTH;
import static jecs.core.utils.GlobalConstants.MAP_WIDTH;

public class MovementSystem extends ECSSystem<MovementSystem> {
    private static final Logger log = LoggerFactory.getLogger(MovementSystem.class);

    private MovementSystem() {
        super(MovementSystem.class);
    }

    public static MovementSystem getInstance() {
        return getInstanceOf(MovementSystem.class);
    }

    @Override
    public void run() {
        Set<Archetype> archetypes = ArchetypeManager.allArchetypesWithType(
            Position.Component.class, Movement.Component.class, SpriteASCII.Component.class).stream()
            .filter(a -> !a.isEmpty()).collect(Collectors.toSet());

        Map<String, Position.Component> positionMap = new HashMap<>();
        Map<Position.Component, Movement.Component> movementMap = new HashMap<>();
        Map<Position.Component, SpriteASCII.Component> spriteMap = new HashMap<>();

        for (Archetype archetype : archetypes) {
            var positions = archetype.getComponentsOfType(Position.Component.class);
            var movements = archetype.getComponentsOfType(Movement.Component.class);
            var sprites = archetype.getComponentsOfType(SpriteASCII.Component.class);

            for (int i = 0; i < positions.size(); i++) {
                Position.Component pos = positions.get(i);
                Movement.Component mov = movements.get(i);
                SpriteASCII.Component spr = sprites.get(i);
                String key = (int) pos.xPos + "," + (int) pos.yPos;
                positionMap.put(key, pos);
                movementMap.put(pos, mov);
                spriteMap.put(pos, spr);
            }
        }

        List<Runnable> actions = new ArrayList<>();
        Set<Position.Component> movedThisFrame = new HashSet<>();
        Set<String> reserved = new HashSet<>();
        Set<Set<Position.Component>> swapsPerformed = new HashSet<>();

        for (Archetype archetype : archetypes) {
            var positions = archetype.getComponentsOfType(Position.Component.class);
            var movements = archetype.getComponentsOfType(Movement.Component.class);
            var sprites = archetype.getComponentsOfType(SpriteASCII.Component.class);

            for (int i = 0; i < positions.size(); i++) {
                Position.Component pos = positions.get(i);
                Movement.Component mov = movements.get(i);
                SpriteASCII.Component sprite = sprites.get(i);

                if (!mov.wantsToMove || movedThisFrame.contains(pos)) continue;

                if ((int) pos.xPos == (int) mov.xDst && (int) pos.yPos == (int) mov.yDst) {
                    mov.wantsToMove = false;
                    mov.path = null;
                    mov.pathIndex = 0;
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

                                        int tmpX = (int) pos.xPos, tmpY = (int) pos.yPos;
                                        pos.xPos = occupying.xPos;
                                        pos.yPos = occupying.yPos;
                                        occupying.xPos = tmpX;
                                        occupying.yPos = tmpY;

                                        mov.pathIndex += finalStepsTaken;
                                        theirMov.pathIndex += 1;
                                        movedThisFrame.add(pos);
                                        movedThisFrame.add(occupying);

                                        SpriteASCII.Component theirSprite = spriteMap.get(occupying);

                                        grid.cellAt((int) pos.xPos, (int) pos.yPos).spriteCharacter = sprite.spriteCharacter;
                                        grid.setBlocked((int) pos.xPos, (int) pos.yPos, pos.blocksTile);

                                        grid.cellAt((int) occupying.xPos, (int) occupying.yPos).spriteCharacter = theirSprite.spriteCharacter;
                                        grid.setBlocked((int) occupying.xPos, (int) occupying.yPos, occupying.blocksTile);

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

                    pos.xPos = finalNextStep[0];
                    pos.yPos = finalNextStep[1];
                    mov.pathIndex += finalStepsTaken;
                    movedThisFrame.add(pos);

                    grid.cellAt((int) pos.xPos, (int) pos.yPos).spriteCharacter = sprite.spriteCharacter;
                    grid.setBlocked((int) pos.xPos, (int) pos.yPos, pos.blocksTile);

                    if ((int) pos.xPos == (int) mov.xDst && (int) pos.yPos == (int) mov.yDst) {
                        mov.wantsToMove = false;
                        mov.path = null;
                        mov.pathIndex = 0;
                    }
                });
            }
        }

        actions.forEach(Runnable::run);

        positionMap.clear();
        movementMap.clear();
        spriteMap.clear();
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
