package com.xie.java.datasource;

public class TransactionCounter {

    private volatile int counter;

    public int increase() {
        counter++;
        return counter;
    }

    public int decrease() {
        counter--;
        return counter;
    }

    public int value() {
        return counter;
    }
}