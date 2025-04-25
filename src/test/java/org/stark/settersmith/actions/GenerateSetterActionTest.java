package org.stark.settersmith.actions;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link GenerateSetterAction}.
 */
public class GenerateSetterActionTest {

    /**
     * Tests that the GenerateSetterAction can be instantiated.
     */
    @Test
    public void testActionInstantiation() {
        GenerateSetterAction action = new GenerateSetterAction();
        Assert.assertNotNull("Action should be instantiated", action);
    }

    /**
     * Tests that the action returns the correct update thread.
     */
    @Test
    public void testGetActionUpdateThread() {
        GenerateSetterAction action = new GenerateSetterAction();
        Assert.assertNotNull("Action update thread should not be null", action.getActionUpdateThread());
    }
}
