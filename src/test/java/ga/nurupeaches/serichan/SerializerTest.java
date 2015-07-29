package ga.nurupeaches.serichan;

import ga.nurupeaches.common.utils.BufferUtils;
import junit.framework.TestCase;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SerializerTest extends TestCase {

    private final Serializer serializer = Serializer.getSerializer(SimpleTransmittableObject.class);

    @Test
    public void testSerializer() throws Exception {
        SimpleTransmittableObject obj = new SimpleTransmittableObject(1337, "Hello, world!");
        ByteBuffer buffer = serializer.serialize(obj);
        buffer.flip();
        System.out.println("arr: " + Arrays.toString(buffer.array()));

        Class<?> klass = Class.forName(BufferUtils.readString(buffer));
        assert Transmittable.class.isAssignableFrom(klass);

        Serializer remoteSerializer = Serializer.getSerializer(klass.asSubclass(Transmittable.class));
        assert remoteSerializer == serializer;

        Object in = serializer.deserialize(buffer);
        assert obj.equals(in);
    }

}
