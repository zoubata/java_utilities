package com.zoubworld.java.utils.security;

import java.util.HashSet;
import java.util.Set;

import com.zoubworld.utils.JavaUtils;

public class PassWordChecker {

	public PassWordChecker() {
		// TODO Auto-generated constructor stub
	}
	/** password checker :
	 * - basic check of password 
	 * - don't contain a first name of more than 3 character
	 * - don't contain a last name of more than 3 character
	 * to return true
	 * example "P!erre=3.141592"
	 *  */
	public static boolean checkStrongPassWord(String passord)
	{
		if(!checkPassWord( passord))
			return false;
		String passordl = passord.toLowerCase();
		String[] first_names = JavaUtils.read("src/com/zoubworld/java/utils/security/first_names.all.txt").split("\n");
		String[] last_names = JavaUtils.read("src/com/zoubworld/java/utils/security/last_names.all.txt").split("\n");
		for(String s:first_names)
			if (s.length()>3)
			if(passordl.contains(s))
				return false;
		for(String s:last_names)
			if (s.length()>3)
			if(passordl.contains(s))
				return false;

			return true;
	}
	/** password checker :
	 * - strong check of password 
	 * - don't contain a first name even if you replace some letter by special letter.
	 * - don't contain a last name even if you replace some letter by special letter.
	 * to return true
	 * example "P!*r=3.141592"
	 *  */
	public static boolean checkVeryStrongPassWord(String passordl)
	{
		if(!checkStrongPassWord( passordl))
			return false;
		passordl=passordl.toLowerCase();
		String[] first_names = JavaUtils.read("src/com/zoubworld/java/utils/security/first_names.all.txt").split("\n");
		String[] last_names = JavaUtils.read("src/com/zoubworld/java/utils/security/last_names.all.txt").split("\n");
		
		passordl=passordl.replace('!', 'i').replace('@', 'a').replace('�', 'a');
		passordl=passordl.replace('&', 'e').replace('�', 'e').replace('�', 'e');
		passordl=passordl.replace('�', 'e').replace('0', 'o').replace('�', 'u');
		passordl=passordl.replace('�', 'u').replace('$', 's').replace('%', 'x');
		passordl=passordl.replace('|', 'i').replace('�', 'c').replace('�', 'l');
		passordl=passordl.replace('1', 'l').replace('5', 's').replace('9', 'g');
		for(String s:first_names)
			if (s.length()>3)
			if(passordl.contains(s))
				return false;
		for(String s:last_names)
			if (s.length()>3)
			if(passordl.contains(s))
				return false;
		return true;
		
	}
	/** password checker :
	 * it should have 
	 * - more than 12 character
	 * - a letter  lower case
	 * - a letter  upper case
	 * - a number
	 * - a special character : &�"'(-�_��)=}]@^\`|[{#&
	 * - more than 7 different character
	 * to return true
	 * example "Pierre=3.141592"
	 * */
	public static boolean checkPassWord(String passord)
	{
		if (passord.length()<11)
			return false;
		if (!passord.matches(".*\\d+.*"))// a number
			return false;
		if (!passord.matches(".*\\w+.*"))// a letter
			return false;
		if (!passord.matches(".*[a-z]+.*"))// a letter lower case
			return false;
		if (!passord.matches(".*[A-Z]+.*"))// a letter upper case
			return false;
		if (!passord.matches(".*[~<>,;:!?/\\.\\$+-�\\*/�&�\"'\\(-�_��\\)=~#\\{\\[\\|`\\\\^@\\]\\}]+.*"))// a special char
			return false;
		Set<Byte> s=new HashSet<Byte>();
		for(byte b:passord.getBytes())
			s.add(b);
		if (s.size()<8)// 8 different character
			return false;
		
					return true;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
