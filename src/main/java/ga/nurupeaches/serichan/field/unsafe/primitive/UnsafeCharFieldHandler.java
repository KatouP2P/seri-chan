package ga.nurupeaches.serichan.field.unsafe.primitive;

import ga.nurupeaches.serichan.Serializer;
import ga.nurupeaches.serichan.field.FieldType;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

public class UnsafeCharFieldHandler extends UnsafeFieldHandler<Character> {

    @Override
    public Character get(Object instance){
        return Serializer.THE_UNSAFE.getChar(instance, offset);
    }

    @Override
    public void set(Object instance, Character value){
        Serializer.THE_UNSAFE.putChar(instance, offset, value);
    }

    @Override
    public FieldType getType(){
        return FieldType.CHAR;
    }

}

