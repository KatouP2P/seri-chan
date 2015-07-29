package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeLongFieldHandler extends UnsafeFieldHandler<Long> {

    @Override
    public Long get(Object instance){
        return Serializer.THE_UNSAFE.getLong(instance, offset);
    }

    @Override
    public void set(Object instance, Long value){
        Serializer.THE_UNSAFE.putLong(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        buffer.putLong(get(instance));
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, buffer.getLong());
    }

    @Override
    public int size(Object instance){
        return Long.BYTES;
    }

}
