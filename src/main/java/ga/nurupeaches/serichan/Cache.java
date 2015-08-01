package ga.nurupeaches.serichan;

import ga.nurupeaches.serichan.field.FieldHandler;
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
        Field[] fields = klass.getDeclaredFields();
        for(Field field : fields){
            if(field.isSynthetic()) continue; // Inner-Transmittable-classes will cause SOEs without this. Damn you this$0!
            if(Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) continue;
            FieldHandler<?> handler = UnsafeFieldHandler.newFieldHandler(field.getType());
            if(handler == null){
                continue;
            }
            handler.initialize(field);
            handlerMap.put(field, handler);
        }
        cached = true;
    }

    public int getSizeOfObject(Object instance){
        int size = 0;
        for(FieldHandler<?> handler : handlerMap.values()){
            size += handler.size(instance);
        }
        return size;
    }

    public boolean isCached(){
        return cached;
    }

    public Collection<FieldHandler<?>> getFieldHandlers(){
        return handlerMap.values();
    }

}
