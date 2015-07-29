package ga.nurupeaches.serichan;

import java.util.HashMap;
import java.util.Map;

public class SimpleTransmittableObject implements Transmittable {

    private int testInt;
    private String testString;
    private Map<KeyObj, SubObj> subObj;

    public SimpleTransmittableObject(){}

    public SimpleTransmittableObject(int i, String str){
        testInt = i;
        testString = str;
        subObj = new HashMap<>();
        subObj.put(new KeyObj(1), new SubObj(i, reverseString(str)));
    }

    public String reverseString(String str){
        return new StringBuilder(str).reverse().toString();
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof SimpleTransmittableObject)){
            return false;
        }

        SimpleTransmittableObject obj = (SimpleTransmittableObject)o;
        System.out.println("local: int=" + testInt + ",subObj=" + subObj + ",str=" + testString);
        System.out.println("remote: int=" + obj.testInt + ",subObj=" + obj.subObj + ",str=" + obj.testString);
        return obj.testInt == testInt && subObj.equals(obj.subObj) && obj.testString.equals(testString);
    }

    private class KeyObj implements Transmittable {

        private int testInt;

        public KeyObj(int i){
            testInt = i;
        }

        @Override
        public boolean equals(Object o){
            if(!(o instanceof KeyObj)){
                return false;
            }

            KeyObj obj = (KeyObj)o;
            System.out.println("local: int=" + testInt);
            System.out.println("remote: int=" + obj.testInt);
            return obj.testInt == testInt;
        }

    }

    private class SubObj implements Transmittable {

        private String testString;
        private int testInt;

        public SubObj(int i, String str){
            testInt = i;
            testString = str;
        }

        @Override
        public boolean equals(Object o){
            if(!(o instanceof SubObj)){
                return false;
            }

            SubObj obj = (SubObj)o;
            System.out.println("local: int=" + testInt + ",str=" + testString);
            System.out.println("remote: int=" + obj.testInt + ",str=" + obj.testString);
            return obj.testInt == testInt && obj.testString.equals(testString);
        }

    }

}
