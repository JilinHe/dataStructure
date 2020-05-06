package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Jilin He
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    // FIXME: Add tests here that pass on a correct Permutation and fail on buggy Permutations.
    @Test
    public void testPermuteChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        Permutation p1 = getNewPermutation("(BA) (CD)", getNewAlphabet("ABCD"));
        Permutation p2 = getNewPermutation("(BAD) (C)", getNewAlphabet("ABCD"));
        /* TODO: Add additional assert statements here! */
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
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        Permutation p1 = getNewPermutation("(BA) (CD)", getNewAlphabet("ABCD"));
        Permutation p2 = getNewPermutation("(BAD) (C)", getNewAlphabet("ABCD"));
        /* TODO: Add additional assert statements here! */
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
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        Permutation p1 = getNewPermutation("(BA) (CD)", getNewAlphabet("ABCD"));
        Permutation p2 = getNewPermutation("(BAD) (C)", getNewAlphabet("ABCD"));
        /* TODO: Add additional assert statements here! */
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
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        Permutation p1 = getNewPermutation("(BA) (CD)", getNewAlphabet("ABCD"));
        Permutation p2 = getNewPermutation("(BAD) (C)", getNewAlphabet("ABCD"));
        /* TODO: Add additional assert statements here! */
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

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
    }
}
