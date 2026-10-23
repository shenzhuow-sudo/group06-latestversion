package ca.uwo.cs2212.demo;

/**
 * Abstraction over a text-based persistence mechanism.
 * <p>
 * The persistence layer can read and write raw JSON/text data using a
 * {@code path} and a {@code data} payload.
 * </p>
 *
 * @author Qixuan Zhu
 */
public interface StorageService {

    /**
     * Reads persisted data from the given path.
     *
     * @param path location identifier used by the underlying storage
     * @return stored content as a string
     */
    String read(String path);

    /**
     * Writes persisted data to the given path.
     *
     * @param path location identifier used by the underlying storage
     * @param data content to write
     */
    void write(String path, String data);
}

