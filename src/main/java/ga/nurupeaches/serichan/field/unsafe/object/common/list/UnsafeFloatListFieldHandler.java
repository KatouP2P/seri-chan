package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeFloatListFieldHandler extends UnsafeListFieldHandler<Float> {

    @Override
    public int sizeof(Float obj){
        return Float.BYTES;
    }

    @Override
    public void writeToBuffer(Float obj, ByteBuffer buffer){
        buffer.putFloat(obj);
    }

    @Override
    public Float readFromBuffer(ByteBuffer buffer){
        return buffer.getFloat();
    }

}
