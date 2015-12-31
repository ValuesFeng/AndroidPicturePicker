package io.valuesfeng.picker.utils;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by laputan on 15-6-8.
 */
public class CloseableUtils {

    private static final String TAG = CloseableUtils.class.getSimpleName();

    private CloseableUtils() {
    }

    public static final void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
                Log.e(TAG, "something went wrong on close", var2);
            }

        }
    }
}
