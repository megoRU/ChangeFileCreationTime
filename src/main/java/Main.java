import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        String path;
        if (args.length >= 1) {
            path = args[0];
        } else {
            throw new IllegalArgumentException("args[0] is NULL");
        }
        updateFiles(path);
    }

    public static void updateFiles(String path) throws IOException {
        try (Stream<Path> files = Files.list(Paths.get(path))) {
            files.forEach(paths -> {
                try {
                    BasicFileAttributes attr = Files.readAttributes(paths, BasicFileAttributes.class);
                    FileTime lastModifiedTime = attr.lastModifiedTime();
                    FileTime time = FileTime.fromMillis(lastModifiedTime.toMillis());
                    Files.setAttribute(paths, "creationTime", time);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
