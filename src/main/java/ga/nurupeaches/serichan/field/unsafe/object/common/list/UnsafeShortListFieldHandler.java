package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeShortListFieldHandler extends UnsafeListFieldHandler<Short> {

    @Override
    public int sizeof(Short obj){
        return Short.BYTES;
    }

    @Override
    public void writeToBuffer(Short obj, ByteBuffer buffer){
        buffer.putShort(obj);
    }

    @Override
    public Short readFromBuffer(ByteBuffer buffer){
        return buffer.getShort();
    }

}
