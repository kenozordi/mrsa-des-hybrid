
package cryptography;


public class TestDES {
    public static void main(String[] args)
    {
        MyDES DES = new MyDES("Hello","123");
        System.out.println(DES.plaintext);
        DES.initialPermutation();
        System.out.println();
        DES.expansionPermutation();
        System.out.println();
        DES.permutedChoiceOne();
        System.out.println();
        //DES.leftCircularShift(1);
        DES.leftCircularShift(3);
        System.out.println();
        DES.permutedChoiceTwo();
        System.out.println();
        System.out.println(DES.permutationChoiceTwoTable);
        System.out.println(DES.expansionPermutationTable);
        DES.XOR(DES.permutationChoiceTwoTable, DES.expansionPermutationTable);
        System.out.println(DES.permutationChoiceTwoTable);
        System.out.println(DES.expansionPermutationTable);  
    }
    
}
