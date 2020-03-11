package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Jilin He
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    void advance() {
        set((setting() + 1) % permutation().size());
    }

    @Override
    boolean atNotch() {
        char ch = permutation().alphabet().toChar(setting());
        for (int i = 0; i < _notches.length(); i++) {
            if (_notches.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    @Override
    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return true;
    }

    /** The notch of the rotor. */
    private String _notches;

}
