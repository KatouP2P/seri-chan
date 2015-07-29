package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeDoubleFieldHandler extends UnsafeFieldHandler<Double> {

    @Override
    public Double get(Object instance){
        return Serializer.THE_UNSAFE.getDouble(instance, offset);
    }

    @Override
    public void set(Object instance, Double value){
        Serializer.THE_UNSAFE.putDouble(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        buffer.putDouble(get(instance));
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, buffer.getDouble());
    }

    @Override
    public int size(Object instance){
        return Double.BYTES;
    }

}
