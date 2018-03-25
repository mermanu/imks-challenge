/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store;

/**
 *
 * @author manuelmerida
 */
public enum InMemoryStoreApiFactory {
    INSTANCE(new InMemoryStoreApiImpl());

    private final InMemoryStoreApi inMemoryStoreApi;

    private InMemoryStoreApiFactory(InMemoryStoreApi inMemoryStoreApi) {
        this.inMemoryStoreApi = inMemoryStoreApi;
    }

    public InMemoryStoreApi get() {
        return this.inMemoryStoreApi;
    }
}
