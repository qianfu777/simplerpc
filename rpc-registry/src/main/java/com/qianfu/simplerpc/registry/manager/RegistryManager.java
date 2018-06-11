package com.qianfu.simplerpc.registry.manager;

/**
 * @author Fu
 * @date 2018/6/11
 */
public interface RegistryManager {
    void register(String serviceName, String url) throws Exception;
    
    void cancel(String serviceName, String url) throws Exception;
    
    String getUrl(String serviceName) throws Exception;
}
