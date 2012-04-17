package org.ybiquitous.messages;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    private final Logger logger;

    public Log(Class<?> clazz) {
        this.logger = Logger.getLogger(clazz.getName());
    }

    public void info(String msg, Object... args) {
        log(Level.INFO, msg, args);
    }

    public void debug(String msg, Object... args) {
        log(Level.FINEST, msg, args);
    }

    private void log(Level level, String msg, Object... args) {
        if (this.logger.isLoggable(level)) {
            this.logger.log(level, (args.length == 0) ? msg : String.format(msg, args));
        }
    }
}
