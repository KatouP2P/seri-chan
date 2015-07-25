package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeFloatFieldHandler extends UnsafeFieldHandler<Float> {

    @Override
    public Float get(Object instance){
        return Serializer.THE_UNSAFE.getFloat(instance, offset);
    }

    @Override
    public void set(Object instance, Float value){
        Serializer.THE_UNSAFE.putFloat(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.FLOAT;
    }

}
