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
public interface NameSpaceStore extends Serializable {

    /**
     * Creates a new namespace KeyValueStore with the provided key if not exist
     * or returns the current associated KeyValueStore if already exist
     *
     * @param key
     * @return KeyValueStore
     * @throws Exception
     */
    KeyValueStore putOrGet(String key) throws Exception;

    /**
     * Returns an KeyValueStore if already exist or null if there is no value
     * assigned
     *
     * @param key
     * @return KeyValueStore
     * @throws Exception
     */
    KeyValueStore get(String key) throws Exception;

    /**
     * Removes the given namespace KeyValueStore
     *
     * @param key
     * @throws Exception
     */
    void delete(String key) throws Exception;

    /**
     * Returns a collection of all KeyValueStore namespaces in this
     * NameSpaceStore
     *
     * @return Collection
     */
    Collection<KeyValueStore> values();

}
