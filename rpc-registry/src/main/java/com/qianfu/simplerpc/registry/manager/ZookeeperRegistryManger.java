package com.qianfu.simplerpc.registry.manager;

import com.qianfu.simplerpc.registry.enums.LoadBalancingEnum;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.File;
import java.util.List;

/**
 * @author Fu
 * @date 2018/6/11
 */
public class ZookeeperRegistryManger implements RegistryManager {
    private LoadBalancingEnum loadBalancing = LoadBalancingEnum.ORDER;
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    private CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .connectionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .namespace("/rpc")
            .build();
    
    public ZookeeperRegistryManger() {
        client.start();
    }
    
    public ZookeeperRegistryManger(LoadBalancingEnum loadBalancing) {
        this();
        this.loadBalancing = loadBalancing;
    }
    
    @Override
    public void register(String serviceName, String url) throws Exception {
        client.create().forPath(buildPath(serviceName, url));
    }
    
    @Override
    public void cancel(String serviceName, String url) throws Exception {
        client.delete().forPath(buildPath(serviceName, url));
    }
    
    @Override
    public String getUrl(String serviceName) throws Exception {
        String servicePath = File.separator + serviceName;
        List<String> serviceUrlList = client.getChildren().forPath(servicePath);
        switch (loadBalancing) {
            case ORDER:
                return serviceUrlList.get(0);
            default:
                return serviceUrlList.get(0);
        }
    }
    
    private String buildPath(String serviceName, String url) {
        return File.separator + serviceName + File.separator + url;
    }
}
