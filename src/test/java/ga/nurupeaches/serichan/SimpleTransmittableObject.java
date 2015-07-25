package ga.nurupeaches.serichan;

import ga.nurupeaches.common.utils.BufferUtils;

public class SimpleTransmittableObject implements Transmittable {

    private int testInt;
    private long testLong;
    private String testString;

    public SimpleTransmittableObject(){}

    public SimpleTransmittableObject(int i, long l, String s){
        testInt = i;
        testLong = l;
        testString = s;
    }

    @Override
    public int getSize(){
        return Integer.BYTES + Long.BYTES + BufferUtils.stringSize(testString);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof SimpleTransmittableObject)){
            return false;
        }

        SimpleTransmittableObject obj = (SimpleTransmittableObject)o;
        System.out.println("local: int=" + testInt + ",long=" + testLong + ",string=" + testString);
        System.out.println("remote: int=" + obj.testInt + ",long=" + obj.testLong + ",string=" + obj.testString);
        return obj.testInt == testInt && obj.testLong == testLong && obj.testString.equals(testString);
    }

}
