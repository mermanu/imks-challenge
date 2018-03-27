/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store;

import com.imks.store.util.SerializableObject;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author manuelmerida
 */
public class InMemoryStoreApiTest {

    private static final String TEST_KEY = "test";
    private static final String[] CREATED_NAMESPACES = {"namespace1", "namespace2", "namespace3", "nothreadstest"};
    private static final int NUMBER_OF_THREAD_POOL = 8;
    private static final int NUMBER_OF_THREADS = 50000;
    private static final int SERVICE_AWAIT_TERMINATION_TIME = 3;
    InMemoryStoreApi inMemoryStoreApi;

    public InMemoryStoreApiTest() {
        inMemoryStoreApi = InMemoryStoreApiFactory.INSTANCE.get();
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testWhenDeleteKeyWhichNotExistThenNullValueReturned() throws Exception{
        inMemoryStoreApi.delete(CREATED_NAMESPACES[3], TEST_KEY+4);
        assertNull(inMemoryStoreApi.get(CREATED_NAMESPACES[3], TEST_KEY+4));
    }
    
    @Test
    public void testWhenGetKeyWhichNotExistThenNullValueReturned() throws Exception{
        assertNull(inMemoryStoreApi.get(CREATED_NAMESPACES[3], TEST_KEY+4));
    }
    
    /**
     * Test basic put,get,delete operations without multi threads
     * @throws Exception 
     */
    @Test
    public void testPutGetDeleteMethods() throws Exception{
        inMemoryStoreApi.put(CREATED_NAMESPACES[3], TEST_KEY, new SerializableObject(new AtomicInteger(0)));
        inMemoryStoreApi.put(CREATED_NAMESPACES[3], TEST_KEY, new SerializableObject(new AtomicInteger(3)));
        inMemoryStoreApi.put(CREATED_NAMESPACES[3], TEST_KEY, new SerializableObject(new AtomicInteger(8)));
        inMemoryStoreApi.put(CREATED_NAMESPACES[3], TEST_KEY, new SerializableObject(new AtomicInteger(2)));
        
        
        SerializableObject serializableResultObject = (SerializableObject) inMemoryStoreApi.get(CREATED_NAMESPACES[3], TEST_KEY);
        assertEquals(2, serializableResultObject.getSum().get());
        
        int newValue = serializableResultObject.getSum().get() + 5;
        inMemoryStoreApi.put(CREATED_NAMESPACES[3], TEST_KEY, new SerializableObject(new AtomicInteger(newValue)));
        
        SerializableObject finalSerializableResultObject = (SerializableObject) inMemoryStoreApi.get(CREATED_NAMESPACES[3], TEST_KEY);
        assertEquals(7, finalSerializableResultObject.getSum().get());
        
        inMemoryStoreApi.delete(CREATED_NAMESPACES[3], TEST_KEY);
        assertNull(inMemoryStoreApi.get(CREATED_NAMESPACES[3], TEST_KEY));
    }

    /**
     * Test of put and get method, of class InMemoryStoreApi.
     * @throws java.lang.Exception
     */
    @Test
    public void testWhenSomeThreadsPutAndGetValuesConcurrentlyToANamespaceKeyValueThenTheValueRemainsInmutable() throws Exception {
        inMemoryStoreApi.put(CREATED_NAMESPACES[0], TEST_KEY, new SerializableObject(new AtomicInteger(0)));

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREAD_POOL);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    SerializableObject serializableObjectInMemory = (SerializableObject) inMemoryStoreApi.get(CREATED_NAMESPACES[0], TEST_KEY);
                    //be sure we are not incrementing the previous object, in a fact we receive a deep copy but not the current stored data
                    serializableObjectInMemory.getSum().getAndIncrement();
                    inMemoryStoreApi.put(CREATED_NAMESPACES[0], TEST_KEY, new SerializableObject(new AtomicInteger(NUMBER_OF_THREAD_POOL)));
                    serializableObjectInMemory.getSum().getAndIncrement();
                } catch (Exception ex) {
                    Logger.getLogger(NameSpaceStoreTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(SERVICE_AWAIT_TERMINATION_TIME, TimeUnit.SECONDS);
        

         while (true) {
            if (executorService.isTerminated()) {
                SerializableObject resultSerializableObject = (SerializableObject) inMemoryStoreApi.get(CREATED_NAMESPACES[0], TEST_KEY);
                assertEquals(1, inMemoryStoreApi.values(CREATED_NAMESPACES[0]).size());
                //it should be NUMBER_OF_THREAD_POOL since is the number we assign per each thread
                assertEquals(new AtomicInteger(NUMBER_OF_THREAD_POOL).get(), resultSerializableObject.getSum().get());
                break;
            }
        }
    }

    /**
     * Test of delete method, of class InMemoryStoreApi.
     * @throws java.lang.Exception
     */
    @Test
    public void testWhenDeleteMethodIsCalledThenTheProvidedKeyIsRemovedFromTheInMemoryStore() throws Exception {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            inMemoryStoreApi.put(CREATED_NAMESPACES[2], TEST_KEY + i, new SerializableObject(new AtomicInteger(i)));
        }
        assertEquals(NUMBER_OF_THREADS, inMemoryStoreApi.values(CREATED_NAMESPACES[2]).size());

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            inMemoryStoreApi.delete(CREATED_NAMESPACES[2], TEST_KEY + i);
            assertNull(inMemoryStoreApi.get(CREATED_NAMESPACES[2], TEST_KEY + i));
        }

        assertTrue(inMemoryStoreApi.values(CREATED_NAMESPACES[2]).isEmpty());
    }

    /**
     * Test of values method, of class InMemoryStoreApi.
     * @throws java.lang.Exception
     */
    @Test
    public void testWhenSomeThreadsAddKeyValuesConcurrentlyToANameSpaceThenValuesMethodReturnsTheCollectionWithTheRightValues() throws Exception {
        List<String> listOfIds = new CopyOnWriteArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREAD_POOL);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    String threadId = UUID.randomUUID().toString();
                    listOfIds.add(threadId);
                    SerializableObject newSerializableObject = new SerializableObject(new AtomicInteger(NUMBER_OF_THREAD_POOL));
                    newSerializableObject.setThreadId(threadId);
                    inMemoryStoreApi.put(CREATED_NAMESPACES[1], threadId, newSerializableObject);
                } catch (Exception ex) {
                    Logger.getLogger(NameSpaceStoreTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(SERVICE_AWAIT_TERMINATION_TIME, TimeUnit.SECONDS);
        
        while (true) {
            if(executorService.isTerminated()){
                List<String> resultListOfIds = inMemoryStoreApi.values(CREATED_NAMESPACES[1]).stream()
                        .map(v -> ((SerializableObject) v).getThreadId())
                        .collect(Collectors.toList());

                Collections.sort(listOfIds);
                Collections.sort(resultListOfIds);
                assertArrayEquals(listOfIds.toArray(), resultListOfIds.toArray());
                break;
            }
        }
    }
}
