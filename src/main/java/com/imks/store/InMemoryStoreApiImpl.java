/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store;

import com.imks.store.copy.DeepCopy;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author manuelmerida
 */
final class InMemoryStoreApiImpl implements InMemoryStoreApi {

    private final DeepCopy<Serializable> deepCopy = new DeepCopy<>();
    private final NameSpaceStore nameSpaceStore = NameSpaceStoreFactory.INSTANCE.get();

    public InMemoryStoreApiImpl() {
    }

    @Override
    public void put(String nameSpace, String key, Serializable value) throws Exception {
        nameSpaceStore.putOrGet(nameSpace).put(key, deepCopy.copy(value));
    }

    @Override
    public Serializable get(String nameSpace, String key) throws Exception {
        return deepCopy.copy(nameSpaceStore.get(nameSpace).get(key));
    }

    @Override
    public void delete(String nameSpace, String key) throws Exception {
        nameSpaceStore.get(nameSpace).delete(key);
    }

    @Override
    public Collection<Serializable> values(String nameSpace) throws Exception {
        return nameSpaceStore.get(nameSpace).values();
    }

}
