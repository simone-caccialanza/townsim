package org.townsimulator;

import jecs.core.*;
import org.annotationlib.annotations.LogField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.townsimulator.components.*;
import org.townsimulator.game.loader.TSGameLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static org.townsimulator.game.logic.GameLogicStore.BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY;
import static org.townsimulator.game.logic.GameLogicStore.BASE_LOGIC_MOVEMENT_ONLY;
import static org.townsimulator.game.loop.GameLoopStore.BASE_LOOP;


class MainTest {



    @BeforeAll
    static void beforeAll() {
        System.setProperty("logLevel", "DEBUG");
    }

    private static List<Class<?>> getClassesInPackage(String packageName) {
        String path = packageName.replace(".", File.separator);
        List<Class<?>> classes = new ArrayList<>();
        String[] classPathEntries = System.getProperty("java.class.path").split(
                System.getProperty("path.separator")
        );

        String name;
        for (String classpathEntry : classPathEntries) {
            if (classpathEntry.endsWith(".jar")) {
                File jar = new File(classpathEntry);
                try {
                    JarInputStream is = new JarInputStream(new FileInputStream(jar));
                    JarEntry entry;
                    while ((entry = is.getNextJarEntry()) != null) {
                        name = entry.getName();
                        if (name.endsWith(".class")) {
                            if (name.contains(path) && name.endsWith(".class")) {
                                String classPath = name.substring(0, entry.getName().length() - 6);
                                classPath = classPath.replaceAll("[\\|/]", ".");
                                classes.add(Class.forName(classPath));
                            }
                        }
                    }
                } catch (Exception ex) {
                    // Silence is gold
                }
            } else {
                try {
                    File base = new File(classpathEntry + File.separatorChar + path);
                    for (File file : base.listFiles()) {
                        name = file.getName();
                        if (name.endsWith(".class")) {
                            name = name.substring(0, name.length() - 6);
                            classes.add(Class.forName(packageName + "." + name));
                        }
                    }
                } catch (Exception ex) {
                    // Silence is gold
                }
            }
        }

        return classes;
    }

    @Test
    void dosomenthing() {

        EntityManager.createEntity(
                new Movement.Component(1, 2, 7, 8),
                new Position.Component(0, 0),
                new Hunger.Component(1, 9.97f),
                new SpriteASCII.Component('E')
        );
        EntityManager.createEntity(
                new Position.Component(7, 8),
                new SpriteASCII.Component('B')
        );


        var gLogic = new TSGameLogic(BASE_LOGIC_MOVEMENT_ONLY);

        var gLoop = new TSGameLoop(BASE_LOOP, gLogic);

        GlobalGrid.getInstance().bindSprites();
        var gLoader = new TSGameLoader();
        var game = new Game(gLoader, gLoop);
        game.start();

    }

    @Test
    void hungerSystemAddGoToTaskAndEntityGoesToDestination() {

        EntityManager.createEntity(
                new Movement.Component(0, 0),
                new Position.Component(0, 0),
                new Hunger.Component(1, 9.97f),
                new SpriteASCII.Component('E')
        );
        EntityManager.createEntity(
                new Position.Component(7, 8),
                new SpriteASCII.Component('B'),
                new FoodProvider.Component(3)
        );


        var gLogic = new TSGameLogic(BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY);

        var gLoop = new TSGameLoop(BASE_LOOP, gLogic);

        GlobalGrid.getInstance().bindSprites();
        var gLoader = new TSGameLoader();
        var game = new Game(gLoader, gLoop);
        game.start();

    }

    @Test
    void collisions() {

        EntityManager.createEntity(
                new Movement.Component(1, 1, 7, 1),
                new Position.Component(0, 1, true),
                new SpriteASCII.Component('A')
        );
        EntityManager.createEntity(
                new Movement.Component(1, 1, 1, 1),
                new Position.Component(8, 1, true),
                new SpriteASCII.Component('B')
        );


        var gLogic = new TSGameLogic(BASE_LOGIC_MOVEMENT_ONLY);

        var gLoop = new TSGameLoop(BASE_LOOP, gLogic);

        GlobalGrid.getInstance().bindSprites();
        GlobalGrid.getInstance().cellAt(2, 2).movementWeight = 1;
        GlobalGrid.getInstance().cellAt(2, 3).movementWeight = 1;
        GlobalGrid.getInstance().cellAt(3, 3).movementWeight = 1;
        GlobalGrid.getInstance().cellAt(4, 3).movementWeight = 1;
        GlobalGrid.getInstance().cellAt(5, 3).movementWeight = 1;

        var gLoader = new TSGameLoader();
        var game = new Game(gLoader, gLoop);
        game.start();

    }

    @Test
    void testVectors() {
        var a1 = new A();
        var a2 = new A();
        var a4 = new A();
        var b1 = new B();
        var b2 = new B();
        var b3 = new B();
        Map<Class<? extends ComponentBase>, Vector<? extends ComponentBase>> components = new HashMap<>(10);
        components.put(A.class, new Vector<>(List.of(a1, a2, a4)));
        components.put(B.class, new Vector<>(List.of(b1, b2, b3)));

        var v = new Vector<String>(10, 10);
        v.ensureCapacity(10);
        v.addAll(Collections.nCopies(10, null));
        v.set(1, "hello");
    }

    @Test
    void testGenerics() {
        var a = new A();
        var b = new B();
        List<ComponentBase> l = List.of(a, b);
        System.out.println("------------------------------------------------------");
        System.out.println(l.get(0));
        System.out.println(l.get(0).getClass());
        System.out.println(l.get(0) instanceof A);
        System.out.println(l.get(0) instanceof B);
        System.out.println(l.get(0) instanceof ComponentBase);
        System.out.println(l.get(0).castToChild(l.get(0)).getClass());
        System.out.println("------------------------------------------------------");
    }


    @Test
    void reflectionsTest() {

        List<Class<?>> components = getClassesInPackage("org.townsimulator.component");
        List<Class<?>> tags = getClassesInPackage("org.townsimulator.tags");
        Map<Class<?>, List<Class<?>>> map = new HashMap<>();

        for (Class<?> tag : tags) {
            List<Class<?>> matchingComponents = components.stream()
                    .filter(tag::isAssignableFrom)
                    .toList();
            map.put(tag, matchingComponents);
        }

        for (Map.Entry<Class<?>, List<Class<?>>> entry : map.entrySet()) {
            System.out.println("Tag: " + entry.getKey().getSimpleName());
            for (Class<?> component : entry.getValue()) {
                System.out.println("  Component: " + component.getName());
            }
        }
    }

    @Test
    void customFieldsLogging() {
        System.out.println(new Age.Component(1));
    }

    @Test
    void allArchetypes() {
        EntityManager.createEntity(
                new Age.Component(40),
                new Name.Component("Simone", "sss"),
                new Hunger.Component()
        );
        EntityManager.createEntity(
                new Age.Component(30),
                new Name.Component("Albano", "aaa")
        );
        EntityManager.createEntity(
                new Age.Component(20),
                new Hunger.Component()
        );
        EntityManager.createEntity(
                new Grid.Component(100, 100)
        );

        var result = ArchetypeManager.allArchetypesWithType(Age.Component.class, Name.Component.class);
        System.out.println(result);

//        GameLoop.run();
    }

    @Test
    void testRetainAllInSets() {
        var a = new A();
        var b = new B();
        var c = new C();
        var s1 = new HashSet<>(List.of(a, b, c));
        var s2 = new HashSet<>(List.of(b, c));
        s1.retainAll(s2);
        Assertions.assertTrue(s1.containsAll(List.of(b, c)));
    }

    @Test
    void packagesInDirectory() throws IOException {
//        System.out.println(GameLoader.getPackages(Paths.get("org.townsimulator")));

    }

    @Test
    void gameLoader() {
    }

    private void printMap(Map<Vector<Class<? extends ComponentBase>>, Archetype> map) {
        map.forEach((key, value) -> System.out.println(key + " -> " + value));
    }

    public static class TSGameLogic extends GameLogic {
        protected TSGameLogic(Runnable runnableLogic) {
            super(runnableLogic);
        }
    }

    public static class TSGameLoop extends GameLoop {
        protected TSGameLoop(Consumer<GameLogic> runnableLoopLogic, GameLogic gameLogic) {
            super(runnableLoopLogic, gameLogic);
        }
    }

    private static class A extends ComponentBase {
        @LogField
        private final String name = "A";

        public static int tableIndex() {
            return 0;
        }

        @Override
        protected void reset() {

        }

        @Override
        public A castToChild(ComponentBase componentBase) {
            return (A) componentBase;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return A.class.getName().compareTo(o.getClass().getName());
        }
    }

    private static class B extends ComponentBase {
        @LogField
        private final String name = "B";

        public static int tableIndex() {
            return 1;
        }

        @Override
        protected void reset() {

        }

        @Override
        public B castToChild(ComponentBase componentBase) {
            return null;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return B.class.getName().compareTo(o.getClass().getName());
        }
    }

    private static class C extends ComponentBase {
        @LogField
        private final String name = "C";

        public static int tableIndex() {
            return 2;
        }

        @Override
        protected void reset() {

        }

        @Override
        public C castToChild(ComponentBase componentBase) {
            return null;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return C.class.getName().compareTo(o.getClass().getName());
        }
    }
}
