import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] a = new int[][]{{1, 3, 4}, {5, 6, 8}, {2, 7, 9}};
        assertEquals(9, MultiArr.maxValue(a));
    }

    @Test
    public void testAllRowSums() {
        int[][] a = new int[][]{{1, 3, 4}, {5, 6, 8}, {2, 7, 9}};
        int[] testsum = new int[]{8, 19, 18};
        for (int i = 0; i < a.length; i++) {
            assertEquals(testsum[i], MultiArr.allRowSums(a)[i]);
        }
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
