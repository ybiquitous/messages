package org.ybiquitous.messages;

import java.util.Locale;

public interface MessageBuilder {
    String build(Locale locale, String key, Object... args);
}
