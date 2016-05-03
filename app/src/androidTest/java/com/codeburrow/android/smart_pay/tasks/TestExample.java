package com.codeburrow.android.smart_pay.tasks;

import android.test.AndroidTestCase;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/03/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class TestExample extends AndroidTestCase {

    public void testThisTestClass(){
        if (true) {

        } else {
            fail("fail(): This should never happen");
        }
    }

    public void testAssertTrue(){
        assertTrue("assertTrue(): with true condition", 1 == 1);
//        assertTrue("assertTrue(): with FALSE condition", 1 != 1);
    }

    public void testAssertFalse(){
//        assertFalse("assertFalse(): with TRUE condition", 1 == 1);
        assertFalse("assertFalse(): with false condition", 1 != 1);
    }

    public void testAssertEquals(){
        assertEquals("assertEquals(): with equal true values", true, true);
        assertEquals("assertEquals(): with equal false values", false, false);
//        assertEquals("assertEquals(): with NOT equal values", true, false);
//        assertEquals("assertEquals(): with NOT equal values", false, true);
    }

}