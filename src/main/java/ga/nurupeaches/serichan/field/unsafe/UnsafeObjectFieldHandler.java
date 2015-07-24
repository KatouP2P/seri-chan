package ga.nurupeaches.serichan.field.unsafe;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeObjectFieldHandler extends UnsafeFieldHandler<Object> {

    @Override
    public Object get(Object instance){
        return Serializer.THE_UNSAFE.getObject(instance, offset);
    }

    @Override
    public void set(Object instance, Object value){
        Serializer.THE_UNSAFE.putObject(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.OBJECT;
    }

}
