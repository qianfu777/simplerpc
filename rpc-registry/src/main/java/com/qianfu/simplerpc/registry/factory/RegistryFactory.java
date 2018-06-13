package com.qianfu.simplerpc.registry.factory;

import com.qianfu.simplerpc.registry.manager.RegistryManager;

/**
 * @author Fu
 * @date 2018/6/11
 */
public class RegistryFactory {
    public static RegistryManager getInstance(Class<? extends RegistryManager> clazz) {
        RegistryManager manager = null;
        try {
            manager = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return manager;
    }
}
