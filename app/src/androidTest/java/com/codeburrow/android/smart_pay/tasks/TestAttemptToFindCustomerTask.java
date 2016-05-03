package com.codeburrow.android.smart_pay.tasks;

import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/03/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class TestAttemptToFindCustomerTask extends AndroidTestCase {

    private static final String LOG_TAG = TestAttemptToFindCustomerTask.class.getSimpleName();

    private CountDownLatch signal = null;
    private JSONObject testCustomer;
    private String taskError;
    private String customerNumber = "5724fb33db45021c1aebef24";


    @Override
    protected void setUp() throws Exception {
        signal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        signal.countDown();
    }

    public void testAttemptToFindAccountTask() throws InterruptedException {
        AttemptToFindCustomerTask attemptToFindCustomerTask = new AttemptToFindCustomerTask(getContext(), new AttemptToFindCustomerTask.CustomerAsyncResponse() {
            @Override
            public void processFindCustomerAsyncFinish(JSONObject foundCustomer, String error) {
                testCustomer = foundCustomer;
                taskError = error;
                signal.countDown();
            }
        }, customerNumber);

        attemptToFindCustomerTask.execute();

        signal.await();

        assertTrue("Error found: " + taskError, taskError == null);
        assertFalse("The JSONArray with the transaction is NULL", testCustomer == null);

        Log.e(LOG_TAG, "Customer: " + testCustomer.toString() + " Error: " + taskError);
    }
}