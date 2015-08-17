package ga.nurupeaches.serichan.field.unsafe.object;

import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.Transmittable;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class UnsafeArrayFieldHandler<T extends Transmittable> extends UnsafeFieldHandler<T[]> {

    private Serializer serializer;

    @Override
    public T[] get(Object instance){
        return (T[]) Serializer.THE_UNSAFE.getObject(instance, offset);
    }

    @Override
    public void set(Object instance, T[] value){
        Serializer.THE_UNSAFE.putObject(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        T[] arr = get(instance);
        buffer.putInt(arr.length); // write out array length
        for(T elem : arr){
            if(elem == null){
                buffer.put((byte)-1);
            } else {
                buffer.put(serializer.serialize(elem, false));
            }
        }
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        T[] arr = (T[])Array.newInstance(serializer.getSerializingClass(), buffer.getInt());
        for(int i=0; i < arr.length; i++){
            if(BufferUtils.peek(buffer) == -1){
                arr[i] = null;
            } else {
                arr[i] = (T)serializer.deserialize(buffer);
            }
        }
        set(instance, arr);
    }

    @Override
    public int size(Object instance){
        T[] arr = get(instance);
        int size = Integer.BYTES;
        for(T elem : arr){
            if(elem == null){
                size += Byte.BYTES;
            } else {
                size += serializer.sizeOf(elem);
            }
        }
        return size;
    }

    @Override
    public void initialize(Field field){
        super.initialize(field);
        serializer = Serializer.getSerializer((Class<? extends Transmittable>)field.getType().getComponentType());
    }

}