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
public class TestAttemptToFindAccountTask extends AndroidTestCase {

    private static final String LOG_TAG = TestAttemptToFindAccountTask.class.getSimpleName();

    private CountDownLatch signal = null;
    private JSONObject testAccount;
    private String taskError;
    private String iban = "IBAN11223377";


    @Override
    protected void setUp() throws Exception {
        signal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        signal.countDown();
    }

    public void testAttemptToFindAccountTask() throws InterruptedException {
        AttemptToFindAccountTask attemptToFindAccountTask = new AttemptToFindAccountTask(getContext(), new AttemptToFindAccountTask.AccountAsyncResponse() {
            @Override
            public void processFindAccountAsyncFinish(JSONObject foundAccount, String error) {
                testAccount = foundAccount;
                taskError = error;
                signal.countDown();
            }
        }, iban);

        attemptToFindAccountTask.execute();

        signal.await();

        assertTrue("Error found: " + taskError, taskError == null);
        assertFalse("The JSONArray with the transaction is NULL", testAccount == null);

        Log.e(LOG_TAG, " Account: " + testAccount.toString() + " Error: " + taskError);
    }
}