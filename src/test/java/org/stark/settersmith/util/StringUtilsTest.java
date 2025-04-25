package org.stark.settersmith.util;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals("Hello", StringUtils.capitalize("hello"));
        
        // Test with a string that's already capitalized
        Assert.assertEquals("Hello", StringUtils.capitalize("Hello"));
        
        // Test with a single character
        Assert.assertEquals("A", StringUtils.capitalize("a"));
        
        // Test with an empty string
        Assert.assertEquals("", StringUtils.capitalize(""));
        
        // Test with null
        Assert.assertNull(StringUtils.capitalize(null));
    }

    /**
     * Tests the {@link StringUtils#camelCaseToHumanReadable(String)} method.
     */
    @Test
    public void testCamelCaseToHumanReadable() {
        // Test with a normal camelCase string
        Assert.assertEquals("Camel Case", StringUtils.camelCaseToHumanReadable("camelCase"));
        
        // Test with a string that starts with an uppercase letter
        Assert.assertEquals("Camel Case", StringUtils.camelCaseToHumanReadable("CamelCase"));
        
        // Test with a string that has multiple uppercase letters
        Assert.assertEquals("Camel Case ID", StringUtils.camelCaseToHumanReadable("camelCaseID"));
        
        // Test with a single word
        Assert.assertEquals("Word", StringUtils.camelCaseToHumanReadable("word"));
        
        // Test with an empty string
        Assert.assertEquals("", StringUtils.camelCaseToHumanReadable(""));
        
        // Test with null
        Assert.assertNull(StringUtils.camelCaseToHumanReadable(null));
    }
}