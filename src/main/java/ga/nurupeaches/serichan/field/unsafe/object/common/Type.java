package ga.nurupeaches.serichan.field.unsafe.object.common;

import ga.nurupeaches.common.utils.BufferUtils;

import java.nio.ByteBuffer;

public enum Type {

    BOOLEAN(boolean.class) {

        @Override
        public int sizeof(Object obj){
            return 1;
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){
            buffer.put((byte) ((boolean) obj ? 1 : 0));
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return buffer.get() == 1;
        }

    }, BYTE(byte.class) {

        @Override
        public int sizeof(Object obj){
            return 1;
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){
            buffer.put((byte) obj);
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return buffer.get();
        }

    }, CHARACTER(char.class) {

        @Override
        public int sizeof(Object obj){
            return Character.BYTES;
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){
            buffer.putChar((char) obj);
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return buffer.getChar();
        }

    }, DOUBLE(double.class) {

        @Override
        public int sizeof(Object obj){
            return Double.BYTES;
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){
            buffer.putDouble((double) obj);
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return buffer.getDouble();
        }

    }, FLOAT(float.class) {

        @Override
        public int sizeof(Object obj){
            return Float.BYTES;
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){
            buffer.putFloat((float) obj);
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return buffer.getFloat();
        }

    }, LONG(long.class) {

        @Override
        public int sizeof(Object obj){
            return Long.BYTES;
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){
            buffer.putDouble((long) obj);
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return buffer.getLong();
        }

    }, SHORT(short.class) {

        @Override
        public int sizeof(Object obj){
            return Short.BYTES;
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){
            buffer.putShort((short) obj);
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return buffer.getShort();
        }

    }, STRING(String.class) {

        @Override
        public int sizeof(Object obj){
            return BufferUtils.stringSize((String)obj);
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){
            BufferUtils.writeString(buffer, (String) obj);
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return BufferUtils.readString(buffer);
        }

    }, UNKNOWN(Void.class){
        @Override
        public int sizeof(Object obj){
            return 0;
        }

        @Override
        public Object readFromBuffer(ByteBuffer buffer){
            return null;
        }

        @Override
        public void writeToBuffer(Object obj, ByteBuffer buffer){}
    };

    private final Class<?> klass;

    Type(Class<?> klass){
        this.klass = klass;
    }

    public static final Type[] VALUES = Type.values();

    public static Type getType(Class<?> klass){
        for(Type t : Type.VALUES){
            if(t.klass.isAssignableFrom(klass)){
                return t;
            }
        }
        return UNKNOWN;
    }

    public abstract void writeToBuffer(Object obj, ByteBuffer buffer);
    public abstract Object readFromBuffer(ByteBuffer buffer);
    public abstract int sizeof(Object obj);

}
