package ga.nurupeaches.serichan.field;

import java.util.Map;

public enum FieldType {

    BOOLEAN(boolean.class), BYTE(byte.class), CHAR(char.class), DOUBLE(double.class), FLOAT(float.class),
    INT(int.class), LONG(long.class), SHORT(short.class),

    // The enums below have custom handling.
    MAP(Map.class),
    STRING(String.class),
    OBJECT(null),

    ;

    private static final FieldType[] VALUES = FieldType.values();
    private final Class<?> klass;

    FieldType(Class<?> klass){
        this.klass = klass;
    }

    public static FieldType valueOf(Class<?> klass){
        for(FieldType type : VALUES){
            if(type.klass == klass){
                return type;
            }
        }

        return OBJECT;
    }

}
