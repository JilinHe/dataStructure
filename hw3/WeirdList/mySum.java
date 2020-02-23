public class mySum implements IntUnaryFunction {
    /**
     * Declare sum.
     */
    private int sum;

    /**
     * constructor.
     */
    public mySum() {
        this.sum = 0;
    }

    @Override
    public int apply(int x) {
        this.sum += x;
        return x;
    }

    /**
     * getSum function.
     * @return sum value.
     */
    public int getSum() {
        return this.sum;
    }
}
