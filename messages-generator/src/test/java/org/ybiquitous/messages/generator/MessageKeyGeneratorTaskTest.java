package org.ybiquitous.messages.generator;

import java.io.File;

import org.apache.tools.ant.BuildFileTest;

public class MessageKeyGeneratorTaskTest extends BuildFileTest {

    private String basedir;

    @Override
    protected void setUp() throws Exception {
        configureProject("target/test-classes/build.xml");
        this.basedir = new File(".").getCanonicalPath();
    }

    public void testDefaultTarget() throws Exception {
        executeTarget("default");
        assertTrue(new File(this.basedir + "/target/ant-test/message/MessageKeys.java").isFile());
    }

    public void testCustomTarget() throws Exception {
        executeTarget("custom");
        assertTrue(new File(this.basedir + "/target/ant-test/msg/MsgKeys.java").isFile());
    }
}
