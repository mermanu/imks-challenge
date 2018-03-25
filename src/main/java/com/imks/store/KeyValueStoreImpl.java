/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author manuelmerida
 */
final class KeyValueStoreImpl implements KeyValueStore {

    private final ConcurrentMap<String, Serializable> keyValueStore = new ConcurrentHashMap<>();

    public KeyValueStoreImpl() {
    }

    @Override
    public void put(String key, Serializable value) throws Exception {
        keyValueStore.put(key, value);
    }

    @Override
    public Serializable get(String key) throws Exception {
        return keyValueStore.get(key);
    }

    @Override
    public void delete(String key) throws Exception {
        keyValueStore.remove(key);
    }

    @Override
    public Collection<Serializable> values() throws Exception {
        return Collections.unmodifiableCollection(keyValueStore.values());
    }

}
