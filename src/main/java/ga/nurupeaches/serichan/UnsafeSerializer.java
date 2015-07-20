package ga.nurupeaches.serichan;

import ga.nurupeaches.common.utils.BufferUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class UnsafeSerializer<T extends Transmittable> implements Serializer<T> {

    private final Class<T> klass;
    private final Cache cache;

    UnsafeSerializer(Class<T> klass){
        this.klass = klass;
        cache = new Cache();
        cache.populateCache(klass);
    }

    public ByteBuffer serialize(Transmittable transmittable) throws SerializationException {
        String className = transmittable.getClass().getName();
        ByteBuffer buffer = ByteBuffer.allocate(BufferUtils.stringSize(className) + transmittable.getSize() +
                cache.fieldCache.size() * 2); // Add the field types.

        Field[] fields = cache.fieldCache.values().toArray(new Field[0]);
        long offset;
        Field field;
        FieldType type;
        Object o;
        for(int i=0; i < fields.length; i++){
            field = fields[i];
            offset = Serializer.unsafe.objectFieldOffset(field);
            type = FieldType.valueOf(field.getType());
            buffer.put((byte) type.ordinal());

            switch(type){
                case BOOLEAN:
                    o = Serializer.unsafe.getBoolean(transmittable, offset);
                    buffer.put((byte)((boolean)o ? 1 : 0));
                    break;
                case BYTE:
                    o = Serializer.unsafe.getByte(transmittable, offset);
                    buffer.put((byte)o);
                    break;
                case SHORT:
                    o = Serializer.unsafe.getShort(transmittable, offset);
                    buffer.putShort((short)o);
                    break;
                case CHAR:
                    o = Serializer.unsafe.getChar(transmittable, offset);
                    buffer.putChar((char)o);
                    break;
                case INT:
                    o = Serializer.unsafe.getInt(transmittable, offset);
                    buffer.putInt((int)o);
                    break;
                case LONG:
                    o = Serializer.unsafe.getLong(transmittable, offset);
                    buffer.putLong((long)o);
                    break;
                case STRING:
                    o = Serializer.unsafe.getObject(transmittable, offset);
                    char[] chars = ((String)o).toCharArray();
                    buffer.putInt(chars.length);
                    for(char c : chars){
                        buffer.putChar(c);
                    }
                    break;
                case NONPRIMITIVE:
                    o = Serializer.unsafe.getObject(transmittable, offset);
                    if(Transmittable.class.isAssignableFrom(field.getDeclaringClass())){
                        buffer.put(serialize((Transmittable)o));
                    }
                    break;
            }
        }

        return buffer;
    }

    public T deserialize(ByteBuffer buffer) throws SerializationException {
        T obj;
        try {
            obj = (T)Serializer.unsafe.allocateInstance(klass);
        } catch (InstantiationException e){
            throw new SerializationException(e);
        }

        FieldType[] types = FieldType.values();
        Field[] fields = cache.fieldCache.values().toArray(new Field[0]);
        long offset;
        for(int i=0; i < cache.fieldCache.size(); i++){
            try {
                offset = Serializer.unsafe.objectFieldOffset(fields[i]);

                switch(types[buffer.get()]){
                    case BOOLEAN:
                        Serializer.unsafe.putBoolean(obj, offset, buffer.get() == 1);
                        break;
                    case BYTE:
                        Serializer.unsafe.putByte(obj, offset, buffer.get());
                        break;
                    case SHORT:
                        Serializer.unsafe.putShort(obj, offset, buffer.getShort());
                        break;
                    case CHAR:
                        Serializer.unsafe.putChar(obj, offset, buffer.getChar());
                        break;
                    case INT:
                        Serializer.unsafe.putInt(obj, offset, buffer.getInt());
                        break;
                    case LONG:
                        Serializer.unsafe.putLong(obj, offset, buffer.getLong());
                        break;
                    case STRING:
                        Serializer.unsafe.putObject(obj, offset, BufferUtils.readString(buffer));
                        break;
                    case NONPRIMITIVE:
                        if(Transmittable.class.isAssignableFrom(fields[i].getDeclaringClass())){
                            Serializer.unsafe.putObject(obj, offset, deserialize(buffer));
                        }
                        break;
                }
            } catch (Throwable t){
                throw new SerializationException(t);
            }
        }
        return obj;
    }

}
