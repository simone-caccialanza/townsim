package org.annotationlib;

import jecs.core.ComponentBase;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public interface AnnotationUtils {

    List<String> FILTERED_CLASS_PATH_ENTRIES = Arrays.stream(System.getProperty("java.class.path")
                    .split(System.getProperty("path.separator")))
            .filter(p -> p.contains("town-simulator"))
            .toList();

    static Map<Class<? extends Annotation>, Collection<Class<?>>> loadClassesFromPackagesWithAnnotations(Collection<String> packagesPath, Collection<Class<? extends Annotation>> annotations) {
        Collection<Class<?>> targetClasses = getClassesInPackages(packagesPath);
        Map<Class<? extends Annotation>, Collection<Class<?>>> map = new HashMap<>();
        for (Class<? extends Annotation> annotation : annotations) {
            Collection<Class<?>> matchingClasses = new ArrayList<>();
            for (Class<?> clz : targetClasses) {
                if (clz.isAnnotationPresent(annotation)) {
                    matchingClasses.add(clz);
                }
            }
            map.put(annotation, matchingClasses);
        }
        return map;
    }

    static Map<Class<? extends Annotation>, Collection<Method>> loadMethodsFromPackagesWithAnnotations(Collection<String> packagesPath, Collection<Class<? extends Annotation>> annotations) {
        Collection<Class<?>> targetClasses = getClassesInPackages(packagesPath);
        Map<Class<? extends Annotation>, Collection<Method>> map = new HashMap<>();
        for (Class<? extends Annotation> annotation : annotations) {
            Collection<Method> matchingMethods = new ArrayList<>();
            for (Class<?> clz : targetClasses) {
                Arrays.stream(clz.getDeclaredMethods()).forEach(method -> {
                    if (method.isAnnotationPresent(annotation)) {
                        matchingMethods.add(method);
                    }
                });
            }
            map.put(annotation, matchingMethods);
        }
        return map;
    }

    // Unused code, here for the future
    @SuppressWarnings("unchecked")
    static Map<Class<?>, List<Class<? extends ComponentBase>>> loadClasses() {
        List<Class<?>> components = getClassesInPackage("org.townsimulator.components");
        List<Class<?>> tags = getClassesInPackage("org.townsimulator.tags");
        Map<Class<?>, List<Class<? extends ComponentBase>>> map = new HashMap<>();
        for (Class<?> tag : tags) {
            List<Class<? extends ComponentBase>> matchingComponents = new ArrayList<>();
            for (Class<?> component : components) {
                if (tag.isAssignableFrom(component)) {
                    matchingComponents.add((Class<? extends ComponentBase>) component);
                }
            }
            map.put(tag, matchingComponents);
        }
        return map;
    }

    /**
     * This is a magic function from stackoverflow that takes all files with .class extension in a package and returns a
     * list of those classes
     */
    static List<Class<?>> getClassesInPackage(String packageName) {
        String path = packageName.replace(".", File.separator);
        List<Class<?>> classes = new ArrayList<>();

        String name;
        for (String classpathEntry : FILTERED_CLASS_PATH_ENTRIES) {
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

    /**
     * Inefficient way to call the magic method above
     *
     * @param packagesName list of all packages to scan
     * @return list of all classes in all packages
     */
    static Collection<Class<?>> getClassesInPackages(Collection<String> packagesName) {
        return packagesName.stream().map(AnnotationUtils::getClassesInPackage).flatMap(List::stream).toList();
    }
}
