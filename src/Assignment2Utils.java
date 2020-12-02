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

    public static BigInteger multInverse(BigInteger a, BigInteger b) {
        BigInteger[] ans = eGCD(a, b);
        return (ans[1].compareTo(BigInteger.ONE) < 0) ? ans[1].add(b) : ans[1];
    }

    public static BigInteger chineseRemainderTheorem(BigInteger[] b, BigInteger[] n) {
        BigInteger[] N = new BigInteger[n.length];
        BigInteger[] Y = new BigInteger[n.length];

        BigInteger nProd = BigInteger.ONE;

        for (BigInteger i : n)
            nProd = nProd.multiply(i);

        for (int i = 0; i < n.length; i++) {
            N[i] = nProd.divide(n[i]);
            Y[i] = multInverse(N[i], (n[i]));
        }

        BigInteger sum = BigInteger.ZERO;

        for (int i = 0; i < n.length; i++)
            sum = sum.add( b[i].multiply(N[i].multiply(Y[i])));

        return sum.mod(nProd);
    }

    public static void main(String[] args) {
        BigInteger[] a = new BigInteger[3];
        a[0] = new BigInteger("2");
        a[1] = new BigInteger("3");
        a[2] = new BigInteger("2");

        BigInteger[] n = new BigInteger[3];
        n[0] = new BigInteger("3");
        n[1] = new BigInteger("5");
        n[2] = new BigInteger("7");

        System.out.println(chineseRemainderTheorem(a, n));
    }
}
