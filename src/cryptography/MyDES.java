
package cryptography;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class MyDES {
    
//    String message;
String plaintext;
String key;
Map<Integer, Integer> initialPermutationTable = new HashMap();
Map<Integer, Integer> plaintextTable = new HashMap();
Map<Integer, Integer> keyTable = new HashMap();
Map<Integer, Integer> expansionPermutationTable = new HashMap();
Map<Integer, Integer> permutationChoiceOneTable = new HashMap();
Map<Integer, Integer> leftCircularShift = new HashMap();
Map<Integer, Integer> permutationChoiceTwoTable = new HashMap();

public MyDES( String pt, String ky)
        {
            plaintext = textToBinary(pt);
            key = textToBinary(ky);
        }

//convert the message to binary
private String textToBinary(String text)
{
    //convert the message to binary
  byte[] bytes = text.getBytes();
  StringBuilder binary = new StringBuilder();
  for (byte b : bytes)
  {
     int val = b;
     for (int i = 0; i < 8; i++)
     {
        binary.append((val & 128) == 0 ? 0 : 1);
        val <<= 1;
     }
     binary.append(' ');
  }
  String convertedtext = binary.toString().replaceAll("\\s","");

  //check if plaintext is 64 bit and pad with 0s if not
  int size = convertedtext.length();
  if (size < 64)
  {
      size = 64 - size;
      for (int i = 0; i < size; i++)
      {
          convertedtext += "0";
      }
  }
  //end
  
  return convertedtext;
}
//end


public void initialPermutation()
{
    //store plaintext values in a table
    
    for (int i=0; i<plaintext.length(); i++)
    {
        //i+1 is to make the table number stsrt from 1 to 32
        plaintextTable.put(i+1,plaintext.charAt(i) - '0');
    }
    //end
    
    //an array of the permutation numbers
    int[] permutationNumbers = {58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8
                                ,57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
    //end
    
    //a table containing new arrangement after initial permutation
    for (int i=0; i<plaintext.length(); i++)
    {
        //i+1 is to make the table number start from 1 and not 0
        initialPermutationTable.put(i+1,plaintextTable.get(permutationNumbers[i]));
    }
    //end
    
    //print the values from the table created above
    initialPermutationTable.entrySet().forEach((entry) -> {
        System.out.print(entry.getValue());
        });
    //end
}

//Beginning of Round Function

    //beginning of expansion permutation
    public void expansionPermutation()
    {
        int[] leftHalf = new int[32];
        int[] rightHalf = new int[32];
        
       int[] permutationNumbers = {32,1,2,3,4,5,4,5,6,7,8,9,8,9,10,11,12,13,12,13,14,15,16,17,16,17,
                                    18,19,20,21,20,21,22,23,24,25,24,25,26,27,28,29,28,29,30,31,32,1};
    //end
    
    int j =0;
    for (int i=0; i<plaintext.length(); i++)
    {
        if (i < 32)
        {
            leftHalf[i] = initialPermutationTable.get(i+1);
        }
        if (i >= 32)
        {
            rightHalf[j] = initialPermutationTable.get(i+1);
            j++;
        }
    }
   
    //a table containing new arrangement after expansion permutation
    for (int i=0; i<48; i++)
    {
        //i+1 is to make the table number start from 1 and not 0
        //i-1 is to make the table collect 1 index lesser because array starts one index lesser
        expansionPermutationTable.put(i+1,rightHalf[(permutationNumbers[i])-1]);
    }
    //end
    
    //print the values from the table created above
    expansionPermutationTable.entrySet().forEach((entry) -> {
        System.out.print(entry.getValue());
        });
    //end
    }   
    
    
    //Beginning of Permuted Choice-1
    public void permutedChoiceOne()
    {
        //store key values in a table
        for (int i=0; i<key.length(); i++)
        {
            //i+1 is to make the table number stsrt from 1 to 32
            keyTable.put(i+1,key.charAt(i) - '0');
        }
        //end
        int[] permutationNumbers = {57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36,
                                    63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4};
        
        //a table containing new arrangement after expansion permutation
        for (int i=0; i<56; i++)
        {
            //i+1 is to make the table number start from 1 and not 0
            permutationChoiceOneTable.put(i+1,keyTable.get(permutationNumbers[i]));
        }
        //end
    
        //print the values from the table created above
        permutationChoiceOneTable.entrySet().forEach((entry) -> {
            System.out.print(entry.getValue());
            });
    //end
    }
    
    public void leftCircularShift(int roundNumber)
    {
        int[] leftHalf = new int[28];
        int[] rightHalf = new int[28];
        int[] leftHalfC = new int[28];
        int[] rightHalfC = new int[28];
        int j =0;
        for (int i=0; i<56; i++)
        {
            if (i < 28)
            {
                leftHalf[i] = permutationChoiceOneTable.get(i+1);
            }
            else if (i >= 28)
            {
                rightHalf[j] = permutationChoiceOneTable.get(i+1);
                j++;
            }
        }
        
        int[] circularShiftNumbers = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
        int numberOfShifts = circularShiftNumbers[roundNumber-1];
        
        if (numberOfShifts == 1)
        {
            for (int i = 0; i<28; i++)
            {
                if (i == 27)
                {
                    leftHalfC[0] = leftHalf[i];
                    rightHalfC[0] = rightHalf[i];
                    
                }
                else
                {
                    leftHalfC[i+1] = leftHalf[i];
                    rightHalfC[i+1] = rightHalf[i];
                }
            }
        }
        
        
        if (numberOfShifts == 2)
        {
            for (int i = 0; i<28; i++)
            {
                if (i == 27)
                {
                    leftHalfC[1] = leftHalf[i];
                    rightHalfC[1] = rightHalf[i];
                    
                }
                else if (i == 26)
                {
                    leftHalfC[0] = leftHalf[i];
                    rightHalfC[0] = rightHalf[i];
                    
                }
                else 
                {
                    leftHalfC[i+2] = leftHalf[i];
                    rightHalfC[i+2] = rightHalf[i];
                }
            }
            
        }
        j= 0;
        for (int i=0; i<56; i++)
        {
            if (i < 28)
            {
                leftCircularShift.put(i+1,leftHalfC[i]);
            }
            else if (i >= 28)
            {
                leftCircularShift.put(i+1,rightHalfC[j]);
                j++;
            }
        }
        System.out.println(Arrays.toString(leftHalfC));
        System.out.println(Arrays.toString(rightHalfC));
        //print the values from the table created above
        leftCircularShift.entrySet().forEach((entry) -> {
            System.out.print(entry.getValue());
            });
        //end
    }
    
    public void permutedChoiceTwo()
    {
        
        int[] permutationNumbers = {14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2,
                                    41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};
        
        //a table containing new arrangement after  permutedChoiceTwo
        for (int i=0; i<48; i++)
        {
            //i+1 is to make the table number start from 1 and not 0
            permutationChoiceTwoTable.put(i+1,leftCircularShift.get(permutationNumbers[i]));
        }
        //end
    
        //print the values from the table created above
        permutationChoiceTwoTable.entrySet().forEach((entry) -> {
            System.out.print(entry.getValue());
            });
    //end
    }
    
    public void XOR(Map<Integer, Integer> left, Map<Integer, Integer> right)
    {
        Map<Integer, Integer> temp = new HashMap();
        for (int i=1; i<=48; i++)
        {
            //i+1 is to make the table number start from 1 and not 0
            temp.put(i,left.get(i));
        }
        for (int i=1; i<=48; i++)
        {
            //i+1 is to make the table number start from 1 and not 0
            left.put(i,right.get(i));
        }
        for (int i=1; i<=48; i++)
        {
            //i+1 is to make the table number start from 1 and not 0
            right.put(i,temp.get(i));
        }
    }
    
    public void SBox()
    {
        int[][] S = { {
        14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
        0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
        4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
        15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
    }, {
        15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
        3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
        0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
        13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
    }, {
        10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
        13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
        13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
        1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
    }, {
        7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
        13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
        10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
        3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
    }, {
        2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
        14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
        4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
        11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
    }, {
        12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
        10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
        9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
        4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
    }, {
        4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
        13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
        1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
        6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
    }, {
        13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
        1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
        7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
        2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
    } };
    }
}

