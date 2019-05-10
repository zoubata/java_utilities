package com.zoubwolrd.java.utils.cryptography.CaesarCipher;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CaesarCipher {
	
	public static int SearchForCharacter(char[] array, char a) {
		
		int index = Arrays.binarySearch(array, a);
		int newIndex = 0;
		
		// plain text with whitespace
		if (index == -1) {
			
			newIndex = index + 27;
			
		} else {
			
			newIndex = index + 3;
					
			if (newIndex > 25)
				newIndex = newIndex - 26;
			
		}
		return newIndex;
	}
	
	public static void main(String[] args) {
		
		// Create Scanner object
		Scanner scan = new Scanner(System.in);
		
		String inputString;
				
		System.out.print("Enter plain text: ");
		inputString = scan.nextLine();
		
		scan.close();
		
		String alphabetString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
		char[] alphabetChars = alphabetString.toCharArray();
		
		String s = inputString.toUpperCase();
		char aChar = ' ';
		
		List<Character> charList = new ArrayList<Character>();
		
		for (int i = 0; i < s.length(); i++) {
			
			aChar = s.charAt(i);
			
			int p = SearchForCharacter(alphabetChars, aChar);
			aChar = alphabetChars[p];
			charList.add(aChar);
						
		}
		
		for (int k = 0; k < charList.size(); k++) {
			System.out.print(charList.get(k));
		}
	}
}


