# Quick_Word_Searcher
**Used perfect hashing to searched a word from 45356-word dictionary only in O(1) time**

# USAGE

$ javac Set_up_CD_ROM.java

$ java Set_up_CD_ROM

$ javac Seach_from_CD_ROM.java

$ java Seach_from_CD_ROM <THE WORD YOU WANT TO SEARCH>

# EXAMPLE OUTPUT

$ java Search_from_CD_ROM my

==The result of checking words in the dictionary: true

$ java Search_from_CD_ROM kkkkk

==The result of checking words in the dictionary: false

$ java Search_from_CD_ROM help

==The result of checking words in the dictionary: true

$ java Search_from_CD_ROM tttt

==The result of checking words in the dictionary: false

$ java Search_from_CD_ROM dictionary

==The result of checking words in the dictionary: true


# Theory of PERFECT HASHING https://en.wikipedia.org/wiki/Perfect_hash_function

1. encode all words (27 is the base)

2. build a data structure that stores the encoded words(use ((ax+b)mod p mod n))

3. check the size of secondary hash table is less than 4*n

4. if there has a collision in each entrance, build secondary hash table to store(use ((a_i * x+ b_i)mod p mod n_i^2))

5. print the structure into CD-ROM.txt

6. Read CD-ROM.txt to seach, based on the hash_funciton to quick find the specific line number

7. if having collison, use hash_funciton(a_i,b_i,n_i^2) to check the secondary hashtable

# NOTICE
 
1.In my code you can see:

static final int SIZEOFWORDS = 45356;
   		
static final String FILENAME = "Words";

The Words file contains 45356 English words. So if you want to test another dictionary just change these two variables.

2.Set_up_CD_ROM.java will generate CD-ROM.txt; Search_from_CD_ROM.java will read CD-ROM.txt by default
3.My organizaiotn of CD-ROM.txt
	
		line 1: p
		line 2: a
		line 3: b
		line 4: i	size	a:q1	b1	secondhashtable:
		---------------------------------Store all the words in hashtable-------------------------
		line 5: 0	0	0	0
		line 6: 1	1	0	0	word1
		line 7: 2	3	a_2	b_2	null	null	word2	null	null	word3	null	null	word4	





