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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
    

public class HybridMRSADES {
   private final static BigInteger one      = new BigInteger("1");
   private final static BigInteger two      = new BigInteger("2");
   private final static SecureRandom random = new SecureRandom();

   private BigInteger privateKey;
   private BigInteger publicKey;
   //private BigInteger modulus;
   public BigInteger modulus;
   private BigInteger f,g;
   private int messagelength;

   // generate an N-bit (roughly) public and private key
   HybridMRSADES(int N) {
       //long startTime = System.currentTimeMillis();
      BigInteger p = BigInteger.probablePrime(N/2, random);
      BigInteger q = BigInteger.probablePrime(N/2, random);
      BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

      modulus    = p.multiply(q);                                  
      publicKey  = new BigInteger("65537");     // common value in practice = 2^16 + 1
      privateKey = publicKey.modInverse(phi);
      f = (publicKey.multiply(two)).add(one);
      g = modulus.subtract(one);
      publicKey = FinalPermutation(f);
      modulus = g.add(one);
     // System.out.println("Time taken to generate key = " + (System.currentTimeMillis() - startTime));
   }


   BigInteger encrypt(BigInteger message) {
       messagelength = message.toString().length();
       publicKey = inverseFinalPermutation(publicKey);
       publicKey = (f.subtract(one).divide(two));
       
      
      return initialPermutation(message).modPow(publicKey, modulus);
   }

   BigInteger decrypt(BigInteger encrypted) {
       
      return inverseInitialPermutation(encrypted.modPow(privateKey, modulus));
      
   }

   public String toString() {
      String s = "";
      s += "public  = " + publicKey + "\n";
      s += "private = " + privateKey + "\n";
      s += "modulus = " + g;
      return s;
   }
   
   public BigInteger initialPermutation(BigInteger plaintext)
{
    
    int[] initialPermutationArray = new int[64];
    String temp = plaintext.toString();
    //check if plaintext is 64 bit and pad with 1s if not
  int size = temp.length();
  if (size < 64)
  {
      size = 64 - size;
      for (int i = 0; i < size; i++)
      {
          temp += "1";
      }
  }
  
  //System.out.println("after padding = " +temp);
  //end
    
    //store plaintext values in an int array
    
    int[] plaintextArray = new int[temp.length()];
    for (int i = 0; i < temp.length(); i++)
    {
        plaintextArray[i] = temp.charAt(i) - '0';
    }
    
    //System.out.println("plaintext array = " + Arrays.toString(plaintextArray));
    //end
    
    //an array of the permutation numbers
    int[] permutationNumbers = {58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8
                                ,57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
    
    //end
    
    //an array containing new arrangement after initial permutation
    for (int i=0; i<temp.length(); i++)
    {
        //i+1 is to make the table number start from 1 and not 0
        initialPermutationArray[i] = plaintextArray[permutationNumbers[i]-1];
    }
    //System.out.println("after permutation = " + Arrays.toString(initialPermutationArray));
    //end
    
    // convert array int Integer
     BigInteger result = new BigInteger("0");
     BigInteger ten = new BigInteger("10");
    for( int i=0; i < initialPermutationArray.length; i++){
      result=result.multiply(ten);
      result= result.add(BigInteger.valueOf(initialPermutationArray[i]));
    }
    //System.out.println(result);
    return result;
    
}
   
   public BigInteger inverseInitialPermutation(BigInteger plaintext)
{
    
    int[] inverseInitialPermutationArray = new int[64];
    String temp = plaintext.toString();
   
    //store plaintext values in an int array
    
    int[] plaintextArray = new int[temp.length()];
    for (int i = 0; i < temp.length(); i++)
    {
        plaintextArray[i] = temp.charAt(i) - '0';
    }
    
    //an array of the permutation numbers
    int[] permutationNumbers = {58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8
                                ,57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
    
    //end
    
    //an array containing new arrangement after initial permutation
    for (int i=0; i<plaintextArray.length; i++)
    {
        //i+1 is to make the table number start from 1 and not 0
        inverseInitialPermutationArray[permutationNumbers[i]-1] = plaintextArray[i];
    }
    
    // convert array int Integer
     BigInteger result = new BigInteger("0");
     BigInteger ten = new BigInteger("10");
    for( int i=0; i < inverseInitialPermutationArray.length; i++){
      result=result.multiply(ten);
      result= result.add(BigInteger.valueOf(inverseInitialPermutationArray[i]));
    }
    
    //Remove padding from plaintext
    temp = result.toString();
  //int size = temp.length();
  String subTemp = temp.substring(0, messagelength);
  result  = new BigInteger(subTemp);
    return result;
    
}
 
   public BigInteger FinalPermutation(BigInteger key)
{
    key = publicKey;
    int[] FinalPermutationArray = new int[64];
    String temp = key.toString();
    //check if plaintext is 64 bit and pad with 1s if not
  int size = temp.length();
  if (size < 64)
  {
      size = 64 - size;
      for (int i = 0; i < size; i++)
      {
          temp += "1";
      }
  }
  
  //System.out.println("after padding = " +temp);
  //end
    
    //store plaintext values in an int array
    
    int[] ciphertextArray = new int[temp.length()];
    for (int i = 0; i < temp.length(); i++)
    {
        ciphertextArray[i] = temp.charAt(i) - '0';
    }
    
    //System.out.println("plaintext array = " + Arrays.toString(plaintextArray));
    //end
    
    //an array of the permutation numbers
    int[] permutationNumbers = {40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
                                36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25};
    //end
    
    //an array containing new arrangement after initial permutation
    for (int i=0; i<temp.length(); i++)
    {
        //i+1 is to make the table number start from 1 and not 0
        FinalPermutationArray[i] = ciphertextArray[permutationNumbers[i]-1];
    }
    //System.out.println("after permutation = " + Arrays.toString(initialPermutationArray));
    //end
    
    // convert array int Integer
     BigInteger result = new BigInteger("0");
     BigInteger ten = new BigInteger("10");
    for( int i=0; i < FinalPermutationArray.length; i++){
      result=result.multiply(ten);
      result= result.add(BigInteger.valueOf(FinalPermutationArray[i]));
    }
    //System.out.println(result);
    return result;
    
}
   
   public BigInteger inverseFinalPermutation(BigInteger key)
{
    
    int[] inverseFinalPermutationArray = new int[64];
    String temp = key.toString();
   
    //store plaintext values in an int array
    
    int[] keyArray = new int[temp.length()];
    for (int i = 0; i < temp.length(); i++)
    {
        keyArray[i] = temp.charAt(i) - '0';
    }
    
    //an array of the permutation numbers
    int[] permutationNumbers = {40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
                                36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25};
    
    //end
    
    //an array containing new arrangement after initial permutation
    for (int i=0; i<keyArray.length; i++)
    {
        //i+1 is to make the table number start from 1 and not 0
        inverseFinalPermutationArray[permutationNumbers[i]-1] = keyArray[i];
    }
    
    // convert array int Integer
     BigInteger result = new BigInteger("0");
     BigInteger ten = new BigInteger("10");
    for( int i=0; i < inverseFinalPermutationArray.length; i++){
      result=result.multiply(ten);
      result= result.add(BigInteger.valueOf(inverseFinalPermutationArray[i]));
    }
    
    //check if plaintext is 64 bit and pad with 1s if not
    temp = result.toString();
  //int size = temp.length();
  String subTemp = temp.substring(0, 10);
  result  = new BigInteger(subTemp);
    return result;
    
}
}



