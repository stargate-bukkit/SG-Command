package net.knarcraft.stargateinterfaces.util;

import java.util.concurrent.Callable;

public class ExceptionHelper {


    public static boolean doesNotThrow(ThrowingRunnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
