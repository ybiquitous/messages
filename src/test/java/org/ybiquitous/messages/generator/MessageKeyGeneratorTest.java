package org.ybiquitous.messages.generator;

import static org.junit.Assert.fail;

import java.io.File;

import javax.tools.ToolProvider;

import org.junit.Test;
import org.ybiquitous.messages.generator.MessageKeyGenerator.Parameter;

public class MessageKeyGeneratorTest {

    @Test
    public void compileCheck() throws Exception {
        Parameter parameter = new Parameter();
        parameter.outputDirectory = new File("target/unittest");
        File source = MessageKeyGenerator.generate(parameter);
        int rtn = ToolProvider.getSystemJavaCompiler().run(null, null, null,
                source.getAbsolutePath());
        if (rtn != 0) {
            fail();
        }
    }
}
