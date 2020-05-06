import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Jilin He
 */
class ECHashStringSet implements StringSet {

    ECHashStringSet() {
        for (int i = 0; i < _bucketCount; i++) {
            _temp[i] = new LinkedList<String>();
        }
    }

    @Override
    public void put(String s) {
        if (this.contains(s)) {
            return;
        }
        if (_variable / _bucketCount == getLoadFactor()) {
            resizeHelper();
        }
        int hashint = s.hashCode() & 0x7fffffff % _bucketCount;
        LinkedList<String> node = _temp[hashint];
        node.add(s);
        _variable++;
    }

    @Override
    public boolean contains(String s) {
        int hashint = s.hashCode() & 0x7fffffff % _bucketCount;
        LinkedList<String> node = _temp[hashint];
        if (node.contains(s)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> asList() {
        List<String> tmpList = new ArrayList<String>();
        for (int i = 0; i < _temp.length; i++) {
            LinkedList<String> node = _temp[i];
            for (int j = 0; j < node.size(); j++) {
                tmpList.add(node.get(j).toString());
            }
        }
        return tmpList;
    }

    public int getLoadFactor() {
        return 5;
    }

    public void resizeHelper() {
        _bucketCount = _bucketCount * 2;
        LinkedList<String>[] newArr = new LinkedList[_bucketCount];
        for (int i = 0; i < _bucketCount; i++) {
            newArr[i] = new LinkedList<String>();
        }
        for (int i = 0; i < _temp.length; i++) {
            LinkedList<String> node = _temp[i];
            for (int j = 0; j < node.size(); j++) {
                String tmp = node.get(j);
                int hashint = tmp.hashCode() & 0x7fffffff % _bucketCount;
                LinkedList<String> newNode = newArr[hashint];
                newNode.add(tmp);
            }
        }
        _temp = newArr;
    }

    private int _bucketCount = 2;

    private int _loadFactor = 0;

    private int _variable = 0;

    private LinkedList<String>[] _temp = new LinkedList[_bucketCount];
}
