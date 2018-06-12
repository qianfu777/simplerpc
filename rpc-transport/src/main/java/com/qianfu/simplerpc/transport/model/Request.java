package com.qianfu.simplerpc.transport.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author Fu
 * @date 2018/6/12
 */
@Data
@AllArgsConstructor
public class Request {
    private Method method;
    private Class clazz;
    private Object[] args;
}
