package com.qianfu.simplerpc.serialization.serialzer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Fu
 * @date 2018/6/1
 */
public class KryoSerializer implements Serializer {
    
    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true); //默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置
        kryo.setRegistrationRequired(false); //默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });
    
    private static Kryo getInstance() {
        return kryoLocal.get();
    }
    
    @Override
    public byte[] serialize(Object o) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        
        Kryo kryo = getInstance();
        kryo.writeClassAndObject(output, o);
        output.flush();
        
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> targetClass) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        
        Kryo kryo = getInstance();
        return (T) kryo.readClassAndObject(input);
    }
}
