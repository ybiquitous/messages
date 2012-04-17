package org.ybiquitous.messages;

public final class Utils {

    public static <T> T notNull(T obj, String name) throws NullPointerException {
        if (obj == null) {
            if (name == null) {
                throw new NullPointerException();
            } else {
                throw new NullPointerException(name + " is required");
            }
        }
        return obj;
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.isEmpty() || str.trim().isEmpty());
    }

    private Utils() {
    }
}
