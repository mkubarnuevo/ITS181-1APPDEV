package finalproj.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class filehelper {

    public static String readLyrics(Path lyricsFilePath) throws IOException {
        return Files.readString(lyricsFilePath);
    }

    public static void saveFile(Path sourceFilePath, Path targetDir, String filename) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        Path targetFile = targetDir.resolve(filename);
        Files.copy(sourceFilePath, targetFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteFile(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
