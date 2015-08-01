package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeLongListFieldHandler extends UnsafeListFieldHandler<Long> {

    @Override
    public int sizeof(Long obj){
        return Long.BYTES;
    }

    @Override
    public void writeToBuffer(Long obj, ByteBuffer buffer){
        buffer.putLong(obj);
    }

    @Override
    public Long readFromBuffer(ByteBuffer buffer){
        return buffer.getLong();
    }

}
