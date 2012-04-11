package org.ybiquitous.messages;

import java.util.Locale;

public class MessageLocaleHolder {

    private static final ThreadLocal<Locale> HOLDER = new ThreadLocal<Locale>() {
        @Override
        protected Locale initialValue() {
            return Locale.getDefault();
        }
    };

    public static Locale get() {
        return HOLDER.get();
    }

    public static void set(Locale locale) {
        HOLDER.set(Utils.notNull(locale, "locale"));
    }

    public static void reset() {
        HOLDER.remove();
    }
}
