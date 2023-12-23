package net.knarcraft.stargateinterfaces.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * Converts the stream directly into a string, includes the newline character
     *
     * @param stream <p> The stream to read from </p>
     * @return <p> A String of the file read </p>
     * @throws IOException <p>If unable to read the stream</p>
     */
    public static String readStreamToString(InputStream stream) throws IOException {
        BufferedReader reader = org.sgrewritten.stargate.util.FileHelper.getBufferedReaderFromInputStream(stream);
        String line = reader.readLine();
        StringBuilder lines = new StringBuilder();
        while (line != null) {
            lines.append(line).append("\n");
            line = reader.readLine();
        }
        return lines.toString();
    }
}
