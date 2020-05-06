import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/** HW #7, Sorting ranges.
 *  @author Jilin He
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        int max = -10000000;
        int min = 0;
        if (intervals.size() == 0) {
            return 0;
        }
        for (int[] temp: intervals) {
            if (min > temp[0]) {
                min = temp[0];
            }
            if (max < temp[1]) {
                max = temp[1];
            }
        }
        int[] store = new int[max - min];
        for (int[] temp: intervals) {
            for (int i = temp[0] - min; i < temp[1] - min; i++) {
                if (store[i] == 0) {
                    store[i] = 1;
                }
            }
        }
        for (int i = 1; i < store.length; i++) {
            store[i] += store[i - 1];
        }
        return store[max - min - 1];
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {-19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 49;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
