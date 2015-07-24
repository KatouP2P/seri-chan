package ga.nurupeaches.serichan;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.field.FieldHandler;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Serializer<T extends Transmittable> {

    public static final Logger LOGGER = Logger.getLogger("Seri-chan");
    private static final Map<Class<? extends Transmittable>, Serializer<?>> SERIALIZERS = new HashMap<>();
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

    public static Serializer<?> getSerializer(Class<? extends Transmittable> klass){
        Serializer<?> serializer;
        if((serializer = SERIALIZERS.get(klass)) == null){
            serializer = new Serializer<>(klass);
            SERIALIZERS.put(klass, serializer);
        }
        return serializer;
    }

    private final Cache cache = new Cache();

    protected Serializer(Class<T> klass){
        cache.populateCache(klass);
    }

    public ByteBuffer serialize(Transmittable transmittable) throws SerializationException{
        // Initialize the buffer we're serializing to.
        ByteBuffer buffer = ByteBuffer.allocate(transmittable.getSize() +
                BufferUtils.stringSize(transmittable.getClass().getName()));
        // Write the class name.
        BufferUtils.writeString(buffer, transmittable.getClass().getName());
        // Get the list of field handlers.
        Collection<FieldHandler<?>> handlers = cache.getFieldHandlers();
        // Put values into the buffer.
        for(FieldHandler<?> handler : handlers){
            switch(handler.getType()){
                case BOOLEAN:
                    buffer.put((byte) ((boolean) handler.get(transmittable) ? 1 : 0));
                    break;
                case BYTE:
                    buffer.put((byte) handler.get(transmittable));
                    break;
                case CHAR:
                    buffer.putChar((char)handler.get(transmittable));
                    break;
                case DOUBLE:
                    buffer.putDouble((double) handler.get(transmittable));
                    break;
                case FLOAT:
                    buffer.putFloat((float) handler.get(transmittable));
                    break;
                case INT:
                    buffer.putInt((int) handler.get(transmittable));
                    break;
                case LONG:
                    buffer.putLong((long) handler.get(transmittable));
                    break;
                case OBJECT:
                    Object o = handler.get(transmittable);
                    if(o instanceof Transmittable){
                        buffer.put(serialize((Transmittable)o));
                    }
                    break;
                case SHORT:
                    buffer.putShort((short) handler.get(transmittable));
                    break;
            }
        }
        return buffer;
    }

    public static Transmittable deserialize(ByteBuffer buffer) throws SerializationException {
        Transmittable transmittable;
        String className = BufferUtils.readString(buffer);
        Class<? extends Transmittable> klass;
        try {
            klass = Class.forName(className).asSubclass(Transmittable.class);
            if(THE_UNSAFE != null){
                transmittable = (Transmittable)THE_UNSAFE.allocateInstance(klass);
            } else {
                transmittable = klass.getConstructor().newInstance();
            }

        } catch (ClassNotFoundException | InstantiationException e){
            throw new SerializationException(e);
        } catch (ReflectiveOperationException e){
            throw new SerializationException("class mis-match?", e);
        }

        return null; // send help.
    }

}
