package org.ybiquitous.messages.generator;

import static org.apache.velocity.runtime.RuntimeConstants.RESOURCE_LOADER;
import static org.apache.velocity.runtime.RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS;

import java.net.URL;
import java.util.Properties;

import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.log.SystemLogChute;
import org.apache.velocity.runtime.resource.loader.URLResourceLoader;

final class VelocityConfig extends Properties {

    private static final long serialVersionUID = 1L;

    private static final boolean DEBUG = false;

    public VelocityConfig(URL url) {
        set(RESOURCE_LOADER, "url");
        set("url.resource.loader.class", URLResourceLoader.class);
        set("url.resource.loader.root", getParent(url));
        set("url.resource.loader.cache", false);
        set("url.resource.loader.modificationCheckInterval", 0);
        if (DEBUG) {
            set(RUNTIME_LOG_LOGSYSTEM_CLASS, SystemLogChute.class);
            set(SystemLogChute.RUNTIME_LOG_LEVEL_KEY, "trace");
            set(SystemLogChute.RUNTIME_LOG_SYSTEM_ERR_LEVEL_KEY, "trace");
        } else {
            set(RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class);
        }
    }

    private String getParent(URL url) {
        String urlStr = url.toString();
        return urlStr.substring(0, urlStr.lastIndexOf('/'));
    }

    private void set(String key, Object value) {
        if (value instanceof String) {
            setProperty(key, (String) value);
        } else if (value instanceof Class<?>) {
            setProperty(key, ((Class<?>) value).getCanonicalName());
        } else {
            setProperty(key, value.toString());
        }
    }
}
