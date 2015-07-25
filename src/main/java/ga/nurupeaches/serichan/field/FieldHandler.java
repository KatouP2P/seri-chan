package ga.nurupeaches.serichan.field;

public interface FieldHandler<T> extends FieldRelated {

    /**
     * Sets the field for the given instance to the value.
     * @param instance The instance to change.
     * @param value The value to set it to.
     */
    public void set(Object instance, T value);

    /**
     * Returns the instance's current field value.
     * @param instance The instance to get the value from.
     * @return The value of the field for the given instance.
     */
    public T get(Object instance);

    /**
     * Returns the field type as an enum.
     * @return An enum representing the field type.
     */
    public FieldType getType();

}
