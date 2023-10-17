package utils;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils
{
    private Utils() {
    }

    public static int getInt(final String string) {
        return getInt(string, 0);
    }

    public static int getInt(final String string, final int def) {
        if (string == null) {
            return def;
        }
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException ignored) {
            return def;
        }
    }

    public static double getDouble(final String string) {
        return getDouble(string, 0.0);
    }

    public static double getDouble(final String string, final double def) {
        if (string == null) {
            return def;
        }
        try {
            return Double.parseDouble(string);
        }
        catch (NumberFormatException ignored) {
            return def;
        }
    }

    public static long getLong(final String string) {
        return getLong(string, 0L);
    }

    public static long getLong(final String string, final long def) {
        if (string == null) {
            return def;
        }
        try {
            return Long.parseLong(string);
        }
        catch (NumberFormatException ignored) {
            return def;
        }
    }

    public static float getFloat(final String string) {
        return getFloat(string, 0.0f);
    }

    public static float getFloat(final String string, final float def) {
        if (string == null) {
            return def;
        }
        try {
            return Float.parseFloat(string);
        }
        catch (NumberFormatException ignored) {
            return def;
        }
    }

    public static <K, V> Map.Entry<K, V> mapEntry(final K a, final V b) {
        return new AbstractMap.SimpleEntry<>(a, b);
    }

    public static <K, V> Map<K, V> mapOf(final K a, final V b) {
        return mapOf(mapEntry(a, b));
    }

    @SafeVarargs
    public static <K, V> Map<K, V> mapOf(final Map.Entry<K, V>... a) {
        return Arrays.stream(a).collect(Collectors.toMap((Function<? super Map.Entry<K, V>, ? extends K>)Map.Entry::getKey, (Function<? super Map.Entry<K, V>, ? extends V>)Map.Entry::getValue, (b, c) -> c));
    }
}
