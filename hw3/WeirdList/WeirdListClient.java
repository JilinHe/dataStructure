/** Functions to increment and sum the elements of a WeirdList.
 * @author Jilin He
 * */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        myAdd temp = new myAdd(n);
        return L.map(temp);
    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L) {
        mySum temp = new mySum();
        L.map(temp);
        return temp.getSum();
    }

    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
}
