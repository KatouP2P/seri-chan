package ga.nurupeaches.serichan;

import java.util.ArrayList;
import java.util.List;

public class SimpleTransmittableObject implements Transmittable {

    private int testInt;
    private String testString;
    private List<SubObj> subObj;

    public SimpleTransmittableObject(){}

    public SimpleTransmittableObject(int i, String str){
        testInt = i;
        testString = str;
        subObj = new ArrayList<>();
        subObj.add(new SubObj(i, reverseString(str)));
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
