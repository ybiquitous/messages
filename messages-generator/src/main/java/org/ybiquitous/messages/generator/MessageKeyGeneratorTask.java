package org.ybiquitous.messages.generator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.ybiquitous.messages.generator.MessageKeyGenerator.Parameter;

public final class MessageKeyGeneratorTask extends Task {

    private static Charset encoding(String encoding) {
        return (encoding == null) ? null : Charset.forName(encoding);
    }

    private static URL url(String resource) {
        if (resource == null) {
            return null;
        }
        try {
            return new URL(resource);
        } catch (MalformedURLException e) {
            throw new BuildException(e);
        }
    }

    private String packageName;
    private String className;
    private String description;
    private String messageResourceUrl;
    private String messageResourceEncoding;
    private String templateUrl;
    private String templateEncoding;
    private String outputDirectory;
    private String outputEncoding;

    private final List<ImportClass> importClasses = new ArrayList<ImportClass>();

    @Override
    public void execute() throws BuildException {
        Parameter parameter = new Parameter();
        parameter.packageName = this.packageName;
        parameter.className = this.className;
        parameter.description = this.description;
        parameter.messageResource = url(this.messageResourceUrl);
        parameter.messageResourceEncoding = encoding(this.messageResourceEncoding);
        parameter.template = url(this.templateUrl);
        parameter.templateEncoding = encoding(this.templateEncoding);
        parameter.outputDirectory = new File(this.outputDirectory.toString());
        parameter.outputEncoding = encoding(this.outputEncoding);
        parameter.importClasses = convertImportClasses(this.importClasses);

        File generated = MessageKeyGenerator.generate(parameter);
        log("generated " + generated);
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMessageResourceUrl(String messageResourceUrl) {
        this.messageResourceUrl = messageResourceUrl;
    }

    public void setMessageResourceEncoding(String messageResourceEncoding) {
        this.messageResourceEncoding = messageResourceEncoding;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setOutputEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    public ImportClass createImportClass() {
        ImportClass result = new ImportClass();
        this.importClasses.add(result);
        return result;
    }

    private static List<Class<?>> convertImportClasses(List<ImportClass> classes) {
        List<Class<?>> result = new ArrayList<Class<?>>(classes.size());
        for (ImportClass clz : classes) {
            result.add(clz.getName());
        }
        return result ;
    }

    public static final class ImportClass {
        private Class<?> name;
        public void setName(Class<?> name) {
            this.name = name;
        }
        public Class<?> getName() {
            return this.name;
        }
    }
}
