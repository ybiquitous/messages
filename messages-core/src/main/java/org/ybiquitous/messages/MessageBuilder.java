package org.ybiquitous.messages;

import java.util.Locale;

public interface MessageBuilder {
    String build(Locale locale, String key, Object... args) throws MessageKeyNotFoundException;
    String buildOrElse(Locale locale, String key, Object[] args, String defaultValue);
}
