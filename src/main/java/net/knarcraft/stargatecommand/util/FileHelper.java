package net.knarcraft.stargatecommand.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * A helper class for dealing with files
 */
public final class FileHelper {

    private FileHelper() {

    }

    /**
     * Gets a buffered reader for
     *
     * @return <p>A buffered read for reading the file</p>
     * @throws FileNotFoundException <p>If unable to get an input stream for the given file</p>
     */
    public static BufferedReader getBufferedReaderForInternalFile(String file) throws FileNotFoundException {
        InputStream inputStream = FileHelper.class.getResourceAsStream(file);
        if (inputStream == null) {
            throw new FileNotFoundException("Unable to read the given file");
        }
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

}
