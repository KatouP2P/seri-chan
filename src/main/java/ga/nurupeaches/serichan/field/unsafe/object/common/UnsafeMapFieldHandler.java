package ga.nurupeaches.serichan.field.unsafe.object.common;

import ga.nurupeaches.common.exception.UncheckedReflectionException;
import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class UnsafeMapFieldHandler<K, V> extends UnsafeFieldHandler<Map<K, V>> {

    private Serializer<K> keySerializer;
    private Serializer<V> valueSerializer;

    @Override
    public Map<K, V> get(Object instance){
        return (Map<K, V>)Serializer.THE_UNSAFE.getObject(instance, offset);
    }

    @Override
    public void set(Object instance, Map<K, V> value){
        Serializer.THE_UNSAFE.putObject(instance, offset, value);
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        try {
            Class<?> mapClass = Class.forName(BufferUtils.readString(buffer));
            if(!Map.class.isAssignableFrom(mapClass)){
                throw new ClassNotFoundException("class didn't subclass list; wrong class?");
            }
            Map<K, V> map = (Map<K, V>)Serializer.THE_UNSAFE.allocateInstance(mapClass);
            int size = buffer.getInt();
            if(map instanceof HashMap){
                Field loadFactor_f = HashMap.class.getDeclaredField("loadFactor");
                // 0.75f is the default load factor
                Serializer.THE_UNSAFE.putFloat(map, Serializer.THE_UNSAFE.objectFieldOffset(loadFactor_f), 0.75f);
            }
            for(int i=0; i < size; i++){
                map.put(keySerializer.deserialize(buffer), valueSerializer.deserialize(buffer));
            }
            set(instance, map);
        } catch (ClassNotFoundException e){
            throw new UncheckedReflectionException("failed to find class", e);
        } catch (ReflectiveOperationException e){
            throw new UncheckedReflectionException(e);
        }
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        Map<K, V> map = get(instance);
        BufferUtils.writeString(buffer, map.getClass().getName()); // write out map class
        buffer.putInt(map.size()); // write out the size
        for(Map.Entry<K, V> entry : map.entrySet()){
            // loop through and write each entry
            buffer.put((ByteBuffer)keySerializer.serialize(entry.getKey(), false).flip());
            buffer.put((ByteBuffer) valueSerializer.serialize(entry.getValue(), false).flip());
        }
    }

    @Override
    public int size(Object instance){
        int size = Integer.BYTES;
        Map<K, V> map = get(instance);
        for(Map.Entry<K, V> entry : map.entrySet()){
            size += keySerializer.sizeOf(entry.getKey());
            size += valueSerializer.sizeOf(entry.getValue());
        }
        return BufferUtils.stringSize(map.getClass().getName()) + size;
    }


    @Override
    public void initialize(Field field){
        super.initialize(field);
        Type[] types = ((ParameterizedType)field.getGenericType()).getActualTypeArguments();
        keySerializer = (Serializer<K>)Serializer.getSerializer((Class<?>)types[0]);
        valueSerializer = (Serializer<V>)Serializer.getSerializer((Class<?>)types[1]);
    }


}
