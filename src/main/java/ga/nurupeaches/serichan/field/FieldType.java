package ga.nurupeaches.serichan.field;

public enum FieldType {

    BOOLEAN(boolean.class), BYTE(byte.class), CHAR(char.class), DOUBLE(double.class), FLOAT(float.class),
    INT(int.class), LONG(long.class), OBJECT(null), SHORT(short.class);

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
