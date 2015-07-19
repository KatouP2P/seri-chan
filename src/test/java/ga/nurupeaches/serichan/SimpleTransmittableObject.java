package ga.nurupeaches.serichan;

public class SimpleTransmittableObject implements Transmittable {

    private int testInt;
    private long testLong;
    private String testString;

    public SimpleTransmittableObject(int i, long l, String s){
        testInt = i;
        testLong = l;
        testString = s;
    }

    @Override
    public int getSize(){
        return Integer.BYTES + Long.BYTES + Serializer.stringSize(testString);
    }

}
