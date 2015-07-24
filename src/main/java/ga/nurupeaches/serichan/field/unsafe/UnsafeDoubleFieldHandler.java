package ga.nurupeaches.serichan.field.unsafe;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeDoubleFieldHandler extends UnsafeFieldHandler<Double> {

    @Override
    public Double get(Object instance){
        return Serializer.THE_UNSAFE.getDouble(instance, offset);
    }

    @Override
    public void set(Object instance, Double value){
        Serializer.THE_UNSAFE.putDouble(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.DOUBLE;
    }

}
