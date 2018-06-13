package com.qianfu.simplerpc.cp.consumer.proxy;

import com.qianfu.simplerpc.registry.factory.RegistryFactory;
import com.qianfu.simplerpc.registry.manager.RegistryManager;
import com.qianfu.simplerpc.registry.manager.ZookeeperRegistryManger;
import com.qianfu.simplerpc.transport.model.Request;
import com.qianfu.simplerpc.transport.model.Response;
import com.qianfu.simplerpc.transport.netty.Client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Fu
 * @date 2018/6/13
 */
public class RpcProxyHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RegistryManager registry = RegistryFactory.getInstance(ZookeeperRegistryManger.class);
        String url = registry.getUrl(method.getDeclaringClass().getName());
        
        Client client = new Client(url);
        Request request = new Request(method, method.getDeclaringClass(), args);
        Response response = client.request(request);
        
        return response.getResult();
    }
  
}
