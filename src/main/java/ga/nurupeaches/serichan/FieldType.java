package ga.nurupeaches.serichan;

public enum FieldType {

    BOOLEAN(boolean.class), BYTE(byte.class), SHORT(short.class), CHAR(char.class), INT(int.class),
    LONG(long.class), FLOAT(float.class), DOUBLE(double.class), STRING(String.class), NONPRIMITIVE(Void.class);

    private Class<?> primitiveClass;

    FieldType(Class<?> klass){
        this.primitiveClass = klass;
    }

    public static FieldType valueOf(Class<?> klass){
        for(FieldType type : FieldType.values()){
            if(type.primitiveClass == klass){
                return type;
            }
        }

        return NONPRIMITIVE;
    }

}
