package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeShortFieldHandler extends UnsafeFieldHandler<Short> {

    @Override
    public Short get(Object instance){
        return Serializer.THE_UNSAFE.getShort(instance, offset);
    }

    @Override
    public void set(Object instance, Short value){
        Serializer.THE_UNSAFE.putShort(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        buffer.putShort(get(instance));
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, buffer.getShort());
    }

    @Override
    public int size(Object instance){
        return Short.BYTES;
    }

}
