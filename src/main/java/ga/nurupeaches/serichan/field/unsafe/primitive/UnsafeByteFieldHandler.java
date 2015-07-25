package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeByteFieldHandler extends UnsafeFieldHandler<Byte> {

    @Override
    public Byte get(Object instance){
        return Serializer.THE_UNSAFE.getByte(instance, offset);
    }

    @Override
    public void set(Object instance, Byte value){
        Serializer.THE_UNSAFE.putByte(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.BYTE;
    }

}
