package ga.nurupeaches.serichan;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class Cache {

    Map<String, Field> fieldCache = new HashMap<>();

    void populateCache(Class<?> klass){
        for(Field field : klass.getDeclaredFields()){
            field.setAccessible(true);
            fieldCache.put(field.getName(), field);
        }
    }

}
