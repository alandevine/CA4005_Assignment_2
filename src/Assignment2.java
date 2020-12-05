import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;

public class Assignment2 {

    public static void writeToFile(String fileName, String value) throws IOException {
        Files.write(Paths.get(fileName), Collections.singleton(value));
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder buff = new StringBuilder();
        for (byte b : bytes) {
            buff.append(String.format("%02X", b));
        }
        return buff.toString();
    }

    public static byte[] BItoByte(BigInteger n) {
        return n.toByteArray();
    }

    public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger p, BigInteger q) {

        // m = c^d (mod n)

        // My implementation of CRT takes arrays of big integers as input
        BigInteger[] crypt_p = new BigInteger[] {CryptoMath.rightToLeftModExponentiation(c, d.mod(p.subtract(BigInteger.ONE)), p)};
        BigInteger[] crypt_q = new BigInteger[] {CryptoMath.rightToLeftModExponentiation(c, d.mod(q.subtract(BigInteger.ONE)), q)};

        return CryptoMath.chineseRemainderTheorem(crypt_p, crypt_q);

    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException{

        String inputFile = (args.length > 0) ? args[0] : null;

        if (inputFile == null) {
            System.out.println("Please provide a file to be encrypted.");
            System.exit(0);
        }

        Random rand = new Random();

        final BigInteger e = new BigInteger("65537");

        BigInteger p, q, n, phi;

        do {
            p = BigInteger.probablePrime(512, rand);
            q = BigInteger.probablePrime(512, rand);

            n = p.multiply(q);

            writeToFile("Modulus.txt", n.toString(16));

            phi = CryptoMath.eulerTotient(p, q);
        } while (CryptoMath.gcd(e, phi).intValue() != 1);

        BigInteger d = CryptoMath.multInverse(e, phi);


        MessageDigest md = MessageDigest.getInstance("SHA-256");

        File ip = new File(inputFile);
        InputStream is = new FileInputStream(ip);
        byte[] inputBytes = is.readAllBytes();

        BigInteger c = new BigInteger(1, inputBytes);

        BigInteger signedDigest = decrypt(c, d, p, q);

        System.out.println(signedDigest.toString(16));
    }
}

class CryptoMath {

    /**
     * Generate the Euler Totient of two BigIntegers p, and q. This method assumes that
     * both p and q are probable primes and calculates phi based on the equation
     * phi(p*q) = phi(p)*phi(q)
     *
     * @param p Probable Prime of Type BigInteger
     * @param q Probable Prime of Type BigInteger
     * @return returns phi(p*q) of type BigInteger
     */
    public static BigInteger eulerTotient(BigInteger p, BigInteger q) {
        BigInteger phiOfP, phiOfQ;

        phiOfP = p.subtract(BigInteger.ONE);
        phiOfQ = q.subtract(BigInteger.ONE);

        return phiOfP.multiply(phiOfQ);

    }

    /**
     * Implementation of the Extended Euclidean Algorithm
     *
     * @param a Some BigInteger a
     * @param b Some BigInteger b
     * @return BigInteger Array containing gcd, x coefficient and y coefficient
     */
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

        assert(b.length == n.length);

        BigInteger[] N = new BigInteger[n.length];
        BigInteger[] Y = new BigInteger[b.length];

        BigInteger nProd = BigInteger.ONE;

        for (BigInteger i : n)
            nProd = nProd.multiply(i);

        for (int i = 0; i < n.length; i++) {
            N[i] = nProd.divide(n[i]);
            Y[i] = multInverse(N[i], (n[i]));
        }

        BigInteger sum = BigInteger.ZERO;

        for (int i = 0; i < n.length; i++)
            sum = sum.add(b[i].multiply(N[i].multiply(Y[i])));

        return sum.mod(nProd);
    }

    public static BigInteger rightToLeftModExponentiation(BigInteger a, BigInteger exp, BigInteger mod) {

        /*
         * This method implements right-to-left modular exponentiation
         * */

        int k = exp.bitLength();

        BigInteger y = new BigInteger("1");

        for (int i = k - 1; i >= 0; i--){
            y = y.multiply(y).mod(mod);
            y = (exp.testBit(i)) ? y.multiply(a).mod(mod) : y;
        }

        return y;
    }
}