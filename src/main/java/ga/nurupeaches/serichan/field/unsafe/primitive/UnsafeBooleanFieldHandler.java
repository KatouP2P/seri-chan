package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeBooleanFieldHandler extends UnsafeFieldHandler<Boolean> {

    @Override
    public Boolean get(Object instance){
        return Serializer.THE_UNSAFE.getBoolean(instance, offset);
    }

    @Override
    public void set(Object instance, Boolean value){
        Serializer.THE_UNSAFE.putBoolean(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        buffer.put((byte) (get(instance) ? 1 : 0));
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, buffer.get() == 1);
    }

    @Override
    public int size(Object instance){
        return Byte.BYTES;
    }

}
