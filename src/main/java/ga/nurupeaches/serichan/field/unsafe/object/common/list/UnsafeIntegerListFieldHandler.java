package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeIntegerListFieldHandler extends UnsafeListFieldHandler<Integer> {

    @Override
    public int sizeof(Integer obj){
        return Integer.BYTES;
    }

    @Override
    public void writeToBuffer(Integer obj, ByteBuffer buffer){
        buffer.putInt(obj);
    }

    @Override
    public Integer readFromBuffer(ByteBuffer buffer){
        return buffer.getInt();
    }

}
