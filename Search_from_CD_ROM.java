import java.io.*;
import java.math.*;
import java.lang.*;
import java.util.*;

public class Search_from_CD_ROM
{
   static final int SIZEOFWORDS = 45356;
   static final String FILENAME = "CD-ROM.txt";
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
	   	if(args.length < 1)
	   	{
	   		System.out.println("Usage: java Search_from_CD_ROM <THE WORD YOU WANT TO SEARCH>");
	   	}
	   	else
	   	{
	   		String words = args[0];
	   		//System.out.println(words);
	   		BigInteger code = EncodeKey(words);
	   		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) 
	   		{
	   			String line;
	   			int count = 0;
	   			line = br.readLine();
	   			BigInteger p = new BigInteger(line);
	   			line = br.readLine();
	   			BigInteger a = new BigInteger(line);
	   			line = br.readLine();
	   			BigInteger b = new BigInteger(line);
	   			line = br.readLine();
				BigInteger n = new BigInteger(Integer.toString(SIZEOFWORDS));	   			
				BigInteger index = hash_function(code, p, a, b, n);
				//System.out.println(index);
	   			int entrance = index.intValue();
	   			boolean result = false;
	   			while ((line = br.readLine()) != null) 
	   			{
	   				if(entrance != count)
	   				{
	   					count++;
	   					continue;
	   				}
	   				
	   				if(entrance == count)
	   				{
	   					//System.out.println("111111");
	   					//System.out.println(line);
	   					String[] array = line.split("\\t");
	   					if(Integer.parseInt(array[1]) == 0)
	   						break;

	   					if(Integer.parseInt(array[1]) == 1)
	   					{
	   						BigInteger save = new BigInteger(array[4]);

	   						int res = code.compareTo(save);
	   						if(res == 0)
	   						{
	   							result = true;
	   							break;
	   						}
	   						else
	   						{
	   							result = false;
	   							break;
	   						}
	   					}

	   					if(Integer.parseInt(array[1]) > 1)
	   					{
	   						BigInteger a1 = new BigInteger(array[2]);
	   						BigInteger b1 = new BigInteger(array[3]);
	   						int size = Integer.parseInt(array[1])*Integer.parseInt(array[1]);
	   						BigInteger n_i = BigInteger.valueOf(size);

	   						BigInteger index_i = hash_function(code, p, a1, b1, n_i);

	   						int check_i = index_i.intValue();

	   						if(array[check_i + 4].equals("null"))
	   							break;
	   						//System.out.println("aaaa");
	   						BigInteger save_sec = new BigInteger(array[check_i + 4]);
	   						
	   						int res = code.compareTo(save_sec);
	   						if(res == 0)
	   						{
	   							result = true;
	   							break;
	   						}
	   						else
	   						{
	   							result = false;
	   							break;
	   						}
	   					}


	   				}

	   			}

	   		System.out.println("==The result of checking words in the dictionary: " + result);
	   		}
	   		 catch (Exception e)
		      {
		         e.printStackTrace();      
		      }


	   	}

   }

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

}