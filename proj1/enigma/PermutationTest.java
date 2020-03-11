package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Jilin He
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p1 = new Permutation("(BA) (CD)", new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(BAD) (C)", new Alphabet("ABCD"));
        assertEquals('C', p.permute('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('B', p.permute('D'));

        assertEquals('B', p1.permute('A'));
        assertEquals('A', p1.permute('B'));
        assertEquals('C', p1.permute('D'));

        assertEquals('D', p2.permute('A'));
        assertEquals('A', p2.permute('B'));
        assertEquals('C', p2.permute('C'));
    }

    @Test
    public void testPermuteInt() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p1 = new Permutation("(BA) (CD)", new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(BAD) (C)", new Alphabet("ABCD"));
        assertEquals(2, p.permute(0));
        assertEquals(0, p.permute(1));
        assertEquals(1, p.permute(3));
        assertEquals(1, p.permute(7));

        assertEquals(1, p1.permute(0));
        assertEquals(0, p1.permute(1));
        assertEquals(2, p1.permute(3));
        assertEquals(2, p1.permute(7));

        assertEquals(3, p2.permute(0));
        assertEquals(0, p2.permute(1));
        assertEquals(2, p2.permute(2));
        assertEquals(2, p2.permute(6));
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p1 = new Permutation("(BA) (CD)", new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(BAD) (C)", new Alphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('A', p.invert('C'));

        assertEquals('B', p1.permute('A'));
        assertEquals('A', p1.permute('B'));
        assertEquals('C', p1.permute('D'));

        assertEquals('D', p2.permute('A'));
        assertEquals('A', p2.permute('B'));
        assertEquals('C', p2.permute('C'));
    }

    @Test
    public void testInvertInt() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p1 = new Permutation("(BA) (CD)", new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(BAD) (C)", new Alphabet("ABCD"));
        assertEquals(1, p.invert(0));
        assertEquals(3, p.invert(1));
        assertEquals(0, p.invert(2));
        assertEquals(0, p.invert(6));

        assertEquals(1, p1.permute(0));
        assertEquals(0, p1.permute(1));
        assertEquals(2, p1.permute(3));
        assertEquals(2, p1.permute(7));

        assertEquals(3, p2.permute(0));
        assertEquals(0, p2.permute(1));
        assertEquals(2, p2.permute(2));
        assertEquals(2, p2.permute(6));
    }

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }
}
