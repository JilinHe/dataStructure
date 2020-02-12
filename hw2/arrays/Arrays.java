package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int len = A.length + B.length;
        int[] C = new int[len];
        for (int i = 0; i < len; i++) {
            if (i < A.length) {
                C[i] = A[i];
            } else {
                C[i] = B[i - A.length];
            }
        }
        return C;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int[] C;
        int[] temp1 = new int[start];
        int[] temp2 = new int[A.length - len - start];
        for (int i = 0; i < temp1.length; i++) {
            temp1[i] = A[i];
        }
        for (int i = 0; i < temp2.length; i++) {
            temp2[i] = A[i + start + len];
        }
        C = catenate(temp1, temp2);
        return C;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int segment = 0;
        for (int i = 0; i < A.length - 1; i++) {
            if (A[i] > A[i + 1]) {
                segment++;
            }
        }
        int[][] outcome = new int[segment + 1][];
        int start = 0;
        int len = 0;
        int flag = 0; // signal to build a new array
        int seg = 0; // sign of rows of the array
        int index = 0;
        for (int i = 0; i < A.length; i++) {
            if (i == A.length - 1) {
                if (A[i - 1] > A[i]) {
                    flag = 1;
                    len++;
                }
            } else {
                if (A[i] < A[i + 1]) {
                    len++;
                } else {
                    flag = 1;
                    len++;
                }
            }
            if (flag == 1) { //signal to build a new array
                outcome[seg] = new int[len];
                for (int j = 0; j < len; j++) {
                    index = j + start;
                    outcome[seg][j] = A[index];
                }
                flag = 0;
                seg++;
                start += len;
                len = 0;
            }
        }
        return outcome;
    }
}
