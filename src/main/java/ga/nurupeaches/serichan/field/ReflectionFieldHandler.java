package ga.nurupeaches.serichan.field;

import ga.nurupeaches.common.exception.UncheckedReflectionException;

import java.lang.reflect.Field;

public class ReflectionFieldHandler<T> implements FieldHandler<T> {

    private final Field field;

    public ReflectionFieldHandler(Field field){
        this.field = field;
        field.setAccessible(true);
    }

    @Override
    public void set(Object instance, T value){
        try {
            field.set(instance, value);
        } catch (ReflectiveOperationException e){
            throw new UncheckedReflectionException(e);
        }
    }

    @Override
    public T get(Object instance){
        try {
            return (T)field.get(instance);
        } catch (ReflectiveOperationException e){
            throw new UncheckedReflectionException(e);
        } catch (ClassCastException e){
            throw new UncheckedReflectionException("unexpected class cast exc; wrong field type????", e);
        }
    }

    @Override
    public FieldType getType(){
        return FieldType.valueOf(field.getType());
    }

    @Override
    public Field getField(){
        return field;
    }

    @Override
    public long getOffset(){
        return -1;
    }

}
