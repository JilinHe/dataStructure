package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        IntList temp1 = L;
        IntList temp2 = L;
        int n, flag = 0;
        IntListList outcome = new IntListList();
        IntListList head = outcome;
        while (temp1 != null) {
            n = 0;
            temp2 = temp1;
            while (temp2 != null) {
                if (temp2.tail != null) {
                    if (temp2.head < temp2.tail.head) {
                        n++;
                        flag = 1;
                    } else {
                        n++;
                        flag = 0;
                        break;
                    }
                } else {
                    if (flag == 1) {
                        n++;
                        break;
                    } else {
                        n = 1;
                        break;
                    }
                }
                temp2 = temp2.tail;
            }
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = temp1.head;
                temp1 = temp1.tail;
            }
            IntList intList = IntList.list(array);
            if (temp1 != null) {
                IntListList intLL = new IntListList();
                outcome.head = intList;
                outcome.tail = intLL;
                outcome = outcome.tail;
            } else {
                outcome.head = intList;
                outcome.tail = null;
            }
        }
        return head;
    }
}
