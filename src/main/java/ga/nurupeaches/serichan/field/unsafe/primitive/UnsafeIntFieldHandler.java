package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeIntFieldHandler extends UnsafeFieldHandler<Integer> {

    @Override
    public Integer get(Object instance){
        return Serializer.THE_UNSAFE.getInt(instance, offset);
    }

    @Override
    public void set(Object instance, Integer value){
        Serializer.THE_UNSAFE.putInt(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        buffer.putInt(get(instance));
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, buffer.getInt());
    }

    @Override
    public int size(Object instance){
        return Integer.BYTES;
    }

}
