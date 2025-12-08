package org.multibank.core.utils;
import lombok.SneakyThrows;
import java.util.function.Supplier;

public final class RetryUtils {

    @SneakyThrows
    public static <T> T retry(int attempts, int delayMs, Supplier<T> action) {
        Throwable last = null;

        for (int i = 0; i < attempts; i++) {
            try {
                return action.get();
            } catch (Throwable t) {
                last = t;
                if (i < attempts - 1) {
                    Thread.sleep(delayMs);
                }
            }
        }
        throw new RuntimeException("Retry attempts reached", last);
    }

    @SneakyThrows
    public static void retryVoid(int attempts, int delayMs, Runnable action) {
        Throwable last = null;

        for (int i = 0; i < attempts; i++) {
            try {
                action.run();
                return;
            } catch (Throwable t) {
                last = t;
                if (i < attempts - 1) {
                    Thread.sleep(delayMs);
                }
            }
        }
        throw new RuntimeException("Retry attempts reached", last);
    }
}
