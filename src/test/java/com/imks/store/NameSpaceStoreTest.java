/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store;

import com.imks.store.util.SerializableObject;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author manuelmerida
 */
public class NameSpaceStoreTest {
    
    private static final String TEST_KEY = "test";
    private static final String[] CREATED_NAMESPACES = {"namespace1", "namespace2", "namespace3"};
    private static final int NUMBER_OF_THREAD_POOL = 8;
    private static final int NUMBER_OF_THREADS = 50000;
    private static final int SERVICE_AWAIT_TERMINATION_TIME = 3;
    private NameSpaceStore nameSpaceStorage;

    public NameSpaceStoreTest() {
    }

    @Before
    public void setUp() {
        nameSpaceStorage = new NameSpaceStoreImpl();
    }
    
    @AfterClass
    public static void after() throws InterruptedException, IOException {
    }

    //Values method
    @Test
    public void testWhenNameSpaceStoreHaveNoValuesThenValuesMethodReturnsAnEmptyCollection() {
        NameSpaceStore resultNameSpaceStore = new NameSpaceStoreImpl();
        ConcurrentMap<String, KeyValueStore> expectedNameSpaceStore = new ConcurrentHashMap<>();

        assertArrayEquals(expectedNameSpaceStore.values().toArray(), resultNameSpaceStore.values().toArray());
    }

    @Test
    public void testWhenNameSpaceStoreHaveCreatedNameSpacesThenValuesMethodReturnsThePopulatedCollection() throws Exception {
        NameSpaceStore resultNameSpaceStore = new NameSpaceStoreImpl();
        for (String namespace : CREATED_NAMESPACES) {
            resultNameSpaceStore.putOrGet(namespace);
        }

        assertEquals(CREATED_NAMESPACES.length, resultNameSpaceStore.values().size());
        for (String namespace : CREATED_NAMESPACES) {
            assertTrue(resultNameSpaceStore.get(namespace) != null);
        }
    }

    //Get method
    @Test
    public void testWhenNoNameSpaceCreatedThenGetMethodReturnsNull() throws Exception {
        NameSpaceStore resultNameSpaceStore = new NameSpaceStoreImpl();

        assertNull(resultNameSpaceStore.get(CREATED_NAMESPACES[0]));
    }

    @Test
    public void testWhenNameSpaceCreatedThenGetMethodReturnsAKeyValueStore() throws Exception {
        NameSpaceStore resultNameSpaceStore = new NameSpaceStoreImpl();
        resultNameSpaceStore.putOrGet(CREATED_NAMESPACES[0]);

        assertNotNull(resultNameSpaceStore.get(CREATED_NAMESPACES[0]));
    }

    //Delete method
    @Test
    public void testWhenDeleteMethodIsCalledThenTheNamedSpaceIsRemoved() throws Exception {
        NameSpaceStore resultNameSpaceStore = new NameSpaceStoreImpl();
        resultNameSpaceStore.putOrGet(CREATED_NAMESPACES[0]);
        resultNameSpaceStore.delete(CREATED_NAMESPACES[0]);
        
        assertNull(resultNameSpaceStore.get(CREATED_NAMESPACES[0]));
    }

    @Test
    public void testWhenDeleteMethodIsCalledThenReturnsNullIfThePreviousValueNotExist() throws Exception {
        NameSpaceStore resultNameSpaceStore = new NameSpaceStoreImpl();
        resultNameSpaceStore.delete(CREATED_NAMESPACES[0]);
        assertNull(resultNameSpaceStore.get(CREATED_NAMESPACES[0]));
    }

    //Test Multiple threads working concurrently with the same key-value store
    @Test
    public void testMultipleThreadsCreateAndGetAndDeleteAndValuesConcurrentlyWithNoException() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREAD_POOL);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    nameSpaceStorage.putOrGet(CREATED_NAMESPACES[0] + 1);
                    nameSpaceStorage.get(CREATED_NAMESPACES[0] + 1);
                    nameSpaceStorage.delete(CREATED_NAMESPACES[0] + 1);
                } catch (Exception ex) {
                    Logger.getLogger(NameSpaceStoreTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(SERVICE_AWAIT_TERMINATION_TIME, TimeUnit.SECONDS);

        assertEquals(0, nameSpaceStorage.values().size());
    }

    @Test
    public void testMultipleThreadsCreateAndGetAndValuesConcurrentlyWithNoException() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREAD_POOL);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    nameSpaceStorage.putOrGet(UUID.randomUUID().toString());
                } catch (Exception ex) {
                    Logger.getLogger(NameSpaceStoreTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(SERVICE_AWAIT_TERMINATION_TIME, TimeUnit.SECONDS);

        assertEquals(NUMBER_OF_THREADS, nameSpaceStorage.values().size());
    }

    @Test
    public void testMultipleThreadsCreateAndSumValueWithNoException() throws Exception {
        //Creates the namespace[0] with the 'test' key-value store
        nameSpaceStorage.putOrGet(CREATED_NAMESPACES[0]);
        nameSpaceStorage.get(CREATED_NAMESPACES[0]).put(TEST_KEY, new SerializableObject(new AtomicInteger(0)));

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREAD_POOL);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    KeyValueStore keyValueStore = nameSpaceStorage.get(CREATED_NAMESPACES[0]);
                    SerializableObject serializableObject = (SerializableObject) keyValueStore.get(TEST_KEY);
                    serializableObject.getSum().getAndIncrement();
                    keyValueStore.put(TEST_KEY, serializableObject);
                } catch (Exception ex) {
                    Logger.getLogger(NameSpaceStoreTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(SERVICE_AWAIT_TERMINATION_TIME, TimeUnit.SECONDS);
        SerializableObject testResultObject = (SerializableObject) nameSpaceStorage.get(CREATED_NAMESPACES[0]).get(TEST_KEY);

        assertEquals(1, nameSpaceStorage.values().size());
        assertEquals(new AtomicInteger(NUMBER_OF_THREADS).get(), testResultObject.getSum().get());
    }

}
