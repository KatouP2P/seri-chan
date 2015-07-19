package ga.nurupeaches.serichan;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.logging.Level;

public class ReflectionSerializer<T extends Transmittable> implements Serializer<T> {

    private final Cache cache;

    ReflectionSerializer(Class<T> klass){
        cache = new Cache();
        cache.populateCache(klass);
    }

    @Override
    public ByteBuffer serialize(Transmittable transmittable) throws SerializationException {
        ByteBuffer buffer = ByteBuffer.allocate(Serializer.stringSize(transmittable.getClass().getName()) +
                transmittable.getSize() + cache.fieldCache.size() * 2); // Add the field types.

        Field[] fields = cache.fieldCache.values().toArray(new Field[0]);
        Field field;
        FieldType type;
        Object o;
        for(int i=0; i < cache.fieldCache.size(); i++){
            try {
                field = fields[i];
                type = FieldType.valueOf(field.getType());
                buffer.put((byte)type.ordinal());
                buffer.put((byte)i);
                o = field.get(transmittable);

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

    @Override
    public T deserialize(ByteBuffer buffer){
        int strLen = buffer.getInt();
        char[] charClassName = new char[strLen];
        for(int i=0; i < strLen; i++){
            charClassName[i] = buffer.getChar();
        }

        String className = new String(charClassName);
        Class<? extends Transmittable> objClass;
        try {
            objClass = Class.forName(className).asSubclass(Transmittable.class);

        } catch (ClassNotFoundException e){
            LOGGER.log(Level.WARNING, "Failed to find class " + className);
            return null;
        } catch (ReflectiveOperationException e){
            LOGGER.log(Level.WARNING, "Failed to find class " + className);
        }

        return null;
    }

}
