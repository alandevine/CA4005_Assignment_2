import java.math.BigInteger;

//  Collection of methods for use in implementing Assignment2

public class Assignment2Utils {

    public static byte[] bigIntegerToByteArray(BigInteger a) {
        // Convert a big integer to a byte array without the two's complement issue.
        return a.toByteArray();
    }

    public static BigInteger[] eGCD(BigInteger a, BigInteger b) {
        // gcd(a, b) = ax + by

        BigInteger[] sol = new BigInteger[3];

        if (b.intValue() == 0) {
            sol[0] = a;
            sol[1] = BigInteger.ONE;
            sol[2] = BigInteger.ZERO;
            return sol;
        }

        BigInteger[] tmp = eGCD(b, a.mod(b));

        // gcd
        sol[0] = tmp[0];
        // x coefficient
        sol[1] = tmp[2];
        // y coefficient
        sol[2] = tmp[1].subtract(a.divide(b).multiply(tmp[2]));

        return sol;
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return (b.intValue() == 0) ? a : gcd(b, a.mod(b));
    }

    public static BigInteger modInverse(BigInteger a, BigInteger b) {
        assert(gcd(a, b) == BigInteger.ONE);
        BigInteger[] ans = eGCD(a, b);

        if (ans[1].compareTo(BigInteger.ONE) != 0) return ans[1];
        return ans[1].add(b);
    }

    public static void main(String[] args) {
        BigInteger a = new BigInteger("3");
        BigInteger b = new BigInteger("17");
        BigInteger c = new BigInteger("1");
        BigInteger d = new BigInteger("1");

        // BigInteger[] n = {a, b, c, d};

        System.out.println(modInverse(a, b));
    }
}
