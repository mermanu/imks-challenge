/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author manuelmerida
 */
final class NameSpaceStoreImpl implements NameSpaceStore {

    private final ConcurrentMap<String, KeyValueStore> nameSpaceStore = new ConcurrentHashMap<>();

    public NameSpaceStoreImpl() {
    }

    @Override
    public KeyValueStore get(String key) throws Exception {
        return nameSpaceStore.get(key);
    }

    @Override
    public KeyValueStore putOrGet(String key) throws Exception {
        //If namespace not created, then we creates a new one
        nameSpaceStore.putIfAbsent(key, new KeyValueStoreImpl());
        return nameSpaceStore.get(key);
    }

    @Override
    public void delete(String key) throws Exception {
        nameSpaceStore.remove(key);
    }

    @Override
    public Collection<KeyValueStore> values() {
        return Collections.unmodifiableCollection(nameSpaceStore.values());
    }

}
