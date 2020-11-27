import java.math.BigInteger;

//  Collection of methods for use in implementing Assignment2

public class Assignment2Utils {

    public static byte[] bigIntegerToByteArray(BigInteger a) {
        // Convert a big integer to a byte array without the two's complement issue.
        return a.toByteArray();
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return (b.intValue() == 0) ? a : gcd(b, a.mod(b));
    }

    public static BigInteger multiplicativeInverse(BigInteger a, BigInteger b) {
        return a;
    }

    public static BigInteger chineseRemainderTheorem(BigInteger a, BigInteger b) {
        return a;
    }

    public static void main(String[] args) {
        BigInteger a = new BigInteger("10");
        BigInteger b = new BigInteger("12");

        System.out.println(gcd(a, b));
    }
}
