package ca.uwo.cs2212.demo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * {@link StorageService} implementation that reads and writes UTF-8 text files on the local filesystem.
 *
 * @author Qixuan Zhu
 */
public class FileStorageService implements StorageService {
    private static final Path APPLICATION_ROOT = locateApplicationRoot();

    /**
     * Reads the entire file at {@code path} as a UTF-8 string.
     * <p>
     * Relative paths are resolved from the detected project/application root
     * instead of the JVM working directory. If the file does not exist,
     * returns an empty string.
     * </p>
     *
     * @param path filesystem path
     * @return file contents, or empty string if missing
     * @throws UncheckedIOException if an I/O error occurs other than missing file
     */
    @Override
    public String read(String path) {
        Path p = resolvePath(path);
        if (!Files.isRegularFile(p)) {
            return "";
        }
        try {
            return Files.readString(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Writes {@code data} to {@code path}, replacing any existing file.
     * <p>
     * Relative paths are resolved from the detected project/application root.
     * Creates parent directories when present. If {@code path} has no parent,
     * writes to the resolved path as given.
     * </p>
     *
     * @param path filesystem path
     * @param data content to write
     * @throws UncheckedIOException if an I/O error occurs
     */
    @Override
    public void write(String path, String data) {
        Path p = resolvePath(path);
        try {
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }
            Files.writeString(
                    p,
                    data == null ? "" : data,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Resolves an absolute path directly, or anchors a relative path at the
     * detected application root.
     *
     * @param path caller-provided storage path
     * @return normalized filesystem path
     */
    private static Path resolvePath(String path) {
        Path candidate = Paths.get(path).normalize();
        if (candidate.isAbsolute()) {
            return candidate;
        }
        return APPLICATION_ROOT.resolve(candidate).normalize();
    }

    /**
     * Attempts to locate the project root so persisted JSON files remain stable
     * when the IDE launches the app with a different working directory.
     *
     * @return best-effort application root directory
     */
    private static Path locateApplicationRoot() {
        Path userDir = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        Path discoveredFromUserDir = findProjectRoot(userDir);
        if (discoveredFromUserDir != null) {
            return discoveredFromUserDir;
        }

        try {
            Path codeSource = Paths.get(FileStorageService.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).toAbsolutePath().normalize();
            Path discoveredFromCodeSource = findProjectRoot(codeSource);
            if (discoveredFromCodeSource != null) {
                return discoveredFromCodeSource;
            }
        } catch (URISyntaxException | NullPointerException ignored) {
            // Fall back to user.dir when the runtime location is unavailable.
        }

        return userDir;
    }

    /**
     * Walks upward from {@code start} looking for a directory that appears to
     * be the repository/application root.
     *
     * @param start starting filesystem location
     * @return detected root directory, or {@code null} if none is found
     */
    private static Path findProjectRoot(Path start) {
        Path current = Files.isDirectory(start) ? start : start.getParent();
        while (current != null) {
            boolean hasRepoMarker = Files.exists(current.resolve(".git"))
                    || Files.exists(current.resolve("The hunt.iml"));
            boolean hasGameData = Files.exists(current.resolve("src/main/resources/levels.json"))
                    || Files.exists(current.resolve("data/players.json"))
                    || Files.exists(current.resolve("levels.json"))
                    || Files.exists(current.resolve("players.json"));
            if (hasRepoMarker || hasGameData) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }
}
