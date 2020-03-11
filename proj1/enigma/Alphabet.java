package enigma;

import static enigma.EnigmaException.error;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Jilin He
 */
class Alphabet {
    /** The input of the alphabet. */
    private String input;

    /**
     * A new alphabet containing CHARS.  Character number #k has index
     * K (numbering from 0). No character may be duplicated.
     */
    Alphabet(String chars) {
        this.input = chars;
    }

    /**
     * A default alphabet of all upper-case characters.
     */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Returns the size of the alphabet.
     */
    int size() {
        return this.input.length();
    }

    /**
     * Returns true if CH is in this alphabet.
     */
    boolean contains(char ch) {
        for (int i = 0; i < this.input.length(); i++) {
            if (ch == this.input.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns character number INDEX in the alphabet, where
     * 0 <= INDEX < size().
     */
    char toChar(int index) {
        if (index < 0 || index > size()) {
            throw error("The index is out of the alphabet! ");
        }
        char temp = this.input.charAt(index);
        return temp;
    }

    /**
     * Returns the index of character CH which must be in
     * the alphabet. This is the inverse of toChar().
     */
    int toInt(char ch) {
        for (int i = 0; i < this.input.length(); i++) {
            if (ch == this.input.charAt(i)) {
                return i;
            }
        }
        throw error("The ch is out of the alphabet!");
    }
}
