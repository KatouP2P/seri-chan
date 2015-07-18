package ga.nurupeaches.serichan;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class Serializer<T extends Transmittable> {

    public static final Logger LOGGER = Logger.getLogger(Serializer.class.getName());
    private static final Map<Class<? extends Transmittable>, Serializer<?>> SERIALIZERS = new HashMap<>(5);

    private final Cache cache;

    private Serializer(Class<? extends Transmittable> klass){
        cache = new Cache();
        cache.populateCache(klass);
    }

    public static Serializer getSerializer(Class<? extends Transmittable> klass){
        Serializer<?> serializer = SERIALIZERS.get(klass);
        if(serializer == null){
            serializer = new Serializer<>(klass);
            SERIALIZERS.put(klass, serializer);
        }
        return serializer;
    }

    public ByteBuffer serialize(Transmittable transmittable) throws SerializationException {
        ByteBuffer buffer = ByteBuffer.allocate(transmittable.getSize());

        for(Field field : cache.fieldCache.values()){
            try {
                FieldType type = FieldType.valueOf(field.getDeclaringClass());
                buffer.put((byte)type.ordinal());
                Object o = field.get(transmittable);

                switch(type){
                    case BOOLEAN:
                        buffer.put((byte)((boolean)o ? 1 : 0));
                        break;
                    case BYTE:
                        buffer.put((byte)o);
                        break;
                    case SHORT:
                        buffer.putShort((short)o);
                        break;
                    case CHAR:
                        buffer.putChar((char)o);
                        break;
                    case INT:
                        buffer.putInt((int)o);
                        break;
                    case LONG:
                        buffer.putLong((long)o);
                        break;
                    case FLOAT:
                        buffer.putFloat((float)o);
                        break;
                    case DOUBLE:
                        buffer.putDouble((double)o);
                        break;
                    case STRING:
                        char[] chars = ((String)o).toCharArray();
                        buffer.putInt(chars.length);
                        for(char c : chars){
                            buffer.putChar(c);
                        }
                        break;
                    case NONPRIMITIVE:
                        if(Transmittable.class.isAssignableFrom(field.getDeclaringClass())){
                            buffer.put(serialize((Transmittable)field.get(transmittable)));
                        }
                        break;
                }
            } catch (Throwable t){
                throw new SerializationException(t);
            }
        }

        return buffer;
    }

    public T deserialize(ByteBuffer buffer){
        return null;
    }



}
