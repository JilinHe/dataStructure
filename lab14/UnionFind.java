
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author Jilin He
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        _N = N + 1;
        parents = new int[_N];
        sizes = new int[_N];
        for (int i = 1; i < _N; i++) {
            parents[i] = i;
            sizes[i] = 1;
        }
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        if (parents[v] != v) {
            parents[v] = find(parents[v]);
        }
        return parents[v];
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        int parentU = find(u);
        int parentV = find(v);
        int ret = parentU;
        if (parentU == parentV) {
            return 0;
        }
        if (sizes[parentU] >= sizes[parentV]) {
            for (int i = 1; i < _N; i++) {
                if (parents[i] == parentV) {
                    parents[i] = parentU;
                    sizes[parentU]++;
                }
            }
        } else {
            for (int i = 1; i < _N; i++) {
                if (parents[i] == parentU) {
                    parents[i] = parentV;
                    sizes[parentV]++;
                }
            }
            ret = parentV;
        }
        return ret;
    }

    // FIXME
    private int[] parents;
    private int[] sizes;
    private int _N;
}
