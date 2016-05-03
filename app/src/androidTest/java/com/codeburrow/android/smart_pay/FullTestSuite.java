package com.codeburrow.android.smart_pay;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/02/2016.
 *
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class FullTestSuite extends TestSuite {

    public FullTestSuite(){
        super();
    }

    public static Test suite(){
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

}