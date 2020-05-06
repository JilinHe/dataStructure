import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test of a BST-based String Set.
 * @author Jilin He
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testbst() {
        String[] s = new String[]{"cc", "aa", "bb", "dd"};
        String[] s1 = new String[]{"aa", "bb", "cc", "dd"};
        List<String> ls = new ArrayList<String>();
        ls.addAll(Arrays.asList(s1));
        BSTStringSet bst = new BSTStringSet();
        for (int i = 0; i < s.length; i++) {
            bst.put(s[i]);
        }
        assertTrue(bst.contains("bb"));
        assertFalse(bst.contains("ee"));
        assertEquals(ls, bst.asList());

        bst.put("ee");
        assertTrue(bst.contains("ee"));
    }

    @Test
    public void testempty() {
        String[] s = new String[]{};
        String[] s1 = new String[]{};
        List<String> ls = new ArrayList<String>();
        ls.addAll(Arrays.asList(s1));
        BSTStringSet bst = new BSTStringSet();
        for (int i = 0; i < s.length; i++) {
            bst.put(s[i]);
        }
        assertFalse(bst.contains("ee"));
        assertEquals(ls, bst.asList());

        bst.put("ee");
        assertTrue(bst.contains("ee"));
    }

    @Test
    public void testbig() {
        List<String> ls = new ArrayList<String>();
        BSTStringSet bst = new BSTStringSet();
        for (int i = 0; i < 10000; i++) {
            String a = StringUtils.randomString(4);
            if (!ls.contains(a)) {
                bst.put(a);
                ls.add(a);
            }
        }
        assertTrue(bst.contains(ls.get(5)));
        assertFalse(bst.contains("1234"));
        Collections.sort(ls);
        List<String> tmp = bst.asList();
        assertEquals(ls, tmp);
    }
}
