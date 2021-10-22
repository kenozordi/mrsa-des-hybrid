# MRSA-DES-hybrid - released 2019
A Hybrid Encryption Algorithm using both Modified Rivest Shamir Adleman (MRSA) Algorithm and Data Encryption Standard (DES) Algorithm.

The algorithm is a hybrid algorithm consisting of two algorithms
-	Modified Rivest Shamir Adleman (MRSA) Algorithm Based on Key Generation
-	Data Encryption Standard (DES) Algorithm

In this algorithm, the following modifications is made to produce the HYBRID MRSA-DES algorithm:- 
## Key Generation
- The public key is hidden in a new variable, and the DES final permutation is performed on it, so that it appears in a different form when stored or shared.
- The modulus is hidden in a new variable so that it appears in a different form when stored or shared.

## Encryption
-	The inverse final permutation from the DES algorithm is performed on public key
-	The initial permutation from the DES algorithm is performed on the plaintext
-	The public key is used to Encrypt the plaintext using RSA formular

# Decryption
-	The inverse initial permutation from the DES algorithm is performed on plaintext 
- The private key is used to decrypt the message using the RSA formular
