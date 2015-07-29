package ga.nurupeaches.serichan;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.field.FieldHandler;

import java.nio.ByteBuffer;
import java.util.Collection;

public class DefaultSerializer<T extends Transmittable> implements Serializer {

    private final Class<T> transmittableClass;
    private final Cache cache = new Cache();

    protected DefaultSerializer(Class<T> klass){
        cache.populateCache(klass);
        transmittableClass = klass;
    }

    @Override
    public ByteBuffer serialize(Object transmittable, boolean includeClassName) throws SerializationException{
        if(!(transmittable instanceof Transmittable)) throw new IllegalArgumentException("attempted to serialize " +
                "non-transmittable object");

        // Get the list of field handlers.
        Collection<FieldHandler<?>> handlers = cache.getFieldHandlers();
        // Store a size for the buffer; add the require size if we're including the class name.
        int sizeToAllocate = cache.getSizeOfObject(transmittable);
        if(includeClassName) sizeToAllocate += BufferUtils.stringSize(transmittable.getClass().getName());
        // Allocate buffer
        ByteBuffer buffer = ByteBuffer.allocate(sizeToAllocate);
        // Write the class name. (Optional)
        if(includeClassName){
            BufferUtils.writeString(buffer, transmittable.getClass().getName());
        }
        // Put values into the buffer.
        for(FieldHandler handler : handlers){
//            System.out.println("writing " + handler.getField().getType().getName() + " for " + handler.getField().getName() + " in class " + handler.getField().getDeclaringClass().getName());
            handler.write(transmittable, buffer);
        }
        return buffer;
    }

    @Override
    public Object deserialize(ByteBuffer buffer) throws SerializationException{
        Transmittable transmittable;
        try {
            if(THE_UNSAFE != null){
                transmittable = (Transmittable) THE_UNSAFE.allocateInstance(transmittableClass);
            } else {
                transmittable = transmittableClass.getConstructor().newInstance();
            }
        } catch (ReflectiveOperationException e){
            throw new SerializationException(e);
        }

        // Okay, this is typically really bad practice, but we have to "drop" the generic part since
        // javac would complain about type mis-match when calling FieldHandler.set since it would expect value
        // to be of type ? (which we can't get during runtime since Java drops generics at compile-time.
        for(FieldHandler handler : cache.getFieldHandlers()){
//            System.out.println("reading " + handler.getField().getType().getName() + " for " + handler.getField().getName() + " in class " + handler.getField().getDeclaringClass().getName());
            handler.read(transmittable, buffer);
        }
        return transmittable; // send help.
    }

    @Override
    public Cache getCache(){
        return cache;
    }

    @Override
    public Class<?> getSerializingClass(){
        return transmittableClass;
    }
}
