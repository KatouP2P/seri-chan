package ga.nurupeaches.serichan;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Okay, for a small overview as to WHY this is something I wanted to do:
 * Interfaces can have static variables (obviously) but they cannot have static block initializers.
 * This makes sense since static block would implicate that the interface has an implementation on itself; which
 * defeats the whole idea of interface as a whole. The main reason why this code works is how nested classes work;
 * a class can access nested class variables without doing any fancy casting or implementation.
 *
 * So, basically, create a new Object and declare the inner variable via anonymous class.
 * Let's say the inner variable is X.
 * Within the anonymous class, create a block initializer (this is basically the same as a 0-arg constructor).
 * Within that block, initialize X.
 * Afterwards, outside of the anonymous class, access the inner variable.
 * Ta-da! We've essentially created an "static initialization" block for a single variable.
 *
 * ... This shouldn't ever be used in actual production code.
 */
public class SemiLambdaInitializerInInterface extends TestCase {

    private interface Hack {

        static String str = new Object(){

            String str; {
                str = "Hello, world!";
            }

        }.str;

    }

    @Test
    public void testHack(){
        assert "Hello, world!".equals(Hack.str);
    }

}
