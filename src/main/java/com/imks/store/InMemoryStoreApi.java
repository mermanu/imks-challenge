/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author manuelmerida
 */
public interface InMemoryStoreApi {

    /**
     * Associates a given value to a given key in a given namespace.
     *
     * @param nameSpace
     * @param key
     * @param value
     * @throws Exception
     */
    void put(String nameSpace, String key, Serializable value) throws Exception;

    /**
     * Returns the value which is associated to a given key in a given
     * namespace.
     *
     * @param nameSpace
     * @param key
     * @return Serializable
     * @throws Exception
     */
    Serializable get(String nameSpace, String key) throws Exception;

    /**
     * Removes the association of any value to a given key in a given namespace.
     *
     * @param nameSpace
     * @param key
     * @throws Exception
     */
    void delete(String nameSpace, String key) throws Exception;

    /**
     * Returns a Collection of all values which are associated to any keys in a
     * given namespace.
     *
     * @param nameSpace
     * @return Collection
     * @throws Exception
     */
    Collection<Serializable> values(String nameSpace) throws Exception;
}
