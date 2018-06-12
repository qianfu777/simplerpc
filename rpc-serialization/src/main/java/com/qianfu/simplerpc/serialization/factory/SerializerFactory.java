package com.qianfu.simplerpc.serialization.factory;

import com.qianfu.simplerpc.serialization.serialzer.Serializer;

/**
 * @author Fu
 * @date 2018/6/1
 */
public class SerializerFactory {
    public static Serializer getInstance(Class<? extends Serializer> implementClass) {
        Serializer serializer = null;
        try {
            serializer = implementClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return serializer;
    }
}
