package ga.nurupeaches.serichan;

import junit.framework.TestCase;
import org.junit.Test;

import java.nio.ByteBuffer;

public class SerializerTest extends TestCase {

    private final Serializer serializer = Serializer.getSerializer(SimpleTransmittableObject.class);

    @Test
    public void testSerializer() throws Exception {
        SimpleTransmittableObject obj = new SimpleTransmittableObject(1337, 13333333333337L, "Hello, world!");
        ByteBuffer buffer = serializer.serialize(obj);
        buffer.flip();

        Transmittable in = Serializer.deserialize(buffer);
        assert obj.equals(in);
    }

}
