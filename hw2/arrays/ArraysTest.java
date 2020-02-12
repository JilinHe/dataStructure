package arrays;

//import lists.IntList;
//import lists.IntListList;
//import lists.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** FIXME
     */
    @Test
    public void myCatenateTests() {
        int[] array0 = new int[]{};
        int[] array1 = new int[]{1, 2};
        int[] array2 = new int[]{3, 4};
        int[] array3 = new int[]{1, 2, 3, 4};

        assertArrayEquals(array2, Arrays.catenate(array0, array2));
        assertArrayEquals(array3, Arrays.catenate(array1, array2));
    }

    @Test
    public void myRemoveTests() {
        int[] array1 = new int[]{3, 4, 5};
        int[] array2 = new int[]{1, 4, 5};
        int[] array3 = new int[]{1, 2, 3, 4, 5};

        assertArrayEquals(array2, Arrays.remove(array3, 1, 2));
        assertArrayEquals(array1, Arrays.remove(array3, 0, 2));
    }

    @Test
    public void myNatualRunTests() {
        int[] array1 = new int[]{5, 3, 7, 5, 4, 6, 9, 1};
        int[][] array = new int[][]{{5}, {3, 7}, {5}, {4, 6, 9}, {1}};

        assertArrayEquals(array, Arrays.naturalRuns(array1));
    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
