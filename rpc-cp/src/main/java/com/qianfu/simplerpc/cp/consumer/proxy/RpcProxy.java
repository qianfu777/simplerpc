package com.qianfu.simplerpc.cp.consumer.proxy;

import java.lang.reflect.Proxy;

/**
 * @author Fu
 * @date 2018/6/13
 */
public class RpcProxy {
    public static <T> T createProxyBean(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceInterface}, new RpcProxyHandler());
    }
}
