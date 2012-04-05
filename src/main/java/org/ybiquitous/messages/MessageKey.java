package org.ybiquitous.messages;

import java.util.Locale;

public final class MessageKey {

    private final String _key;

    private final MessageBuilder _builder;

    public MessageKey(String key) {
        this(key, new DefaultMessageBuilder("messages"));
    }

    public MessageKey(String key, MessageBuilder builder) {
        this._key = Utils.notNull(key, "key");
        this._builder = Utils.notNull(builder, "builder");
    }

    public String get(Object... args) {
        return get(Locale.getDefault(), args);
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
