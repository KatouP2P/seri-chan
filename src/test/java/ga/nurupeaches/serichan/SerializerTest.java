package ga.nurupeaches.serichan;

import junit.framework.TestCase;
import org.junit.Test;

import java.nio.ByteBuffer;

public class SerializerTest extends TestCase {

    private final Serializer serializer = Serializer.getSerializer(SimpleTransmittableObject.class);

    @Test
    public void testSerializer(){
        SimpleTransmittableObject obj = new SimpleTransmittableObject(1337, Long.MAX_VALUE, "Hello, world!");
        ByteBuffer buffer = serializer.serialize(obj);
        assert buffer.capacity() == obj.getSize() + 3;
    }

}
