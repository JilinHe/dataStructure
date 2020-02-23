public class isEmpty extends WeirdList {

    /**
     * Constructor of the isEmpty.
     *
     */
    public isEmpty() {
        super(0, null);
    }

    /**
     * length() function.
     * @return 0.
     */
    @Override
    public int length() {
        return 0;
    }

    /**
     * toString() function.
     * @return "".
     */
    @Override
    public String toString() {
        return "";
    }

    @Override
    public WeirdList map(IntUnaryFunction func) {
        return this;
    }
}
