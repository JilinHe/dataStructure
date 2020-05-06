/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static String P1 =
            "^((0[1-9]|[1-9])|10|11|12)." +
                    "(((0|1|2)[0-9]|[1-9])|30|31).(19|[2-9][0-9])[0-9]{2}$";

    /** Pattern to match 61b notation for literal IntLists. */
    public static String P2 =
            "^\\(((\\d+),\\s*)+?(\\d+)\\)$";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static String P3 =
            "^(([a-z]|[^-\\.][a-z-]*?[^-\\.])\\.)*[a-z]{2,6}$";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static String P4 = "^[^0-9].*$";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static String P5 =
            "^((((0|1)?[0-9]?[0-9])|(2[0-4][0-9])" +
                    "|(25[0-5]))(\\.)){3}(((0|1)?[0-9]?[0-9])" +
                    "|(2[0-4][0-9])|(25[0-5]))$";

}
