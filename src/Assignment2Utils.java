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

    public static BigInteger gcdOfN(BigInteger[] n) {
        // GCD implemented for n number of BigIntegers
        BigInteger sol = n[0];

        for (int i = 0; i < n.length; i++)
            sol = gcd(sol, n[i]);

        return sol;
    }

    public static BigInteger multiplicativeInverse(BigInteger a, BigInteger b) {
        return a;
    }

    public static BigInteger chineseRemainderTheorem(BigInteger a, BigInteger b) {
        return a;
    }

    public static void main(String[] args) {
        BigInteger a = new BigInteger("4");
        BigInteger b = new BigInteger("12");
        BigInteger c = new BigInteger("6");
        BigInteger d = new BigInteger("18");

        BigInteger[] n = {a, b, c, d};

        System.out.println(gcdOfN(n));
        System.out.println(gcd(a, b));

    }
}
