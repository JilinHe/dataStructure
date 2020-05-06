/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        left = input.colNameToIndex(colName);
        this.subStr = subStr;
    }

    @Override
    protected boolean keep() {
        String a = _next.getValue(left);
        return a.contains(subStr);
    }

    int left;
    String subStr;
}
