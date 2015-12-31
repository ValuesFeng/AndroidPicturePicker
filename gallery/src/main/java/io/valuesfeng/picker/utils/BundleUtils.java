package io.valuesfeng.picker.utils;

/**
 * Created by zaiyong on 2015/6/6.
 */
public class BundleUtils {
    private BundleUtils() {
    }

    public static String buildKey(Class<?> clazz, String name) {
        return clazz.getCanonicalName() + "." + name;
    }
}
