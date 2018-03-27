/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imks.store.copy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author manuelmerida
 * @param <T>
 */
public class DeepCopy<T> implements Serializable{

    /**
     *
     * @param serializableObject
     * @return
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public T copy(T serializableObject) throws IOException, ClassNotFoundException {
        synchronized(this){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(serializableObject);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        }
    }

}
