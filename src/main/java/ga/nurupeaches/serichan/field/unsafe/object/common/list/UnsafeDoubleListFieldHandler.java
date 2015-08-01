package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeDoubleListFieldHandler extends UnsafeListFieldHandler<Double> {

    @Override
    public int sizeof(Double obj){
        return Double.BYTES;
    }

    @Override
    public void writeToBuffer(Double obj, ByteBuffer buffer){
        buffer.putDouble(obj);
    }

    @Override
    public Double readFromBuffer(ByteBuffer buffer){
        return buffer.getDouble();
    }

}
