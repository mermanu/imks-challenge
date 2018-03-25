/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Serializable class to test concurrent use of the InMemoryStore
 */
public class SerializableObject implements Serializable {

    private AtomicInteger sum;
    private String threadId;

    public SerializableObject(AtomicInteger sum) {
        this.sum = sum;
    }

    public AtomicInteger getSum() {
        return sum;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

}
