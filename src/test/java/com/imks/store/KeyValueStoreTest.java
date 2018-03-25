/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store;

import com.imks.store.util.SerializableObject;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author manuelmerida
 */
public class KeyValueStoreTest {
    
    private static final String TEST_KEY = "test";
    private static final int NUMBER_OF_KEY_VALUES = 30; 
    
    public KeyValueStoreTest() {
    }
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() throws InterruptedException, IOException {
    }

    /**
     * Test of put and get method, of class KeyValueStore.
     * @throws java.lang.Exception
     */
    @Test
    public void testWhenPutAKeyValueInAKeyValueStoreThenValueIsSavedAndReturnedWithGetMethod() throws Exception {
        KeyValueStore keyValueStore = new KeyValueStoreImpl();
        SerializableObject expKeyValueObject = new SerializableObject(new AtomicInteger(15));
        keyValueStore.put(TEST_KEY, expKeyValueObject);
        SerializableObject resultObject = (SerializableObject)keyValueStore.get(TEST_KEY);
        
        assertEquals(expKeyValueObject.getSum().get(), resultObject.getSum().get());
    }

    /**
     * Test of delete method, of class KeyValueStore.
     * @throws java.lang.Exception
     */
    @Test
    public void testWhenPutAKeyValueInAKeyValueStoreAndCallDeleteTheKeyThenValueIsDeleted() throws Exception {
        KeyValueStore keyValueStore = new KeyValueStoreImpl();
        SerializableObject expKeyValueObject = new SerializableObject(new AtomicInteger(0));
        keyValueStore.put(TEST_KEY, expKeyValueObject);
        keyValueStore.delete(TEST_KEY);
        
        assertNull(keyValueStore.get(TEST_KEY));
    }
    
    /**
     * Test of values method, of class KeyValueStore.
     * @throws java.lang.Exception
     */
    @Test
    public void testWhenPutSomeKeyValueInAKeyValueStoreAndCallValuesMethodThenTheCollectionIsReturnedWithTheExpectedValues() throws Exception {
        KeyValueStore keyValueStore = new KeyValueStoreImpl();
        for(int i = 0; i < NUMBER_OF_KEY_VALUES; i++){
            SerializableObject expKeyValueObject = new SerializableObject(new AtomicInteger(i));
            keyValueStore.put(String.valueOf(i), expKeyValueObject);
        }
        
        assertEquals(NUMBER_OF_KEY_VALUES, keyValueStore.values().size());          
    }
    
}
