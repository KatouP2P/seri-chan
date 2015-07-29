package ga.nurupeaches.serichan.field.unsafe.object;

import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeStringFieldHandler extends UnsafeFieldHandler<String> {

    @Override
    public String get(Object instance){
        return (String)Serializer.THE_UNSAFE.getObject(instance, offset);
    }

    @Override
    public void set(Object instance, String value){
        Serializer.THE_UNSAFE.putObject(instance, offset, value);
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        set(instance, BufferUtils.readString(buffer));
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        BufferUtils.writeString(buffer, get(instance));
    }

    @Override
    public int size(Object instance){
        return BufferUtils.stringSize(get(instance));
    }

}
