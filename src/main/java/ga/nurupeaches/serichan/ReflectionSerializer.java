package ga.nurupeaches.serichan;

import ga.nurupeaches.common.utils.BufferUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ReflectionSerializer<T extends Transmittable> implements Serializer<T> {

    private final Class<T> klass;
    private final Cache cache;

    ReflectionSerializer(Class<T> klass){
        this.klass = klass;
        cache = new Cache();
        cache.populateCache(klass);
    }

    @Override
    public ByteBuffer serialize(Transmittable transmittable) throws SerializationException {
        String className = transmittable.getClass().getName();
        ByteBuffer buffer = ByteBuffer.allocate(BufferUtils.stringSize(className) + transmittable.getSize() +
                cache.fieldCache.size() * 2); // Add the field types.
        BufferUtils.writeString(buffer, transmittable.getClass().getName());

        Field[] fields = cache.fieldCache.values().toArray(new Field[0]);
        Field field;
        FieldType type;
        Object o;
        for(int i=0; i < cache.fieldCache.size(); i++){
            try {
                field = fields[i];
                o = field.get(transmittable);
                type = FieldType.valueOf(field.getType());
                buffer.put((byte)type.ordinal());

                switch(type){
                    case BOOLEAN:
                        buffer.put((byte) ((boolean) o ? 1 : 0));
                        break;
                    case BYTE:
                        buffer.put((byte) o);
                        break;
                    case SHORT:
                        buffer.putShort((short) o);
                        break;
                    case CHAR:
                        buffer.putChar((char) o);
                        break;
                    case INT:
                        buffer.putInt((int) o);
                        break;
                    case LONG:
                        buffer.putLong((long) o);
                        break;
                    case STRING:
                        BufferUtils.writeString(buffer, (String)o);
                        break;
                    case NONPRIMITIVE:
                        if(Transmittable.class.isAssignableFrom(field.getDeclaringClass())){
                            buffer.put(serialize((Transmittable)o));
                        }
                        break;
                }

                System.out.println(i + ", " + type + ", " + Arrays.toString(buffer.array()));
            } catch (Throwable t){
                throw new SerializationException(t);
            }
        }

        return buffer;
    }

    @Override
    public T deserialize(ByteBuffer buffer) throws SerializationException {
        T obj;
        try {
            obj = klass.newInstance();
        } catch (Throwable t){
            throw new SerializationException(t);
        }

        FieldType types[] = FieldType.values();
        Field[] fields = cache.fieldCache.values().toArray(new Field[0]);
        Field field;
        Object o;
        FieldType type;
        for(int i=0; i < cache.fieldCache.size(); i++){
            try {
                field = fields[i];
                type = types[buffer.get()];

                switch(type){
                    case BOOLEAN:
                        o = buffer.get() == 1;
                        break;
                    case BYTE:
                        o = buffer.get();
                        break;
                    case SHORT:
                        o = buffer.getShort();
                        break;
                    case CHAR:
                        o = buffer.getChar();
                        break;
                    case INT:
                        o = buffer.getInt();
                        break;
                    case LONG:
                        o = buffer.getLong();
                        break;
                    case STRING:
                        o = BufferUtils.readString(buffer);
                        break;
                    case NONPRIMITIVE:
                        if(Transmittable.class.isAssignableFrom(field.getDeclaringClass())){
                            o = deserialize(buffer);
                        } else {
                            o = null;
                        }
                        break;
                    default:
                        o = null;
                        break;
                }

                field.set(obj, o);
            } catch (Throwable t){
                throw new SerializationException(t);
            }
        }
        return obj;
    }

}
