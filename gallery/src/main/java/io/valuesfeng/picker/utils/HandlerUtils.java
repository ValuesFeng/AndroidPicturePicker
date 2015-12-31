package io.valuesfeng.picker.utils;

import android.os.Handler;
import android.os.Looper;

/**
 */
public class HandlerUtils {
    private HandlerUtils() {
    }

    public static Handler getMainHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public static void postOnMain(Runnable message) {
        getMainHandler().post(message);
    }

    public static void postOnMainWithDelay(Runnable message, long delayMillis) {
        getMainHandler().postDelayed(message, delayMillis);
    }
}
