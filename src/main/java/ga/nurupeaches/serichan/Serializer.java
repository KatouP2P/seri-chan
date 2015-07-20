package ga.nurupeaches.serichan;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface Serializer<T extends Transmittable> {

    public static final Logger LOGGER = Logger.getLogger(Serializer.class.getName());
    static final Map<Class<? extends Transmittable>, Serializer<?>> SERIALIZERS = new HashMap<>(5);

    // They'll never know what hit them.
    // NEVER DO THIS IN ACTUAL PRODUCTION CODE PLS.
    public static Unsafe unsafe = new Object(){

        Unsafe unsafe = null; {
            try {
                Field theUnsafe_f = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe_f.setAccessible(true);
                unsafe = (Unsafe) theUnsafe_f.get(null);
            } catch (ReflectiveOperationException e){
                LOGGER.log(Level.WARNING, "Failed to get Unsafe instance.");
            }
        }

    }.unsafe;

    public static Serializer getSerializer(Class<? extends Transmittable> klass){
        Serializer<?> serializer = SERIALIZERS.get(klass);
        if(serializer == null){
            if(unsafe != null){
                serializer = new UnsafeSerializer<>(klass);
            } else {
                serializer = new ReflectionSerializer<>(klass);
            }
            SERIALIZERS.put(klass, serializer);
        }
        return serializer;
    }

    public ByteBuffer serialize(Transmittable transmittable) throws SerializationException;

    public T deserialize(ByteBuffer buffer) throws SerializationException;

}
