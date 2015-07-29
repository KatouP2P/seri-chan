package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeByteFieldHandler extends UnsafeFieldHandler<Byte> {

    @Override
    public Byte get(Object instance){
        return Serializer.THE_UNSAFE.getByte(instance, offset);
    }

    @Override
    public void set(Object instance, Byte value){
        Serializer.THE_UNSAFE.putByte(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        buffer.put(get(instance));
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, buffer.get());
    }

    @Override
    public int size(Object instance){
        return Byte.BYTES;
    }

}
