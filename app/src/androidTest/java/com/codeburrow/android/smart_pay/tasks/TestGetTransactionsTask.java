package com.codeburrow.android.smart_pay.tasks;

import android.test.AndroidTestCase;

import com.codeburrow.android.smart_pay.apis.TransactionApi;

import org.json.JSONArray;

import java.util.concurrent.CountDownLatch;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/02/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class TestGetTransactionsTask extends AndroidTestCase {

    private static final String LOG_TAG = TestGetTransactionsTask.class.getSimpleName();

    private CountDownLatch signal = null;
    private JSONArray testTransactions;
    private int numberOfTransactions = -1;

    @Override
    protected void setUp() throws Exception {
        signal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        signal.countDown();
    }

    public void testGetTransactionsTask() throws InterruptedException {

        GetTransactionsTask getTransactionsTask = new GetTransactionsTask(getContext(), new GetTransactionsTask.TransactionAsyncResponse() {
            @Override
            public void processGetTransactionsAsyncFinish(JSONArray transactions) {
                testTransactions = transactions;
                signal.countDown();
            }
        });

        getTransactionsTask.execute();

        signal.await();

        assertFalse("The JSONArray with the transaction is NULL", testTransactions == null);

        testCountTransactions(testTransactions);
    }

    public void testCountTransactions(JSONArray transactions) {
        numberOfTransactions = TransactionApi.countTransactions(transactions);

        assertTrue("Transactions count failure", numberOfTransactions != -1);
    }

}