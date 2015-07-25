package ga.nurupeaches.serichan;

import ga.nurupeaches.serichan.field.FieldHandler;
import ga.nurupeaches.serichan.field.ReflectionFieldHandler;
import ga.nurupeaches.serichan.field.UnsafeFieldHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private boolean cached = false;
    private final Map<Field, FieldHandler<?>> handlerMap = new HashMap<>();

    public void populateCache(Class<?> klass){
        Class<?> currentClass = klass;
        do{
            Field[] fields = currentClass.getDeclaredFields();
            if(Serializer.THE_UNSAFE != null){
                for(Field field : fields){
                    if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())
                            || Modifier.isTransient(field.getModifiers())) continue;

                    UnsafeFieldHandler<?> handler = UnsafeFieldHandler.newFieldHandler(field.getType());
                    handler._init(field);
                    handlerMap.put(field, handler);
                }
            } else {
                for(Field field : fields){
                    handlerMap.put(field, new ReflectionFieldHandler<>(field));
                }
            }
            currentClass = currentClass.getSuperclass();
        } while (!currentClass.equals(Object.class));

        cached = true;
    }

    public boolean isCached(){
        return cached;
    }

    public Collection<FieldHandler<?>> getFieldHandlers(){
        return handlerMap.values();
    }

}
