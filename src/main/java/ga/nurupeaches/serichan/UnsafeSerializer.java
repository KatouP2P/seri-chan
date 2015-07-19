package ga.nurupeaches.serichan;

import java.nio.ByteBuffer;

public class UnsafeSerializer<T extends Transmittable> implements Serializer<T> {

    private final Cache cache;

    UnsafeSerializer(Class<T> klass){
        cache = new Cache();
        cache.populateCache(klass);
    }

    public ByteBuffer serialize(Transmittable transmittable) throws SerializationException {
        return null;
    }

    public T deserialize(ByteBuffer buffer) throws SerializationException {
        return null;
    }

}
