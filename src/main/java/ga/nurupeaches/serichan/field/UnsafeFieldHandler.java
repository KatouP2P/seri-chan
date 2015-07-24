package ga.nurupeaches.serichan.field;

import ga.nurupeaches.common.exception.SerializationException;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.unsafe.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class UnsafeFieldHandler<T> implements FieldHandler<T> {

    protected long offset;
    private static final Map<Class<?>, Class<? extends UnsafeFieldHandler>> LOOKUP_MAP = new HashMap<>();

    static {
        LOOKUP_MAP.put(int.class, UnsafeIntFieldHandler.class);
        LOOKUP_MAP.put(byte.class, UnsafeByteFieldHandler.class);
        LOOKUP_MAP.put(char.class, UnsafeCharFieldHandler.class);
        LOOKUP_MAP.put(float.class, UnsafeFloatFieldHandler.class);
        LOOKUP_MAP.put(double.class, UnsafeDoubleFieldHandler.class);
        LOOKUP_MAP.put(boolean.class, UnsafeBooleanFieldHandler.class);
        LOOKUP_MAP.put(long.class, UnsafeLongFieldHandler.class);
    }

    public static UnsafeFieldHandler<?> newFieldHandler(Class<?> type){
        Class<? extends UnsafeFieldHandler> handlerClass = UnsafeObjectFieldHandler.class; // default
        if(type.isPrimitive())
            handlerClass = LOOKUP_MAP.get(type);

        try {
            return (UnsafeFieldHandler<?>) Serializer.THE_UNSAFE.allocateInstance(handlerClass);
        } catch (InstantiationException e){
            throw new SerializationException(e);
        }
    }

    public void _init(Field field){
        offset = Serializer.THE_UNSAFE.objectFieldOffset(field);
    }

}
