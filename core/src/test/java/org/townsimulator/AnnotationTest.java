package org.townsimulator;

import org.annotationlib.ClassAnnotationProcessor;
import org.annotationlib.annotations.HasInit;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class AnnotationTest {

    @Test
    void annotationProcessorShouldProcess() {
        ClassAnnotationProcessor ap = new ClassAnnotationProcessor();
        ap.addAnnotationAndActions(new ClassAnnotationProcessor.Property(HasInit.class, c -> System.out.println("HAS INIT"), Set.of("org.townsimulator", "org.townsimulator.clocks")));
        ap.process();
    }

    @Test
    void packageLib() {
        Package[] packages = Package.getPackages();
        Arrays.stream(packages).sorted(Comparator.comparing(Package::getName)).forEach(p -> System.out.println(p));
    }

    @Test
    void packageLibNIO() {
        Path startPath = Paths.get(System.getProperty("user.dir"));
        try {
            List<Path> directories = Files.walk(startPath)
                    .filter(Files::isDirectory)
                    .toList();

            directories
                    .stream()
                    .filter(dir -> dir.toString().contains("src\\main\\java\\org\\townsimulator"))
                    .map(p -> p.toString().substring(p.toString().indexOf("org\\townsimulator")))
                    .map(dir -> dir.replace("\\", "."))
                    .forEach(dir -> System.out.println(dir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void aaa() {
        Arrays.stream(System.getProperty("java.class.path").split(
                System.getProperty("path.separator")
        ));
    }
}
