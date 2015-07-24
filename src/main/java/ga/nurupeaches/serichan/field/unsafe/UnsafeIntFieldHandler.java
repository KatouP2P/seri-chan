package ga.nurupeaches.serichan.field.unsafe;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeIntFieldHandler extends UnsafeFieldHandler<Integer> {

    @Override
    public Integer get(Object instance){
        return Serializer.THE_UNSAFE.getInt(instance, offset);
    }

    @Override
    public void set(Object instance, Integer value){
        Serializer.THE_UNSAFE.putInt(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.INT;
    }

}
