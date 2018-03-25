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
 *
 */
public interface KeyValueStore extends Serializable {

    /**
     * Associates a given value to a given key
     *
     * @param key
     * @param value
     * @throws Exception
     */
    void put(String key, Serializable value) throws Exception;

    /**
     * Returns the value which is associated to a given key
     *
     * @param key
     * @return
     * @throws Exception
     */
    Serializable get(String key) throws Exception;

    /**
     * Removes the association of any value to a given key
     *
     * @param key
     * @throws Exception
     */
    void delete(String key) throws Exception;

    /**
     * Returns a Collection which contains all values of this KeyValueStore
     *
     * @return
     * @throws Exception
     */
    Collection<Serializable> values() throws Exception;
}
