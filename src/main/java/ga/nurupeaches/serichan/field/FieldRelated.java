package ga.nurupeaches.serichan.field;

import java.lang.reflect.Field;

/**
 * Indicates that the class pertains to using fields.
 */
public interface FieldRelated {

    public Field getField();

    /**
     * Retrieves the offset of the field.
     * @return The offset or -1 if the FieldRelated implemented class does not use sun.misc.Unsafe
     */
    public long getOffset();

}
