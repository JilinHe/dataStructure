/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        left = input.colNameToIndex(colName);
        this.ref = ref;
    }

    @Override
    protected boolean keep() {
        String a = _next.getValue(left);
        if (a.compareTo(ref) > 0) {
            return true;
        } else {
            return false;
        }
    }

    int left;
    String ref;
}
