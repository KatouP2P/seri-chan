package ga.nurupeaches.serichan.field.unsafe.object.common;

import ga.nurupeaches.common.exception.UncheckedReflectionException;
import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.Transmittable;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class UnsafeMapFieldHandler<K extends Transmittable, V extends Transmittable> extends UnsafeFieldHandler<Map<K, V>> {

    private Serializer keySerializer;
    private Serializer valueSerializer;

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
        checkSerializer();

        try {
            Class<?> mapClass = Class.forName(BufferUtils.readString(buffer));
            if(!Map.class.isAssignableFrom(mapClass)){
                throw new ClassNotFoundException("class didn't subclass list; wrong class?");
            }
            Map map = (Map)Serializer.THE_UNSAFE.allocateInstance(mapClass);
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
            e.printStackTrace();
            throw new UncheckedReflectionException("failed to find class", e);
        } catch (ReflectiveOperationException e){
            e.printStackTrace();
            throw new UncheckedReflectionException(e);
        }
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        checkSerializer();
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
        checkSerializer();
        int size = Integer.BYTES;
        Map<K, V> map = get(instance);
        for(Map.Entry<K, V> entry : map.entrySet()){
            size += keySerializer.getCache().getSizeOfObject(entry.getKey());
            size += valueSerializer.getCache().getSizeOfObject(entry.getValue());
        }
        return BufferUtils.stringSize(map.getClass().getName()) + size;
    }

    // If you ask why this does basically the same thing in both loops:
    // From what I've read, getGenericType() can cause a performance impact; so I want to avoid calling it a lot.
    // Micro-optimization is the end of me.
    private final void checkSerializer(){
        if(keySerializer == null){
            ParameterizedType type = (ParameterizedType)field.getGenericType();
            keySerializer = Serializer.getSerializer((Class<? extends Transmittable>)type.getActualTypeArguments()[0]);

            if(valueSerializer == null){
                valueSerializer = Serializer.getSerializer((Class<? extends Transmittable>)type.getActualTypeArguments()[1]);
            }
        }

        if(valueSerializer == null){
            ParameterizedType type = (ParameterizedType)field.getGenericType();
            valueSerializer = Serializer.getSerializer((Class<? extends Transmittable>)type.getActualTypeArguments()[1]);

            if(keySerializer == null){
                keySerializer = Serializer.getSerializer((Class<? extends Transmittable>)type.getActualTypeArguments()[0]);
            }
        }
    }

}
