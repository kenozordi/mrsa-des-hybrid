/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptography;

import java.math.BigInteger;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class TestHybridMRSADES {
    public static void main(String[] args) {
       Scanner input = new Scanner(System.in);
      //int N = Integer.parseInt(args[0]);
//      System.out.println("Please enter a number");
//      int N = input.nextInt();
//      HybridMRSADES key = new HybridMRSADES(N);
        //long startTime = System.currentTimeMillis();
        long startTime = System.nanoTime();
       
        HybridMRSADES key = new HybridMRSADES(1024);
        //startTime = System.currentTimeMillis() - startTime;
        startTime = System.nanoTime() - startTime;
        System.out.println("Time taken to generate key = " + (startTime));
      System.out.println(key);
 
      // create random message, encrypt and decrypt
      //BigInteger message = new BigInteger(N-1, random);

      // create message by converting string to integer
       String s = "Password01234567";
      byte[] bytes = s.getBytes();
      BigInteger message = new BigInteger(bytes);
      System.out.println(message);
      startTime = System.nanoTime();
      BigInteger encrypt = key.encrypt(message);
      startTime = System.nanoTime() - startTime;
      System.out.println("Time taken to encrypt = " + (startTime));
      
      startTime = System.nanoTime();
      BigInteger decrypt = key.decrypt(encrypt);
      startTime = System.nanoTime() - startTime;
      System.out.println("Time taken to decrypt = " + (startTime));
      System.out.println();
      System.out.println("message   = " + message);
      System.out.println("encrypted = " + encrypt);
      System.out.println("length of encrypted File = " + encrypt.toString().length());
      System.out.println("length of modulus = " + key.modulus.toString().length());
      System.out.println("decrypted = " + decrypt);
      System.out.println(new String(decrypt.toByteArray()));
      
      
      //key.initialPermutation(message);
      
   }
}
