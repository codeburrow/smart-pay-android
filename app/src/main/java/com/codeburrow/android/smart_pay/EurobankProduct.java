package com.codeburrow.android.smart_pay;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/26/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class EurobankProduct {

    public String type;
    public String contractNumber;
    public long availableBalance;
    public long balance;
    public String currency;

    public EurobankProduct(String type, String contractNumber, long availableBalance, long balance, String currency) {
        this.type = type;
        this.contractNumber = contractNumber;
        this.availableBalance = availableBalance;
        this.balance = balance;
        this.currency = currency;
    }

}
