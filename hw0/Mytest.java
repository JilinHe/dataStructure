import org.junit.Test;
import static org.junit.Assert.*;

//import ucb.junit.textui;

/** Tests for hw0. 
 *  @author JILIN HE
 */
public class Mytest {

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(4, hw0.max(new int[] { -6, 2, 4 }));
        assertEquals(200, hw0.max(new int[] { -6, 3, 10, 200 }));
        assertEquals(15, hw0.max(new int[] { 8, 2, -1, -1, 15 }));
        assertEquals(14, hw0.max(new int[] { 0, -5, 2, 14, 10 }));
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(hw0.threeSUM(new int[] { -6, 2, 4 }));
        assertTrue(hw0.threeSUM(new int[] { -6, 3, 10, 200 }));
        assertTrue(hw0.threeSUM(new int[] { 8, 2, -1, -1, 15 }));
    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        assertFalse(hw0.threeSUM_Distinct(new int[] {-6, 2, 4 }));
        assertFalse(hw0.threeSUM_Distinct(new int[] { -6, 3, 10, 200 }));
        assertFalse(hw0.threeSUM_Distinct(new int[] { 8, 2, -1, -1, 15 }));
    }

    public static void main(String[] args){

    }
}
