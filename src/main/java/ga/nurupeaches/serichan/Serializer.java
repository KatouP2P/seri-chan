package ga.nurupeaches.serichan;

import ga.nurupeaches.common.exception.SerializationException;
import ga.nurupeaches.common.exception.UncheckedReflectionException;
import ga.nurupeaches.common.utils.BufferUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface Serializer {

    public static final Logger LOGGER = Logger.getLogger("Seri-chan");
    public static final Map<Class<?>, Serializer> SERIALIZERS = new HashMap<>();
    // They'll never know what hit them.
    public static final Unsafe THE_UNSAFE = new Object(){

        Unsafe unsafe = null; {
            try {
                Field theUnsafe_f = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe_f.setAccessible(true);
                unsafe = (Unsafe)theUnsafe_f.get(null);
            } catch (ReflectiveOperationException e){
                Serializer.LOGGER.log(Level.WARNING, "Failed to get Unsafe instance.");
            }
        }

    }.unsafe;

    public static Serializer findSerializer(ByteBuffer buffer){
        Serializer serializer;
        String className = BufferUtils.readString(buffer);
        Class<? extends Transmittable> klass;
        try {
            klass = Class.forName(className).asSubclass(Transmittable.class);
            serializer = SERIALIZERS.get(klass);
        } catch (ClassNotFoundException e){
            throw new UncheckedReflectionException(e);
        }
        return serializer;
    }

    public static Serializer getSerializer(Class<? extends Transmittable> klass){
        Serializer serializer;
        if((serializer = SERIALIZERS.get(klass)) == null){
            serializer = new DefaultSerializer<>(klass);
            SERIALIZERS.put(klass, serializer);
        }
        return serializer;
    }

    public default ByteBuffer serialize(Object transmittable) throws SerializationException {
        return serialize(transmittable, true);
    }

    public ByteBuffer serialize(Object transmittable, boolean includeClassName) throws SerializationException;
    public Object deserialize(ByteBuffer buffer) throws SerializationException;
    public Class<?> getSerializingClass();
    public Cache getCache();

}
