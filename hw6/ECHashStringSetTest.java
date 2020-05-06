import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Jilin He
 */
public class ECHashStringSetTest  {

    @Test
    public void testEch() {
        String[] s = new String[]{"cc", "aa", "bb", "dd", "zz", "ab", "ba", "cd", "dc", "zx", "ws", "ed"};
        String[] s1 = new String[]{"cc", "aa", "bb", "dd", "zz", "ws", "ab", "cd", "zx", "ba", "dc", "ed"};
        ECHashStringSet echash = new ECHashStringSet();
        List<String> ls = new ArrayList<String>();
        ls.addAll(Arrays.asList(s1));
        for (int i = 0; i < s.length; i++) {
            echash.put(s[i]);
        }
        assertTrue(echash.contains("bb"));
        assertFalse(echash.contains("ee"));
        assertEquals(ls, echash.asList());

        echash.put("cc");
    }
}
