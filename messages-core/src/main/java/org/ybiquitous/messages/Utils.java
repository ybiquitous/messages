package org.ybiquitous.messages;


public final class Utils {

    public static <T> T notNull(T obj, String name) throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException(name + " is required");
        }
        return obj;
    }

    public static <T> T notNullOrElse(T obj, T defaultValue) throws NullPointerException {
        return (obj != null) ? obj : notNull(defaultValue, "defaultValue");
    }

    public static <T> T notNullOrElse(T obj, Factory<T> defaultValueFactory) throws NullPointerException {
        return (obj != null) ? obj : notNull(defaultValueFactory, "defaultValueFactory").get();
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.isEmpty() || str.trim().isEmpty());
    }

    private Utils() {
    }
}
