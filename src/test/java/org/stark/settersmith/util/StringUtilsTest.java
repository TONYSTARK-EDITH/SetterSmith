package org.stark.settersmith.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for {@link StringUtils}.
 */
public class StringUtilsTest {

    /**
     * Tests the {@link StringUtils#capitalize(String)} method.
     */
    @Test
    public void testCapitalize() {
        // Test with a normal string
        assertEquals("Hello", StringUtils.capitalize("hello"));

        // Test with a string that's already capitalized
        assertEquals("Hello", StringUtils.capitalize("Hello"));

        // Test with a single character
        assertEquals("A", StringUtils.capitalize("a"));

        // Test with an empty string
        assertEquals("", StringUtils.capitalize(""));

        // Test with null
        assertNull(StringUtils.capitalize(null));
    }

    /**
     * Tests the {@link StringUtils#camelCaseToHumanReadable(String)} method.
     */
    @Test
    public void testCamelCaseToHumanReadable() {
        // Test with a normal camelCase string
        assertEquals("Camel Case", StringUtils.camelCaseToHumanReadable("camelCase"));

        // Test with a string that starts with an uppercase letter
        assertEquals("Camel Case", StringUtils.camelCaseToHumanReadable("CamelCase"));

        // Test with a string that has multiple uppercase letters
        assertEquals("Camel Case ID", StringUtils.camelCaseToHumanReadable("camelCaseID"));

        // Test with a single word
        assertEquals("Word", StringUtils.camelCaseToHumanReadable("word"));

        // Test with an empty string
        assertEquals("", StringUtils.camelCaseToHumanReadable(""));

        // Test with null
        assertNull(StringUtils.camelCaseToHumanReadable(null));
    }
}