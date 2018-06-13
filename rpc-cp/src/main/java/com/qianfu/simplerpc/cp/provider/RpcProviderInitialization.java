package com.qianfu.simplerpc.cp.provider;

import com.qianfu.simplerpc.cp.annoation.Provider;
import com.qianfu.simplerpc.registry.factory.RegistryFactory;
import com.qianfu.simplerpc.registry.manager.RegistryManager;
import com.qianfu.simplerpc.registry.manager.ZookeeperRegistryManger;
import com.qianfu.simplerpc.transport.netty.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.SocketUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author Fu
 * @date 2018/6/13
 */
@Slf4j
public class RpcProviderInitialization implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private RegistryManager registey = RegistryFactory.getInstance(ZookeeperRegistryManger.class);
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @PostConstruct
    public void publishService() {
        Map<String, Object> providerClassMap = applicationContext.getBeansWithAnnotation(Provider.class);
        providerClassMap.values().forEach(v -> v.getClass().getName());
    }
    
    private void register(final String beanName) {
        Thread thread = new Thread(() -> {
            try {
                Server server = new Server();
                
                int port = SocketUtils.findAvailableTcpPort();
                registey.register(beanName, getLocalAddressAndPort(port));
                server.init(port);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        thread.start();
    }
    
    private String getLocalAddressAndPort(int port) throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress() + ":" + port;
    }
}
