package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeFloatFieldHandler extends UnsafeFieldHandler<Float> {

    @Override
    public Float get(Object instance){
        return Serializer.THE_UNSAFE.getFloat(instance, offset);
    }

    @Override
    public void set(Object instance, Float value){
        Serializer.THE_UNSAFE.putFloat(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        buffer.putFloat(get(instance));
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, buffer.getFloat());
    }

    @Override
    public int size(Object instance){
        return Float.BYTES;
    }

}
