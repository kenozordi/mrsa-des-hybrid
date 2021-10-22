/******************************************************************************
 *  Compilation:  javac RSA.java
 *  Execution:    java RSA N
 *  
 *  Generate an N-bit public and private RSA key and use to encrypt
 *  and decrypt a random message.
 * 
 *  % java RSA 50
 *  public  = 65537
 *  private = 553699199426609
 *  modulus = 825641896390631
 *  message   = 48194775244950
 *  encrpyted = 321340212160104
 *  decrypted = 48194775244950
 *
 *  Known bugs (not addressed for simplicity)
 *  -----------------------------------------
 *  - It could be the case that the message >= modulus. To avoid, use
 *    a do-while loop to generate key until modulus happen to be exactly N bits.
 *
 *  - It's possible that gcd(phi, publicKey) != 1 in which case
 *    the key generation fails. This will only happen if phi is a
 *    multiple of 65537. To avoid, use a do-while loop to generate
 *    keys until the gcd is 1.
 *
 ******************************************************************************/
package cryptography;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;
    

public class RSAKeyGen {
   private final static BigInteger one      = new BigInteger("1");
   private final static BigInteger two      = new BigInteger("2");
   private final static SecureRandom random = new SecureRandom();

   private BigInteger privateKey;
   private BigInteger publicKey;
   private BigInteger modulus;
   private BigInteger f,g;

   // generate an N-bit (roughly) public and private key
   RSAKeyGen(int N) {
      BigInteger p = BigInteger.probablePrime(N/2, random);
      BigInteger q = BigInteger.probablePrime(N/2, random);
      BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

      modulus    = p.multiply(q);                                  
      publicKey  = new BigInteger("65537");     // common value in practice = 2^16 + 1
      privateKey = publicKey.modInverse(phi);
      f = (publicKey.multiply(two)).add(one);
      g = modulus.subtract(one);
      publicKey = (f.subtract(one).divide(two));
      modulus = g.add(one);
   }


   BigInteger encrypt(BigInteger message) {
      return message.modPow(publicKey, modulus);
   }

   BigInteger decrypt(BigInteger encrypted) {
      return encrypted.modPow(privateKey, modulus);
   }

   public String toString() {
      String s = "";
      s += "public  = " + publicKey  + "\n";
      s += "private = " + privateKey + "\n";
      s += "modulus = " + modulus;
      return s;
   }
 
   public static void main(String[] args) {
       Scanner input = new Scanner(System.in);
      //int N = Integer.parseInt(args[0]);
      System.out.println("Please enter a number");
      int N = input.nextInt();
      RSA key = new RSA(N);
      System.out.println(key);
 
      // create random message, encrypt and decrypt
      BigInteger message = new BigInteger(N-1, random);

      //// create message by converting string to integer
      // String s = "test";
      // byte[] bytes = s.getBytes();
      // BigInteger message = new BigInteger(bytes);

      BigInteger encrypt = key.encrypt(message);
      BigInteger decrypt = key.decrypt(encrypt);
      System.out.println("message   = " + message);
      System.out.println("encrypted = " + encrypt);
      System.out.println("decrypted = " + decrypt);
      
   }
}



