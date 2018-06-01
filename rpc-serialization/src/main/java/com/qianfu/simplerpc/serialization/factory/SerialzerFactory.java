package com.qianfu.simplerpc.serialization.factory;

import com.qianfu.simplerpc.serialization.serialzer.Serializer;

/**
 * @author Fu
 * @date 2018/6/1
 */
public class SerialzerFactory {
    public Serializer getInstance(Class<? extends Serializer> implclass) {
        Serializer serializer = null;
        try {
            serializer = implclass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return serializer;
    }
}
