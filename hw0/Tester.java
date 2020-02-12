import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @author Jilin He
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(4, hw0.max(new int[] { -6, 2, 4 }));
        assertEquals(200, hw0.max(new int[] { -6, 3, 10, 200 }));
        assertEquals(15, hw0.max(new int[] { 8, 2, -1, -1, 15 }));
        assertEquals(14, hw0.max(new int[] { 0, -5, 2, 14, 10 }));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(hw0.threeSUM(new int[] { -6, 2, 4 }));
        assertTrue(hw0.threeSUM(new int[] { -6, 3, 10, 200 }));
        assertTrue(hw0.threeSUM(new int[] { 8, 2, -1, -1, 15 }));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        assertTrue(hw0.threeSUM_Distinct(new int[] {-6, 2, 4 }));
        assertFalse(hw0.threeSUM_Distinct(new int[] { -6, 3, 10, 200 }));
        assertTrue(hw0.threeSUM_Distinct(new int[] { 8, 2, -1, -1, 15 }));
        // REPLACE THIS WITH MORE TESTS.
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
