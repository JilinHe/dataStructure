import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(10, CompoundInterest.numYears(2030));
        assertEquals(15, CompoundInterest.numYears(2035));
        assertEquals(39, CompoundInterest.numYears(2059));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(411447.78, CompoundInterest.futureValue(10000.00, 10, 2059), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(125433.16, CompoundInterest.futureValueReal(10000.00, 10, 2059, 3), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(4425925.56, CompoundInterest.totalSavings(10000.00, 2059, 10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(1349278.94, CompoundInterest.totalSavingsReal(10000.00, 2059, 10, 3), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
