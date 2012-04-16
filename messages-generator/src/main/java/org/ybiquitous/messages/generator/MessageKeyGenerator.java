package org.ybiquitous.messages.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.ybiquitous.messages.Constants;
import org.ybiquitous.messages.Utils;

public final class MessageKeyGenerator {

    public static final class Parameter {

        public String packageName;
        public String className;
        public String description;
        public File messageResourceFile;
        public Charset messageResourceFileEncoding;
        public File templateFile;
        public Charset templateFileEncoding;
        public File outputDirectory;
        public Charset outputEncoding;
        public List<Class<?>> importClasses;

        private URL messageResourceUrl;
        private URL templateUrl;

        public void initialize() {
            if (this.packageName == null) {
                this.packageName = "message";
            }
            if (this.className == null) {
                this.className = "MessageKeys";
            }
            if (this.description == null) {
                this.description = "This is an auto generated class.";
            }
            if (this.messageResourceFile == null) {
                this.messageResourceUrl = getClass().getResource(
                        '/' + Constants.DEFAULT_MESSAGE_RESOURCE);
            } else {
                this.messageResourceUrl = toURL(this.messageResourceFile);
            }
            if (this.messageResourceFileEncoding == null) {
                this.messageResourceFileEncoding = Constants.DEFAULT_CHARSET;
            }
            if (this.templateFile == null) {
                this.templateUrl = getClass().getResource("MessageKeys.vm");
            } else {
                this.templateUrl = toURL(this.templateFile);
            }
            if (this.templateFileEncoding == null) {
                this.templateFileEncoding = Constants.DEFAULT_CHARSET;
            }
            if (this.outputDirectory == null) {
                this.outputDirectory = new File(".");
            }
            if (this.outputEncoding == null) {
                this.outputEncoding = Constants.DEFAULT_CHARSET;
            }
            if (this.importClasses == null) {
                this.importClasses = new ArrayList<Class<?>>();
            }
        }

        public void validate() throws IllegalArgumentException {
            final StringBuilder error = new StringBuilder();
            final String separator = "\n";

            String tempError = validateFile(this.messageResourceFile);
            if (tempError != null) {
                error.append(separator);
                error.append(tempError);
            }

            tempError = validateFile(this.templateFile);
            if (tempError != null) {
                error.append(separator);
                error.append(tempError);
            }

            if (error.length() > 0) {
                throw new IllegalArgumentException(error.toString());
            }
        }

        private static String validateFile(File file) {
            if (file != null && !file.isFile()) {
                return file + " not found";
            }
            return null;
        }

        private static URL toURL(File file) {
            Utils.notNull(file, "file");
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(file.toString(), e);
            }
        }
    }

    public static File generate(Parameter parameter) {
        Utils.notNull(parameter, "parameter");
        parameter.initialize();
        parameter.validate();

        try {
            final Properties messageResource = loadMessageResource(parameter);
            return renderTemplate(parameter, messageResource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Properties loadMessageResource(Parameter parameter)
            throws IOException {
        final InputStreamReader reader = new InputStreamReader(
                parameter.messageResourceUrl.openStream(),
                parameter.messageResourceFileEncoding);
        try {
            final Properties result = new Properties();
            result.load(reader);
            return result;
        } finally {
            reader.close();
        }
    }

    private static File buildJavaFile(Parameter parameter) {
        final File dir;
        if (parameter.packageName == null) {
            dir = parameter.outputDirectory;
        } else {
            dir = new File(parameter.outputDirectory,
                    parameter.packageName.replace('.', File.separatorChar));
        }
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException("failed mkdir " + dir);
            }
        }
        return new File(dir, parameter.className + ".java");
    }

    private static File renderTemplate(Parameter parameter,
            Properties messageResource) throws Exception {

        final VelocityEngine engine = new VelocityEngine();
        engine.init(new VelocityConfig(parameter.templateUrl));

        final Template template = engine.getTemplate(
                getLastSegment(parameter.templateUrl),
                parameter.templateFileEncoding.name());

        final File outputFile = buildJavaFile(parameter);
        final BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile),
                        parameter.outputEncoding));
        try {
            template.merge(buildContext(parameter, messageResource), writer);
        } finally {
            writer.close();
        }
        return outputFile;
    }

    private static String getLastSegment(URL url) {
        String urlStr = url.toString();
        return urlStr.substring(urlStr.lastIndexOf('/'));
    }

    private static VelocityContext buildContext(Parameter parameter,
            Properties messageResource) {
        final VelocityContext context = new VelocityContext();
        context.put("packageName", parameter.packageName);
        context.put("className", parameter.className);
        context.put("description", parameter.description);
        context.put("generator", MessageKeyGenerator.class.getCanonicalName());
        context.put("timestamp", getTimestamp());
        context.put("messageKeys", buildMessageKeys(messageResource));
        context.put("importClasses", parameter.importClasses);
        context.put(
                "messageResource",
                getLastSegment(parameter.messageResourceUrl)
                    .replace('.' + Constants.DEFAULT_MESSAGE_RESOURCE_EXTENSION, "")
                    .replaceFirst("/", ""));
        return context;
    }

    private static String getTimestamp() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(cal.getTimeZone());
        return sdf.format(cal.getTime());
    }

    private static final Pattern REPLACE_PATTERN = Pattern
            .compile("[.\\-\\[\\]]");
    private static final String REPLACE_STRING = "_";

    private static List<Map<String, Object>> buildMessageKeys(
            Properties messageResource) {

        final List<String> keys = new ArrayList<String>(
                messageResource.stringPropertyNames());
        Collections.sort(keys);

        final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(
                messageResource.size());
        for (String key : keys) {
            final Map<String, Object> keyInfo = new HashMap<String, Object>(2);
            keyInfo.put("real", key);
            keyInfo.put("identifier",
                    REPLACE_PATTERN.matcher(key).replaceAll(REPLACE_STRING));
            result.add(keyInfo);
        }
        return result;
    }
}
