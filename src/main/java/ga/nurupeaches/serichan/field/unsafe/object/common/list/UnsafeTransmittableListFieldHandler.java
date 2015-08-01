package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.Transmittable;
import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;

public class UnsafeTransmittableListFieldHandler<T extends Transmittable> extends UnsafeListFieldHandler<T> {

    private Serializer serializer;

    @Override
    public void writeToBuffer(Transmittable obj, ByteBuffer buffer){
        checkSerializer();
        buffer.put(serializer.serialize(obj, false));
    }

    @Override
    public T readFromBuffer(ByteBuffer buffer){
        checkSerializer();
        return (T)serializer.deserialize(buffer);
    }

    @Override
    public int sizeof(Transmittable obj){
        checkSerializer();
        return serializer.getCache().getSizeOfObject(obj);
    }

    private final void checkSerializer(){
        if(serializer == null){
            ParameterizedType type = (ParameterizedType)field.getGenericType();
            serializer = Serializer.getSerializer((Class<? extends Transmittable>)type.getActualTypeArguments()[0]);
        }
    }

}
