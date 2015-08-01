package ga.nurupeaches.serichan.field;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class NoFieldHandler implements FieldHandler<Void> {

    private Field field;

    @Override
    public void set(Object instance, Void value){}

    @Override
    public Void get(Object instance){ return null; }

    @Override
    public int size(Object instance){ return 0; }

    @Override
    public void read(Object instance, ByteBuffer buffer){}

    @Override
    public void write(Object instance, ByteBuffer buffer){}

    @Override
    public Field getField(){
        return field;
    }

    @Override
    public void initialize(Field field){
        this.field = field;
    }

}
