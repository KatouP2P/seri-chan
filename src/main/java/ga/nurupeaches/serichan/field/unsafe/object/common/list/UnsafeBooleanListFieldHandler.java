package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeBooleanListFieldHandler extends UnsafeListFieldHandler<Boolean> {

    @Override
    public int sizeof(Boolean obj){
        return 1;
    }

    @Override
    public void writeToBuffer(Boolean obj, ByteBuffer buffer){
        buffer.put((byte) (obj ? 1 : 0));
    }

    @Override
    public Boolean readFromBuffer(ByteBuffer buffer){
        return buffer.get() == 1;
    }

}
