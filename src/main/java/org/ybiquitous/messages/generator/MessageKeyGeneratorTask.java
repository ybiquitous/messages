package org.ybiquitous.messages.generator;

import java.io.File;
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

    private String packageName;
    private String className;
    private String description;
    private File messageResourceFile;
    private String messageResourceFileEncoding;
    private File templateFile;
    private String templateFileEncoding;
    private File outputDirectory;
    private String outputEncoding;

    private final List<ImportClass> importClasses = new ArrayList<ImportClass>();

    @Override
    public void execute() throws BuildException {
        Parameter parameter = new Parameter();
        parameter.packageName = this.packageName;
        parameter.className = this.className;
        parameter.description = this.description;
        parameter.messageResourceFile = this.messageResourceFile;
        parameter.messageResourceFileEncoding = encoding(this.messageResourceFileEncoding);
        parameter.templateFile = this.templateFile;
        parameter.templateFileEncoding = encoding(this.templateFileEncoding);
        parameter.outputDirectory = this.outputDirectory;
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

    public void setMessageResourceFile(File messageResourceFile) {
        this.messageResourceFile = messageResourceFile;
    }

    public void setMessageResourceFileEncoding(
            String messageResourceFileEncoding) {
        this.messageResourceFileEncoding = messageResourceFileEncoding;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    public void setTemplateFileEncoding(String templateFileEncoding) {
        this.templateFileEncoding = templateFileEncoding;
    }

    public void setOutputDirectory(File outputDirectory) {
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
