package org.stark.settersmith.util;

/**
 * Utility class for string operations.
 */
public class StringUtils {

    /**
     * Capitalizes the first letter of a string.
     *
     * @param str the string to capitalize
     * @return the capitalized string
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Converts a camelCase string to a human-readable format.
     * For example, "camelCase" becomes "Camel Case".
     *
     * @param str the camelCase string
     * @return the human-readable string
     */
    public static String camelCaseToHumanReadable(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Pre-allocate StringBuilder with a capacity based on input length
        // Assuming worst case: every character is uppercase and needs a space
        StringBuilder result = new StringBuilder(str.length() * 2);
        result.append(Character.toUpperCase(str.charAt(0)));

        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c) && (i == 1 || !Character.isUpperCase(str.charAt(i - 1)))) {
                result.append(' ');
            }
            result.append(c);
        }

        return result.toString();
    }
}
