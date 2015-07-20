package ga.nurupeaches.serichan;

public enum FieldType {

    BOOLEAN(boolean.class), BYTE(byte.class), SHORT(short.class), CHAR(char.class), INT(int.class),
    LONG(long.class), STRING(String.class), NONPRIMITIVE(Void.class);

    public static final FieldType[] VALUES = FieldType.values();
    private Class<?> klass;

    FieldType(Class<?> klass){
        this.klass = klass;
    }

    public static FieldType valueOf(Class<?> klass){
        for(FieldType type : FieldType.values()){
            if(type.klass == klass){
                return type;
            }
        }

        return NONPRIMITIVE;
    }

}
