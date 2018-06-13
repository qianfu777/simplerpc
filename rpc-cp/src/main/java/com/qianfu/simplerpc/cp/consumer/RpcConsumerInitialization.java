package com.qianfu.simplerpc.cp.consumer;

import com.qianfu.simplerpc.cp.annoation.Consumer;
import com.qianfu.simplerpc.cp.consumer.proxy.RpcProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

/**
 * @author Fu
 * @date 2018/6/13
 */
public class RpcConsumerInitialization implements BeanPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final Class<?> clazz = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
        
        ReflectionUtils.doWithFields(clazz, field -> {
            Consumer rpcConsumer = field.getAnnotation(Consumer.class);
            if (rpcConsumer != null) {
                Class serviceClass = field.getType();
                Object object = RpcProxy.createProxyBean(serviceClass);
                
                field.setAccessible(true);
                field.set(bean, object);
            }
        });
        return bean;
    }
}
