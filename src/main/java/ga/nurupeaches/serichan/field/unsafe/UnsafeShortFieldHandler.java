package ga.nurupeaches.serichan.field.unsafe;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeShortFieldHandler extends UnsafeFieldHandler<Short> {

    @Override
    public Short get(Object instance){
        return Serializer.THE_UNSAFE.getShort(instance, offset);
    }

    @Override
    public void set(Object instance, Short value){
        Serializer.THE_UNSAFE.putShort(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.SHORT;
    }

}
