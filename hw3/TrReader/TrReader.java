import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Jilin He
 */
public class TrReader extends Reader {
    private String from;
    private String to;
    private Reader str;
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        this.str = str;
        this.from = from;
        this.to = to;
    }

//    @Override
//    public int read(char[] chars) throws IOException {
//        int temp = str.read(chars);
//        for (int j = 0; j < temp; j++) {
//            for (int x = 0; x < from.length(); x++) {
//                if (chars[j] == from.charAt(x)) {
//                    chars[j] = to.charAt(x);
//                }
//            }
//        }
//        return temp;
//    }

    @Override
    public int read(char[] chars, int i, int i1) throws IOException {
        int temp = this.str.read(chars, i, i1);
        if (temp == -1) {
            return temp;
        } else {
            for (int j = i; j < i + i1; j++) {
                int fIndex = this.from.indexOf(chars[j]);
                if (fIndex >= 0) {
                    chars[j] = this.to.charAt(fIndex);
                }
            }
            return temp;
        }
    }

    @Override
    public void close() throws IOException {
        str.close();
    }

}
