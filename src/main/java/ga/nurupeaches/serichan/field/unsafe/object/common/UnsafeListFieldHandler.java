package ga.nurupeaches.serichan.field.unsafe.object.common;

import ga.nurupeaches.common.exception.UncheckedReflectionException;
import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class UnsafeListFieldHandler<T> extends UnsafeFieldHandler<List<T>> {

    private Type type;

    @Override
    public List<T> get(Object instance){
        return (List<T>) Serializer.THE_UNSAFE.getObject(instance, offset);
    }

    @Override
    public void set(Object instance, List<T> value){
        Serializer.THE_UNSAFE.putObject(instance, offset, value);
    }

    @Override
    public void read(Object instance, ByteBuffer buffer){
        try {
            Class<?> listClass = Class.forName(BufferUtils.readString(buffer));
            if(!List.class.isAssignableFrom(listClass)){
                throw new ClassNotFoundException("class didn't subclass list; wrong class?");
            }
            List<T> list = (List<T>)Serializer.THE_UNSAFE.allocateInstance(listClass);
            int size = buffer.getInt();
            // Specific to ArrayList since the elementData can never be null
            if(list instanceof ArrayList){
                Field elementData_f = ArrayList.class.getDeclaredField("elementData");
                Serializer.THE_UNSAFE.putObject(list, Serializer.THE_UNSAFE.objectFieldOffset(elementData_f), new Object[size]);
            }
            for(int i=0; i < size; i++){
                list.add((T)type.readFromBuffer(buffer));
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
        List<T> list = get(instance);
        BufferUtils.writeString(buffer, list.getClass().getName()); // write out list class
        buffer.putInt(list.size()); // write out the size
        for(T obj : list){
            // loop through and write each element
            type.writeToBuffer(obj, buffer);
        }
    }

    @Override
    public int size(Object instance){
        List<T> list = get(instance);
        int size = Integer.BYTES;
        for(T obj : list){
            size += type.sizeof(obj);
        }
        return BufferUtils.stringSize(list.getClass().getName()) + size;
    }

    @Override
    public void initialize(Field field){
        super.initialize(field);
        type = Type.getType(field.getType());
    }

}