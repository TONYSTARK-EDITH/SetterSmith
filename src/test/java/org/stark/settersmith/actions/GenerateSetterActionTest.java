package org.stark.settersmith.actions;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(action);
    }

    /**
     * Tests that the action returns the correct update thread.
     */
    @Test
    public void testGetActionUpdateThread() {
        GenerateSetterAction action = new GenerateSetterAction();
        assertNotNull(action.getActionUpdateThread());
    }
}
