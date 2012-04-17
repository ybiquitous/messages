package org.ybiquitous.messages;

import java.util.Locale;

public class ThreadLocalLocaleHolder {

    private static final ThreadLocal<Locale> THREAD_LOCAL = new ThreadLocal<Locale>() {
        @Override
        protected Locale initialValue() {
            return Locale.getDefault();
        }
    };

    public static Locale get() {
        return THREAD_LOCAL.get();
    }

    public static void set(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        THREAD_LOCAL.set(locale);
    }

    public static void reset() {
        THREAD_LOCAL.remove();
    }
}
