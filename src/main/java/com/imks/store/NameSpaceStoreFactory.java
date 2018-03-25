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
enum NameSpaceStoreFactory {    
    INSTANCE(new NameSpaceStoreImpl());

    private final NameSpaceStore nameSpaceStore;

    private NameSpaceStoreFactory(NameSpaceStore nameSpaceStore) {
        this.nameSpaceStore = nameSpaceStore;
    }
    
    public NameSpaceStore get() {
        return this.nameSpaceStore;
    }

}
