import java.io.*;
import java.math.*;
import java.lang.*;
import java.util.*;

public class Set_up_CD_ROM 
{
   static final int SIZEOFWORDS = 45356;
   static final String FILENAME = "Words";
   static class Content
   {
      int sizeofsecondtable;
      int index;
      BigInteger a1;
      BigInteger b1;
      BigInteger [] secondhashtable;
      BigInteger [] collision;
      Content(int sizeofsecondtable, BigInteger a1, BigInteger b1)
      {
         this.sizeofsecondtable = sizeofsecondtable;
         this.index = sizeofsecondtable;
         this.a1 = a1;
         this.b1 = b1;
      }
   }

   public static void main(String[] args) 
   {
   		BigInteger base = new BigInteger("18514522701203512632016388292916154226711");
         // get the value of p
   	  	BigInteger p = base.nextProbablePrime();  
         Content [] HashTable = new Content [SIZEOFWORDS];
         //a and b are chose from 1 to p-1
         BigInteger a = rndBigInt(p.subtract(new BigInteger("1")));
         BigInteger b = rndBigInt(p.subtract(new BigInteger("1")));
         //a1 and b1 is the structure of secondary hash table
         BigInteger a1 = new BigInteger("0");
         BigInteger b1 = new BigInteger("0");
         //initizate the hashtable
         for(int i = 0; i < SIZEOFWORDS; i++)
         {
            HashTable[i] =  new Content(0, a1, b1);
            
         }
   	  	ReadLine(p, HashTable, a, b);
         //PrintCD_ROM(HashTable, p, a, b);
         PrintCD_ROM_FILE(HashTable, p, a, b);
   	  	  
   }

   private static void PrintCD_ROM(Content [] HashTable, BigInteger p , BigInteger a, BigInteger b)
   {

      System.out.println(p);
      System.out.println(a);
      System.out.println(b);
      System.out.println("i\tsize\ta1\tb1\tsecondhashtable:\t");
      for(int i = 0; i < SIZEOFWORDS; i++)
      {
         System.out.print(i + "\t" + HashTable[i].sizeofsecondtable + "\t" + HashTable[i].a1 + "\t" + HashTable[i].b1  +"\t" );
         if(HashTable[i].sizeofsecondtable >= 1)
         {
            for(BigInteger x : HashTable[i].secondhashtable)
            {
               System.out.print(x + "\t");
            }
         }
         System.out.println("");     
      }
   }
   private static void PrintCD_ROM_FILE(Content [] HashTable, BigInteger p , BigInteger a, BigInteger b) 
   {
      try
      {
         BufferedWriter bw = new BufferedWriter(new FileWriter("CD-ROM.txt"));
         bw.write(p.toString());
         bw.newLine();
         bw.write(a.toString());
         bw.newLine();
         bw.write(b.toString());
         bw.newLine();
         bw.write("i\tsize\ta1\tb1\tsecondhashtable:\t");
         bw.newLine();
         
         for(int i = 0; i < SIZEOFWORDS; i++)
         {
            bw.write(i + "\t" + HashTable[i].sizeofsecondtable + "\t" + HashTable[i].a1.toString() + "\t" + HashTable[i].b1.toString()  +"\t" );
            
            if(HashTable[i].sizeofsecondtable >= 1)
            {
               for(BigInteger x : HashTable[i].secondhashtable)
               {
                  if(x == null)
                     bw.write("null\t");
                  else
                     bw.write(x.toString() + "\t");
               }
               
            }
            
           bw.newLine();    
         }
         
         bw.close();
         

      }
      catch (Exception e)
      {
         e.printStackTrace();      
      }
      
   }



   private static void ReadLine(BigInteger p, Content [] HashTable, BigInteger a, BigInteger b)
   {
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) 
		{
   	    	String line;
            BigInteger n = new BigInteger(Integer.toString(SIZEOFWORDS));
            //do the first time to check the value of a and b is good
            //the size of the secondary hash should be less than 4*n
   	    	while ((line = br.readLine()) != null) 
   	    	{
               BigInteger CodeOfWords = EncodeKey(line);
   	      	BigInteger entrance = hash_function(CodeOfWords, p, a, b, n);
               HashTable[entrance.intValue()].sizeofsecondtable++;
   	    	}
            //check the vaule of a and b are valid

            if(CheckSizeOfSecondHashTable(HashTable, p))
            {
               //create secondary hashing
               CreateSecondHashTable(p, HashTable, a, b);
            }

    	}
    	catch (Exception e)
		{
		    //System.err.println(e.getMessage()); // handle exception
		}
   }

   private static void CreateSecondHashTable(BigInteger p, Content [] HashTable, BigInteger a, BigInteger b)
   {
      //Create the secondary hash table
      for(int i = 0; i < SIZEOFWORDS; i++)
      {
         if(HashTable[i].sizeofsecondtable >= 1)
         {
            int size = HashTable[i].sizeofsecondtable;
            //size * size about the secondhashtable
            HashTable[i].secondhashtable = new BigInteger[size*size];
            HashTable[i].collision = new BigInteger[size];
            
            
         }
         else
         {
            HashTable[i].collision = new BigInteger[0];
         }

      }


      //read a file again
      try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) 
      {
         String line;
         BigInteger n = new BigInteger(Integer.toString(SIZEOFWORDS));
         while ((line = br.readLine()) != null) 
         {
            BigInteger CodeOfWords = EncodeKey(line);
            BigInteger entrance = hash_function(CodeOfWords, p, a, b, n);
            int chance = HashTable[entrance.intValue()].sizeofsecondtable;
            //no mapping
            if(chance == 0)
               continue;
            if (chance == 1)
            {
               HashTable[entrance.intValue()].secondhashtable[0] = CodeOfWords;
            }
            if (chance >= 2)
            {
               //have collision and add to collision
               //System.out.println(entrance.intValue()+ "\tCollision add : " + CodeOfWords +"\t");
               int index_collision = HashTable[entrance.intValue()].index;
               HashTable[entrance.intValue()].collision[index_collision] = CodeOfWords;
               HashTable[entrance.intValue()].index++;
            }
            
         }
         //check the collison mapping correctly to the secondary HashTable
         
         for(int i = 0; i < SIZEOFWORDS; i++)
         {
            //for each entrance that has collision
            //we need to find valid a1 and b1 to map the words to the second hash without collision
            if(HashTable[i].collision.length > 1)
            {
               
               int n_size = HashTable[i].collision.length;
               Content valid_a_b = CheckCollisionOfCode(HashTable[i].collision, p, n_size);
               HashTable[i].a1 = valid_a_b.a1;
               HashTable[i].b1 = valid_a_b.b1;
               BigInteger n_i = BigInteger.valueOf(n_size);
               for(BigInteger codes: HashTable[i].collision)
               {
                  //System.out.println("i\tentrance\tcodes");
                  //System.out.println(i + "\t" + hash_function(codes, p, valid_a_b.a1, valid_a_b.b1, n_i.multiply(n_i)).intValue() +"\t" + codes);
                  HashTable[i].secondhashtable[hash_function(codes, p, valid_a_b.a1, valid_a_b.b1, n_i.multiply(n_i)).intValue()] = codes;

               }
               

            }

            
         }
         


      }
      catch (Exception e)
      {
          //System.err.println(e.getMessage()); // handle exception
      }
      
   }

   private static Content CheckCollisionOfCode(BigInteger[] collision, BigInteger p, int n)
   {
      boolean check = true;
      BigInteger a_i = rndBigInt(p.subtract(new BigInteger("1")));
      BigInteger b_i = rndBigInt(p.subtract(new BigInteger("1")));
      Content obj = new Content(0, a_i, b_i);
      while(true)
      {
          a_i = rndBigInt(p.subtract(new BigInteger("1")));
          b_i = rndBigInt(p.subtract(new BigInteger("1")));
         BigInteger n_i = BigInteger.valueOf(n);
          obj = new Content(0, a_i, b_i);

         HashSet<BigInteger> CheckDUP = new HashSet<>();

         for(BigInteger codes: collision)
         {
            BigInteger entrance = hash_function(codes, p, a_i, b_i, n_i.multiply(n_i));
            if(!CheckDUP.contains(entrance))
            {
               CheckDUP.add(entrance);
               check = false;

            }
            else
            {
               //return value a and b
                check = true;
                break;
            }
         }
         if(check == false)
            break;

      }
      
      return obj;


   }



   private static boolean CheckSizeOfSecondHashTable(Content [] HashTable, BigInteger p)
   {
      int SizeOfSecondHashTable = 0;
      for(int i = 0; i < SIZEOFWORDS; i++)
      {
         SizeOfSecondHashTable = SizeOfSecondHashTable + HashTable[i].sizeofsecondtable * HashTable[i].sizeofsecondtable;
      }

      if(SizeOfSecondHashTable > 4 * SIZEOFWORDS)
      {
         System.out.println("The value of a and b is invalid to guantee that the the size of the secondary hash should be less than 4*n\nRepick them! Redo it.");
         System.exit(0);
         return false;
      }
      
      System.out.println(" The value of a and b is valid !!!");
      return true;
      

   }
   //transfer word to code
   // abc  --->   123
   // zzz -----> 26 26 26
   private static BigInteger EncodeKey(String word)
   {
   	BigInteger code = new BigInteger("0");
   	BigInteger base = new BigInteger("0");
   	BigInteger multiple = new BigInteger("27");

   	for(int i = word.length() - 1; i >= 0 ; i--)
   	{
   		Integer cut = (word.charAt(i) - 'a' + 1);
   		BigInteger bcut =  BigInteger.valueOf(cut.intValue());//Bigint of the char value

   		base = bcut.multiply(multiple);
   		code = code.add(base);
   		multiple = multiple.multiply( new BigInteger("27") ) ;//return the times

   	}
   	return code;
   }
  

   private static BigInteger hash_function(BigInteger x, BigInteger p, BigInteger a, BigInteger b, BigInteger n)
   {
   		
 		BigInteger entrance = a.multiply(x).add(b).mod(p).mod(n);// ((ax + b) mod p ) mod m
      
      

 		return entrance;
   }

   private static BigInteger rndBigInt(BigInteger max) 
   {
	    Random rnd = new Random();
	    do 
	    {
	        BigInteger i = new BigInteger(max.bitLength(), rnd);
	        if (i.compareTo(max) <= 0 && i.compareTo(new BigInteger("0")) == 1 )
	            return i;
	    } 
	    while (true);
	}


}