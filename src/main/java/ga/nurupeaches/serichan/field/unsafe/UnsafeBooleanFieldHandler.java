package ga.nurupeaches.serichan.field.unsafe;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeBooleanFieldHandler extends UnsafeFieldHandler<Boolean> {

    @Override
    public Boolean get(Object instance){
        return Serializer.THE_UNSAFE.getBoolean(instance, offset);
    }

    @Override
    public void set(Object instance, Boolean value){
        Serializer.THE_UNSAFE.putBoolean(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.BOOLEAN;
    }

}
