package ga.nurupeaches.serichan.field.unsafe.object.common;

import ga.nurupeaches.common.exception.UncheckedReflectionException;
import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.Transmittable;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class UnsafeListFieldHandler<T extends Transmittable> extends UnsafeFieldHandler<List<T>> {

    private Serializer serializer;

    @Override
    public List<T> get(Object instance){
        return (List<T>)Serializer.THE_UNSAFE.getObject(instance, offset);
    }

    @Override
    public void set(Object instance, List<T> value){
        Serializer.THE_UNSAFE.putObject(instance, offset, value);
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        checkSerializer();

        try {
            Class<?> listClass = Class.forName(BufferUtils.readString(buffer));
            if(!List.class.isAssignableFrom(listClass)){
                throw new ClassNotFoundException("class didn't subclass list; wrong class?");
            }
            List list = (List)Serializer.THE_UNSAFE.allocateInstance(listClass);
            int size = buffer.getInt();
            // Specific to ArrayList since the elementData can never be null
            if(list instanceof ArrayList){
                Field elementData_f = ArrayList.class.getDeclaredField("elementData");
                Serializer.THE_UNSAFE.putObject(list, Serializer.THE_UNSAFE.objectFieldOffset(elementData_f), new Object[size]);
            }
            for(int i=0; i < size; i++){
                list.add(serializer.deserialize(buffer));
            }
            set(instance, list);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            throw new UncheckedReflectionException("failed to find class", e);
        } catch (ReflectiveOperationException e){
            e.printStackTrace();
            throw new UncheckedReflectionException(e);
        }
    }

    @Override
    public void write(Object instance, ByteBuffer buffer){
        checkSerializer();
        List<T> list = get(instance);
        BufferUtils.writeString(buffer, list.getClass().getName()); // write out list class
        buffer.putInt(list.size()); // write out the size
        for(T obj : list){
            // loop through and write each element
            buffer.put((ByteBuffer)serializer.serialize(obj, false).flip());
        }
    }

    @Override
    public int size(Object instance){
        checkSerializer();
        List<T> list = get(instance);
        int size = Integer.BYTES;
        for(T obj : list){
            size += serializer.getCache().getSizeOfObject(obj);
        }
        return BufferUtils.stringSize(list.getClass().getName()) + size;
    }

    private final void checkSerializer(){
        if(serializer == null){
            ParameterizedType type = (ParameterizedType)field.getGenericType();
            serializer = Serializer.getSerializer((Class<? extends Transmittable>)type.getActualTypeArguments()[0]);
        }
    }

}
