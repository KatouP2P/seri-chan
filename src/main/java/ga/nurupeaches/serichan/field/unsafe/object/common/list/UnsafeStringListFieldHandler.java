package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.common.utils.BufferUtils;
import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeStringListFieldHandler extends UnsafeListFieldHandler<String> {

    @Override
    public int sizeof(String obj){
        return BufferUtils.stringSize(obj);
    }

    @Override
    public void writeToBuffer(String obj, ByteBuffer buffer){
        BufferUtils.writeString(buffer, obj);
    }

    @Override
    public String readFromBuffer(ByteBuffer buffer){
        return BufferUtils.readString(buffer);
    }

}
