import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;

public class Assignment2 {

    /**
     * Simple Method for writing a string to a file.
     *
     * @param fileName some filename
     * @param value some string value
     */
    public static void writeToFile(String fileName, String value) throws IOException {
        Files.write(Paths.get(fileName), Collections.singleton(value));
    }

    /**
     * Method for finding the euler totient of two probable primes.
     *
     * @param p a Probable Prime with the type, BigInteger.
     * @param q a Probable Prime with the type, BigInteger.
     * @return returns the euler totient of p and q.
     */
    public static BigInteger eulerTotient(BigInteger p, BigInteger q) {
        BigInteger phiOfP, phiOfQ;

        phiOfP = p.subtract(BigInteger.ONE);
        phiOfQ = q.subtract(BigInteger.ONE);

        return phiOfP.multiply(phiOfQ);
    }

    /**
     * Method for performing right-to=left modular exponentiation
     *
     * @param a some BigInteger
     * @param exp some exponent of the type BigInteger
     * @param mod some modulus of the type BigInteger
     * @return returns the modular exponentiation of the input values.
     */
    public static BigInteger modExponent(BigInteger a, BigInteger exp, BigInteger mod) {

        int k = exp.bitLength();

        BigInteger y = new BigInteger("1");

        for (int i = k - 1; i >= 0; i--){
            y = y.multiply(y).mod(mod);
            y = (exp.testBit(i)) ? y.multiply(a).mod(mod) : y;
        }

        return y;
    }

    /**
     * Method for performing the Extended Euclidean Algorithm.
     *
     * @param a some BigInteger a
     * @param b some BigInteger b
     * @return returns an array of BigInteger's containing {gcd, x coefficient, y coefficient}
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

    /**
     * Method for preforming standard gcd Algorithm
     *
     * @param a some BigInteger a
     * @param b some BigInteger b
     * @return returns the greatest common divisor of the BigIntegers `a` and `b`
     */
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return (b.intValue() == 0) ? a : gcd(b, a.mod(b));
    }

    /**
     * Method for finding the multiplicative inverse
     *
     * @param a some number a
     * @param b some modulus b
     * @return returns the multiplicative inverse of a number `a` and a modulus `b`
     */
    public static BigInteger multInverse(BigInteger a, BigInteger b) {
        BigInteger[] ans = eGCD(a, b);
        return (ans[1].intValue() == 1) ? ans[1].add(b) : ans[1];
    }

    /**
     * Method for finding a message signature using Chinese Remainder Theorem
     * 
     * @param c digest of some input with the type BigInteger
     * @param d a decryption component
     * @param p a public key with the type BigInteger
     * @param q a private key with the type BigInteger
     * @return returns a signed digest
     */
    public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger p, BigInteger q) {
        BigInteger crypt_p = modExponent(c, d.mod(p.subtract(BigInteger.ONE)), p);
        BigInteger crypt_q = modExponent(c, d.mod(q.subtract(BigInteger.ONE)), q);

        BigInteger qInv = multInverse(q, p);

        return crypt_p.add(q.multiply(qInv.multiply(crypt_p.subtract(crypt_q)).mod(p)));
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

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

            phi = eulerTotient(p, q);
        } while (gcd(e, phi).intValue() != 1);

        writeToFile("Modulus.txt", n.toString(16));

        BigInteger d = multInverse(e, phi);

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        File ip = new File(inputFile);
        InputStream is = new FileInputStream(ip);
        byte[] inputBytes = is.readAllBytes();
        is.close();

        BigInteger c = new BigInteger(1, md.digest(inputBytes));

        BigInteger signedDigest = decrypt(c, d, p, q);

        System.out.println(signedDigest.toString(16));
    }
}
