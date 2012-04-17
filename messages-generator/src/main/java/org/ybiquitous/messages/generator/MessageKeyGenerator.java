package org.ybiquitous.messages.generator;

import static org.ybiquitous.messages.Constants.DEFAULT_CHARSET;
import static org.ybiquitous.messages.Constants.DEFAULT_MESSAGE_RESOURCE;
import static org.ybiquitous.messages.Constants.DEFAULT_MESSAGE_RESOURCE_EXTENSION;
import static org.ybiquitous.messages.Utils.notNull;
import static org.ybiquitous.messages.Utils.notNullOrElse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import org.ybiquitous.messages.Factory;

public final class MessageKeyGenerator {

    public static final class Parameter {

        public String packageName;
        public String className;
        public String description;
        public URL messageResource;
        public Charset messageResourceEncoding;
        public URL template;
        public Charset templateEncoding;
        public File outputDirectory;
        public Charset outputEncoding;
        public List<Class<?>> importClasses;

        private static final Factory<URL> MESSAGE_RESOURCE_FACTORY = new Factory<URL>() {
            @Override
            public URL get() {
                return Parameter.class.getResource('/' + DEFAULT_MESSAGE_RESOURCE);
            }
        };

        private static final Factory<URL> TEMPLATE_FACTORY = new Factory<URL>() {
            @Override
            public URL get() {
                return Parameter.class.getResource("MessageKeys.vm");
            }
        };

        private static final Factory<File> OUTPUT_DIRECTORY_FACTORY = new Factory<File>() {
            @Override
            public File get() {
                return new File(".");
            }
        };

        public void initialize() {
            this.packageName = notNullOrElse(this.packageName, "message");
            this.className = notNullOrElse(this.className, "MessageKeys");
            this.description = notNullOrElse(this.description, "This is an auto generated class.");
            this.messageResource = notNullOrElse(this.messageResource, MESSAGE_RESOURCE_FACTORY);
            this.messageResourceEncoding = notNullOrElse(this.messageResourceEncoding, DEFAULT_CHARSET);
            this.template = notNullOrElse(this.template, TEMPLATE_FACTORY);
            this.templateEncoding = notNullOrElse(this.templateEncoding, DEFAULT_CHARSET);
            this.outputDirectory = notNullOrElse(this.outputDirectory, OUTPUT_DIRECTORY_FACTORY);
            this.outputEncoding = notNullOrElse(this.outputEncoding, DEFAULT_CHARSET);
            this.importClasses = notNullOrElse(this.importClasses, Collections.<Class<?>> emptyList());
        }
    }

    public static File generate(Parameter parameter) {
        notNull(parameter, "parameter");
        parameter.initialize();

        try {
            final Properties messageResource = loadMessageResource(parameter);
            return renderTemplate(parameter, messageResource);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Properties loadMessageResource(Parameter parameter)
            throws IOException {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                parameter.messageResource.openStream(),
                parameter.messageResourceEncoding));
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
        engine.init(new VelocityConfig(parameter.template));

        final Template template = engine.getTemplate(
                getLastSegment(parameter.template),
                parameter.templateEncoding.name());

        final VelocityContext context = buildContext(parameter, messageResource);

        final File outputFile = buildJavaFile(parameter);
        final BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile),
                        parameter.outputEncoding));
        try {
            template.merge(context, writer);
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
                getLastSegment(parameter.messageResource)
                    .replace('.' + DEFAULT_MESSAGE_RESOURCE_EXTENSION, "")
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
