package org.ybiquitous.messages;

import java.nio.charset.Charset;

public final class Constants {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static final String DEFAULT_MESSAGE_RESOURCE_NAME = "messages";

    public static final String DEFAULT_MESSAGE_RESOURCE_EXTENSION = "properties";

    public static final String DEFAULT_MESSAGE_RESOURCE = DEFAULT_MESSAGE_RESOURCE_NAME
            + '.' + DEFAULT_MESSAGE_RESOURCE_EXTENSION;

    private Constants() {
    }
}
