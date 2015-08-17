package ga.nurupeaches.serichan;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.field.FieldHandler;

import java.nio.ByteBuffer;
import java.util.Collection;

public class DefaultSerializer<T extends Transmittable> extends Serializer<T> {

    private final Class<T> transmittableClass;
    private final Cache cache = new Cache();

    protected DefaultSerializer(Class<?> klass){
        if(!Transmittable.class.isAssignableFrom(klass)){
            throw new IllegalArgumentException("can't serialize non-Transmittable classes");
        }
        cache.populateCache(klass);
        transmittableClass = (Class<T>)klass;
    }

    @Override
    public ByteBuffer serialize(T transmittable, boolean includeClassName) throws SerializationException{
        if(transmittable == null) throw new IllegalArgumentException("attempted to serialize " +
                "non-transmittable object");

        // Get the list of field handlers.
        Collection<FieldHandler<?>> handlers = cache.getFieldHandlers();
        // Store a size for the buffer; add the require size if we're including the class name.
        int sizeToAllocate = sizeOf(transmittable);
        if(includeClassName) sizeToAllocate += BufferUtils.stringSize(transmittable.getClass().getName());
        // Allocate buffer
        ByteBuffer buffer = ByteBuffer.allocate(sizeToAllocate);
        // Write the class name. (Optional)
        if(includeClassName){
            BufferUtils.writeString(buffer, transmittable.getClass().getName());
        }
        // Put values into the buffer.
        for(FieldHandler<?> handler : handlers){
//            System.out.println("writing " + handler.getField().getType().getName() + " for " + handler.getField().getName() + " in class " + handler.getField().getDeclaringClass().getName());
            handler.write(transmittable, buffer);
        }
        return buffer;
    }

    @Override
    public T deserialize(ByteBuffer buffer) throws SerializationException{
        T transmittable;
        try {
            if(THE_UNSAFE != null){
                transmittable = (T)THE_UNSAFE.allocateInstance(transmittableClass);
            } else {
                transmittable = transmittableClass.getConstructor().newInstance();
            }
        } catch (ReflectiveOperationException e){
            throw new SerializationException(e);
        }

        for(FieldHandler<?> handler : cache.getFieldHandlers()){
//            System.out.println("reading " + handler.getField().getType().getName() + " for " + handler.getField().getName() + " in class " + handler.getField().getDeclaringClass().getName());
            handler.read(transmittable, buffer);
        }
        return transmittable; // send help.
    }

    @Override
    public int sizeOf(Object o){
        int size = 0;
        for(FieldHandler<?> handler : cache.getFieldHandlers()){
            size += handler.size(o);
        }
        return size;
    }

    @Override
    public Class<?> getSerializingClass(){
        return transmittableClass;
    }
}
