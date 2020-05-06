import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; i++) {
                for (int j = i; j > 0; j--) {
                    if (array[j] < array[j - 1]) {
                        swap(array, j, j - 1);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                int min = array[i];
                int index = i;
                for (int j = i; j < k; j++) {
                    if (array[j] < min) {
                        min = array[j];
                        index = j;
                    }
                }
                swap(array, i, index);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int[] outcome = sortHelper(array, 0, k - 1);
            System.arraycopy(outcome, 0, array, 0, k);
        }

        // may want to add additional methods
        public int[] sortHelper(int[] array, int l, int h) {
            if (l == h) {
                return new int[]{array[l]};
            }
            int mid = l + (h - l) / 2;

            int[] arr1 = sortHelper(array, l, mid);
            int[] arr2 = sortHelper(array, mid + 1, h);
            int[] arrSum = new int[arr1.length + arr2.length];

            int i, j, s = 0;
            for (i = 0, j = 0; i < arr1.length && j < arr2.length; ) {
                if (arr1[i] < arr2[j]) {
                    arrSum[s++] = arr1[i++];
                } else {
                    arrSum[s++] = arr2[j++];
                }
            }
            if (i < arr1.length) {
                System.arraycopy(arr1, i, arrSum, s, arr1.length - i);
            } else if (j < arr2.length) {
                System.arraycopy(arr2, j, arrSum, s, arr2.length - j);
            }
            return arrSum;
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int max = -1000;
            for (int i = 0; i < k; i++) {
                if (max < array[i]) {
                    max = array[i];
                }
            }
            int[] range = new int[max];
            for (int j = 0; j < k; j++) {
                int tmp = array[j];
                range[tmp] += 1;
            }

            for (int m = 1; m < range.length; m++) {
                range[m] += range[m - 1];
            }

            int[] copy = new int[k];
            System.arraycopy(array, 0, copy, 0, k);
            for (int n = 0; n < copy.length; n++) {
                int temp = range[array[n]];
                copy[temp] = array[n];
                range[array[n]]--;
            }
            System.arraycopy(copy, 0, array, 0, k);
        }

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {

        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {

        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int max = -1000;
            for (int i = 0; i < k; i++) {
                if (max < a[i]) {
                    max = a[i];
                }
            }
            int loops = 1;
            while (max > 9) {
                max /= 10;
                loops *= 10;
            }

            for (int j = 1; j <= loops; j *= 10) {
                int[][] store = new int[10][k];
                int[] rowSize = new int[10];
                for (int m = 0; m < k; m++) {
                    int lsd = (a[m] / j) % 10;
                    store[lsd][rowSize[lsd]] = a[m];
                    rowSize[lsd]++;
                }
                int index = 0;
                for (int x = 0; x < 10; x++) {
                    for (int y = 0; y < rowSize[x]; y++) {
                        a[index++] = store[x][y];
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {

        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
