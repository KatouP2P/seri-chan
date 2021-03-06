package ga.nurupeaches.serichan.field;

import ga.nurupeaches.common.exception.SerializationException;
import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.Transmittable;
import ga.nurupeaches.serichan.field.unsafe.object.UnsafeArrayFieldHandler;
import ga.nurupeaches.serichan.field.unsafe.object.UnsafeStringFieldHandler;
import ga.nurupeaches.serichan.field.unsafe.object.UnsafeTransmittableFieldHandler;
import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;
import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeMapFieldHandler;
import ga.nurupeaches.serichan.field.unsafe.primitive.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UnsafeFieldHandler<T> implements FieldHandler<T> {

    private static final Map<Class<?>, Class<? extends UnsafeFieldHandler>> LOOKUP_MAP = new HashMap<>();

    // A "extension" to the look-up map; used for Lists and such.
    private static final Map<Class<?>, Class<? extends UnsafeFieldHandler>> EXTENDED_MAP = new HashMap<>();

    protected Field field;
    protected long offset;

    static {
        LOOKUP_MAP.put(int.class, UnsafeIntFieldHandler.class);
        LOOKUP_MAP.put(byte.class, UnsafeByteFieldHandler.class);
        LOOKUP_MAP.put(char.class, UnsafeCharFieldHandler.class);
        LOOKUP_MAP.put(long.class, UnsafeLongFieldHandler.class);
        LOOKUP_MAP.put(float.class, UnsafeFloatFieldHandler.class);
        LOOKUP_MAP.put(double.class, UnsafeDoubleFieldHandler.class);
        LOOKUP_MAP.put(boolean.class, UnsafeBooleanFieldHandler.class);
        LOOKUP_MAP.put(String.class, UnsafeStringFieldHandler.class);

        EXTENDED_MAP.put(Map.class, UnsafeMapFieldHandler.class);
        EXTENDED_MAP.put(List.class, UnsafeListFieldHandler.class);
    }

    public static FieldHandler<?> newFieldHandler(Class<?> type){
        Class<? extends UnsafeFieldHandler> handlerClass = LOOKUP_MAP.get(type); // default
        if(handlerClass == null){
            if(Transmittable.class.isAssignableFrom(type)){
                handlerClass = UnsafeTransmittableFieldHandler.class;
            } else {
                if(type.isArray()){
                    if(Transmittable.class.isAssignableFrom(type.getComponentType())){
                        handlerClass = UnsafeArrayFieldHandler.class;
                    }
                    // goes to check if handlerClass is null
                } else {
                    for(Map.Entry<Class<?>, Class<? extends UnsafeFieldHandler>> entry : EXTENDED_MAP.entrySet()){
                        if(entry.getKey().isAssignableFrom(type)){
                            handlerClass = entry.getValue();
                            break;
                        }
                    }
                }

                if(handlerClass == null){
                    return new NoFieldHandler();
                }
            }
        }

        try {
            return (UnsafeFieldHandler<?>) Serializer.THE_UNSAFE.allocateInstance(handlerClass);
        } catch (InstantiationException e){
            throw new SerializationException(e);
        }
    }

    @Override
    public void initialize(Field field){
        this.field = field;
        offset = Serializer.THE_UNSAFE.objectFieldOffset(field);
    }

    @Override
    public Field getField(){
        return field;
    }

    public long getOffset(){
        return offset;
    }

}
