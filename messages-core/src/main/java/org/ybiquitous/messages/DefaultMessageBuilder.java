package org.ybiquitous.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

class DefaultMessageBuilder implements MessageBuilder {

    private static final Map<String, DefaultMessageBuilder> CACHE = new WeakHashMap<String, DefaultMessageBuilder>();

    public static DefaultMessageBuilder create(String baseName) {
        DefaultMessageBuilder result = CACHE.get(baseName);
        if (result == null) {
            result = new DefaultMessageBuilder(baseName);
            CACHE.put(baseName, result);
        }
        return result;
    }

    private final String _baseName;

    private DefaultMessageBuilder(String baseName) {
        this._baseName = Utils.notNull(baseName, "baseName");
    }

    @Override
    public String build(Locale locale, String key, Object... args) {
        if (args == null || args.length == 0) {
            return _build(locale, key, args);
        }
        final Object[] newArgs = args.clone();
        int i = 0;
        for (Object arg : args) {
            if (arg instanceof MessageKey) {
                newArgs[i++] = _build(locale, ((MessageKey) arg).toString());
            }
        }
        return _build(locale, key, newArgs);
    }

    private String _build(Locale locale, String key, Object... args) {
        final String base = ResourceBundle.getBundle(this._baseName, locale,
                CustomControl.INSTANCE).getString(key);
        return new MessageFormat(base, locale).format(args);
    }

    private static class CustomControl extends ResourceBundle.Control {

        public static final CustomControl INSTANCE = new CustomControl();

        private static final List<String> FORMATS = Arrays
                .asList(Constants.DEFAULT_MESSAGE_RESOURCE_EXTENSION);

        @Override
        public List<String> getFormats(String baseName) {
            Utils.notNull(baseName, "baseName");
            return FORMATS;
        }

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale,
                String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException,
                IOException {

            final String bundleName = toBundleName(baseName, locale);
            final String resourceName = toResourceName(bundleName, format);
            final InputStream stream = getStream(resourceName, loader, reload);

            if (stream == null) {
                throw new IllegalArgumentException("unknown resouce: "
                        + resourceName);
            }

            try {
                return new PropertyResourceBundle(
                        new BufferedReader(new InputStreamReader(stream,
                                Constants.DEFAULT_CHARSET)));
            } finally {
                stream.close();
            }
        }

        private InputStream getStream(String resourceName, ClassLoader loader,
                boolean reload) throws IOException {
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        return connection.getInputStream();
                    }
                }
            }
            return loader.getResourceAsStream(resourceName);
        }
    }

}
