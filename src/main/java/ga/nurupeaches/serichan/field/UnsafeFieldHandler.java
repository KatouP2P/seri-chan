package ga.nurupeaches.serichan.field;

import ga.nurupeaches.common.exception.SerializationException;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.unsafe.object.UnsafeObjectFieldHandler;
import ga.nurupeaches.serichan.field.unsafe.object.UnsafeStringFieldHandler;
import ga.nurupeaches.serichan.field.unsafe.primitive.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class UnsafeFieldHandler<T> implements FieldHandler<T> {

    protected Field field;
    protected long offset;
    private static final Map<Class<?>, Class<? extends UnsafeFieldHandler>> LOOKUP_MAP = new HashMap<>();

    static {
        LOOKUP_MAP.put(int.class, UnsafeIntFieldHandler.class);
        LOOKUP_MAP.put(byte.class, UnsafeByteFieldHandler.class);
        LOOKUP_MAP.put(char.class, UnsafeCharFieldHandler.class);
        LOOKUP_MAP.put(long.class, UnsafeLongFieldHandler.class);
        LOOKUP_MAP.put(float.class, UnsafeFloatFieldHandler.class);
        LOOKUP_MAP.put(double.class, UnsafeDoubleFieldHandler.class);
        LOOKUP_MAP.put(String.class, UnsafeStringFieldHandler.class);
        LOOKUP_MAP.put(boolean.class, UnsafeBooleanFieldHandler.class);
    }

    public static UnsafeFieldHandler<?> newFieldHandler(Class<?> type){
        Class<? extends UnsafeFieldHandler> handlerClass = LOOKUP_MAP.get(type); // default
        if(handlerClass == null){
            handlerClass = UnsafeObjectFieldHandler.class;
        }

        try {
            return (UnsafeFieldHandler<?>) Serializer.THE_UNSAFE.allocateInstance(handlerClass);
        } catch (InstantiationException e){
            throw new SerializationException(e);
        }
    }

    public void _init(Field field){
        this.field = field;
        offset = Serializer.THE_UNSAFE.objectFieldOffset(field);
    }

    @Override
    public Field getField(){
        return field;
    }

    @Override
    public long getOffset(){
        return offset;
    }

}
