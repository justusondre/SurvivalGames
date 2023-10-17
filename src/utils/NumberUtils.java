package utils;

public class NumberUtils
{
    private NumberUtils() {
    }

    public static boolean isInteger(final String str) {
        if (str == null) {
            return false;
        }
        final int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        while (i < length) {
            final char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
            ++i;
        }
        return true;
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

    public static boolean isDouble(final String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException ignored) {
            return false;
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

    public static boolean isLong(final String str) {
        try {
            Long.parseLong(str);
            return true;
        }
        catch (NumberFormatException ignored) {
            return false;
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

    public static boolean isShort(final String str) {
        try {
            Short.parseShort(str);
            return true;
        }
        catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static long getShort(final String string) {
        return getShort(string, (short)0);
    }

    public static long getShort(final String string, final short def) {
        if (string == null) {
            return def;
        }
        try {
            return Short.parseShort(string);
        }
        catch (NumberFormatException ignored) {
            return def;
        }
    }

    public static boolean isFloat(final String str) {
        try {
            Float.parseFloat(str);
            return true;
        }
        catch (NumberFormatException ignored) {
            return false;
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

    public static boolean isBetween(final int value, final int min, final int max) {
        return value >= min && value <= max;
    }

    public static int roundInteger(final int integer, final int floor) {
        return (integer < floor) ? floor : ((int)(floor * Math.round(integer / (double)floor)));
    }
}
