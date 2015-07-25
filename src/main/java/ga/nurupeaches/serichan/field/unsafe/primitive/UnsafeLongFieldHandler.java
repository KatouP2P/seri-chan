package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeLongFieldHandler extends UnsafeFieldHandler<Long> {

    @Override
    public Long get(Object instance){
        return Serializer.THE_UNSAFE.getLong(instance, offset);
    }

    @Override
    public void set(Object instance, Long value){
        Serializer.THE_UNSAFE.putLong(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.LONG;
    }

}
