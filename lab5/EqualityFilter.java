/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        left = input.colNameToIndex(colName);
        this.match = match;
    }

    @Override
    protected boolean keep() {
        String a = _next.getValue(left);
        return a.equals(match);
    }

    int left;
    String match;
}
