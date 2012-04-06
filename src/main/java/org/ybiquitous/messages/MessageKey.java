package org.ybiquitous.messages;

import java.util.Locale;

public final class MessageKey {

    public static MessageKey of(String key) {
        return of(key, DEFAULT_RESOURCE_NAME);
    }

    public static MessageKey of(String key, String resourceName) {
        return of(key, DefaultMessageBuilder.create(resourceName));
    }

    public static MessageKey of(String key, MessageBuilder builder) {
        return new MessageKey(key, builder);
    }

    private static final String DEFAULT_RESOURCE_NAME = "messages";

    private final String _key;

    private transient final MessageBuilder _builder;

    private MessageKey(String key, MessageBuilder builder) {
        this._key = Utils.notNull(key, "key");
        this._builder = Utils.notNull(builder, "builder");
    }

    public String get(Object... args) {
        return get(MessageLocaleHolder.get(), args);
    }

    public String get(Locale locale, Object... args) {
        return this._builder.build(locale, this._key, args);
    }

    @Override
    public String toString() {
        return this._key;
    }

    @Override
    public int hashCode() {
        return this._key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MessageKey that = (MessageKey) obj;
        return this._key.equals(that._key);
    }
}
