/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        left = input.colNameToIndex(colName1);
        right = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        String a = _next.getValue(left);
        String b = _next.getValue(right);
        return a.equals(b);
    }

    int left;
    int right;
}
