package ga.nurupeaches.serichan.field.unsafe.object;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.Transmittable;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeTransmittableFieldHandler<T extends Transmittable> extends UnsafeFieldHandler<T> {

    private Serializer serializer;

    @Override
    public T get(Object instance){
        return (T)Serializer.THE_UNSAFE.getObject(instance, offset);
    }

    @Override
    public void set(Object instance, T value){
        Serializer.THE_UNSAFE.putObject(instance, offset, value);
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        checkSerializer();
        set(instance, (T) serializer.deserialize(buffer));
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        checkSerializer();
        // FOR THE LOVE OF GOD NEVER FORGET CALLING FLIP()
        buffer.put((ByteBuffer)serializer.serialize(get(instance), false).flip());
    }

    @Override
    public int size(Object instance){
        checkSerializer();
        return serializer.sizeOf(get(instance));
    }

    private final void checkSerializer(){
        if(serializer == null){
            serializer = Serializer.getSerializer((Class<? extends Transmittable>)field.getType());
        }
    }

}
