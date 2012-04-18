package org.ybiquitous.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {

    private final Logger logger;

    public Log(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz.getName());
    }

    public void error(Object msgOrFmt, Object... args) {
        this.logger.error(buildMsg(msgOrFmt, args));
    }

    public void warn(Object msgOrFmt, Object... args) {
        this.logger.warn(buildMsg(msgOrFmt, args));
    }

    public void info(Object msgOrFmt, Object... args) {
        this.logger.info(buildMsg(msgOrFmt, args));
    }

    public void debug(Object msgOrFmt, Object... args) {
        this.logger.debug(buildMsg(msgOrFmt, args));
    }

    private static String buildMsg(Object msgOrFmt, Object... args) {
        final String str = String.valueOf(msgOrFmt);
        return (args.length == 0) ? str : String.format(str, args);
    }
}
