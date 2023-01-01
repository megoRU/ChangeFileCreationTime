import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TestChangeCreatedTime {

    private static final Map<Path, BasicFileAttributes> listPathWithTime = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        String path = "src/test/resources";
        try (Stream<Path> files = Files.list(Paths.get(path))) {
            files.forEach(paths -> {
                try {
                    BasicFileAttributes attr = Files.readAttributes(paths, BasicFileAttributes.class);
                    listPathWithTime.put(paths, attr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println(listPathWithTime.size());
    }

    @Test
    void changeCreatedTime() throws IOException {
        String[] args = {"src/test/resources"};
        Main.main(args);
    }

    @AfterAll
    static void done() throws IOException {
        String path = "src/test/resources";
        try (Stream<Path> files = Files.list(Paths.get(path))) {
            files.forEach(paths -> {
                BasicFileAttributes basicFileAttributes = listPathWithTime.get(paths);
                try {
                    FileTime lastModified = FileTime.fromMillis(basicFileAttributes.lastModifiedTime().toMillis());
                    FileTime lastCreated = FileTime.fromMillis(basicFileAttributes.creationTime().toMillis());
                    Files.setAttribute(paths, "creationTime", lastCreated);
                    Files.setLastModifiedTime(paths, lastModified);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("Done");
    }

}
