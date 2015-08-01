package ga.nurupeaches.serichan.field.unsafe.object.common.list;

import ga.nurupeaches.serichan.field.unsafe.object.common.UnsafeListFieldHandler;

import java.nio.ByteBuffer;

public class UnsafeCharListFieldHandler extends UnsafeListFieldHandler<Character> {

    @Override
    public int sizeof(Character obj){
        return Character.BYTES;
    }

    @Override
    public void writeToBuffer(Character obj, ByteBuffer buffer){
        buffer.putChar(obj);
    }

    @Override
    public Character readFromBuffer(ByteBuffer buffer){
        return buffer.getChar();
    }

}
