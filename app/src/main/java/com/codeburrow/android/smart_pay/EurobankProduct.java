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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(long availableBalance) {
        this.availableBalance = availableBalance;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}