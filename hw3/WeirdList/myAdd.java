public class myAdd implements IntUnaryFunction {
    /**
     * Declare _list and n.
     */
    private WeirdList _list;
    private int n;

    /**
     * constructor.
     * @param n.
     */
    public myAdd(int n) {
        this.n = n;
    }

    @Override
    public int apply(int x) {
        return x + n;
    }
}
