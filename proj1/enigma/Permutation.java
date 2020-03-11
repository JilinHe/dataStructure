package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jilin He
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles = _cycles + cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char ch = _alphabet.toChar(wrap(p));
        ch = permute(ch);
        return _alphabet.toInt(ch);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char ch = _alphabet.toChar(wrap(c));
        ch = invert(ch);
        return _alphabet.toInt(ch);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int tempPar = 0;
        char result = p;
        for (int j = 0; j < _cycles.length(); j++) {
            if (_cycles.charAt(j) == '(') {
                tempPar = j;
            }
            if (_cycles.charAt(j) == p) {
                int index = j + 1;
                if (_cycles.charAt(index) == ')') {
                    result = _cycles.charAt(tempPar + 1);
                } else {
                    result = _cycles.charAt(index);
                }
            }
        }
        return result;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int tempPar = 0;
        char result = c;
        for (int j = _cycles.length() - 1; j > 0; j--) {
            if (_cycles.charAt(j) == ')') {
                tempPar = j;
            }
            if (_cycles.charAt(j) == c) {
                int index = j - 1;
                if (_cycles.charAt(index) == '(') {
                    result = _cycles.charAt(tempPar - 1);
                } else {
                    result = _cycles.charAt(index);
                }
            }
        }
        return result;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int j = 0; j < _cycles.length(); j++) {
            if (_cycles.charAt(j) == permute(_cycles.charAt(j))
                    || _cycles.charAt(j) == invert(_cycles.charAt(j))) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Cycles of this permutation. */
    private String _cycles;
}
