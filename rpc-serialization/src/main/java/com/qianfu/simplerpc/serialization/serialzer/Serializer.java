package com.qianfu.simplerpc.serialization.serialzer;

/**
 * @author Fu
 * @date 2018/6/1
 */
public interface Serializer {
    byte[] serialize(Object o);
    
    <T> T deserialize(byte[] bytes, Class<T> targetClass);
}
