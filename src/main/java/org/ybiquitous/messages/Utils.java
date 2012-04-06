package org.ybiquitous.messages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Utils {

    public static <T> T notNull(T obj, String name) throws NullPointerException {
        if (obj == null) {
            if (name == null) {
                throw new NullPointerException();
            } else {
                throw new NullPointerException(name + " is required");
            }
        }
        return obj;
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.isEmpty() || str.trim().isEmpty());
    }

    public static List<String> readLines(File file, Charset charset)
            throws IOException {

        notNull(file, "file");
        notNull(charset, "charset");

        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), charset));
        try {
            final List<String> lines = new ArrayList<String>();
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            return lines;
        } finally {
            reader.close();
        }
    }

    public static void writeLines(List<String> lines, File file, Charset charset)
            throws IOException {

        notNull(lines, "lines");
        notNull(file, "file");
        notNull(charset, "charset");

        final BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), charset));
        try {
            for (String line : lines) {
                writer.append(line);
            }
        } finally {
            writer.close();
        }
    }

    public static File getResourceFile(Class<?> clazz, String name) {
        notNull(clazz, "clazz");
        notNull(name, "name");

        final URL resource = clazz.getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException(name + " not found");
        }

        try {
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Utils() {
    }
}
