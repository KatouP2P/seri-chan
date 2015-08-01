package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeByteListFieldHandler extends UnsafeListFieldHandler<Byte> {

    @Override
    public int sizeof(Byte obj){
        return 1;
    }

    @Override
    public void writeToBuffer(Byte obj, ByteBuffer buffer){
        buffer.put(obj);
    }

    @Override
    public Byte readFromBuffer(ByteBuffer buffer){
        return buffer.get();
    }

}
