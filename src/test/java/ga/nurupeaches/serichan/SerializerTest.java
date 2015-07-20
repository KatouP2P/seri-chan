package ga.nurupeaches.serichan;

import ga.nurupeaches.common.utils.BufferUtils;
import junit.framework.TestCase;
import org.junit.Test;

import java.nio.ByteBuffer;

public class SerializerTest extends TestCase {

    private final Serializer serializer = Serializer.getSerializer(SimpleTransmittableObject.class);

    @Test
    public void testSerializer() throws Exception {
        SimpleTransmittableObject obj = new SimpleTransmittableObject(1337, Long.MAX_VALUE, "Hello, world!");
        ByteBuffer buffer = serializer.serialize(obj);
        buffer.flip();

        Serializer theSerializer = Serializer.getSerializer(Class.forName(BufferUtils.readString(buffer)).asSubclass(Transmittable.class));
        Transmittable in = theSerializer.deserialize(buffer);
        assert obj.equals(in);
    }

}
