package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeCharFieldHandler extends UnsafeFieldHandler<Character> {

    @Override
    public Character get(Object instance){
        return Serializer.THE_UNSAFE.getChar(instance, offset);
    }

    @Override
    public void set(Object instance, Character value){
        Serializer.THE_UNSAFE.putChar(instance, offset, value);
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        buffer.putChar(get(instance));
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, buffer.getChar());
    }

    @Override
    public int size(Object instance){
        return Character.BYTES;
    }

}

