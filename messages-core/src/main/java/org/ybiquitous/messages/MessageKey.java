package org.ybiquitous.messages;

import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

public final class MessageKey {

    private static final Object[] EMPTY_ARGS = {};

    public static MessageKey of(String key) {
        return of(key, Constants.DEFAULT_MESSAGE_RESOURCE_NAME);
    }

    public static MessageKey of(String key, String resourceName) {
        return of(key, DefaultMessageBuilder.create(resourceName));
    }

    public static MessageKey of(String key, MessageBuilder builder) {
        final CacheKey cacheKey = new CacheKey(key, builder);
        MessageKey result = CACHE.get(cacheKey);
        if (result == null) {
            result = new MessageKey(key, builder);
            CACHE.put(cacheKey, result);
        }
        return result;
    }

    private static final Map<CacheKey, MessageKey> CACHE = new WeakHashMap<CacheKey, MessageKey>();

    private final String _key;

    private transient final MessageBuilder _builder;

    private MessageKey(String key, MessageBuilder builder) {
        this._key = Utils.notNull(key, "key");
        this._builder = Utils.notNull(builder, "builder");
    }

    public String get(Object... args) throws MessageKeyNotFoundException {
        return get(MessageLocaleHolder.get(), args);
    }

    public String get(Locale locale, Object... args) throws MessageKeyNotFoundException {
        return this._builder.build(locale, this._key, args);
    }

    public String getOrElse(String defaultValue) {
        return getOrElse(EMPTY_ARGS, defaultValue);
    }

    public String getOrElse(Object[] args, String defaultValue) {
        return getOrElse(MessageLocaleHolder.get(), args, defaultValue);
    }

    public String getOrElse(Locale locale, String defaultValue) {
        return getOrElse(locale, EMPTY_ARGS, defaultValue);
    }

    public String getOrElse(Locale locale, Object[] args, String defaultValue) {
        return this._builder.buildOrElse(locale, this._key, args, defaultValue);
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
