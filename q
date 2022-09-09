[1mdiff --git a/java/utils/security/PassWordChecker.java b/java/utils/security/PassWordChecker.java[m
[1mindex 0eb9485..d444952 100644[m
[1m--- a/java/utils/security/PassWordChecker.java[m
[1m+++ b/java/utils/security/PassWordChecker.java[m
[36m@@ -10,77 +10,69 @@[m [mpublic class PassWordChecker {[m
 	public PassWordChecker() {[m
 		// TODO Auto-generated constructor stub[m
 	}[m
[31m-	/** password checker :[m
[31m-	 * - basic check of password [m
[31m-	 * - don't contain a first name of more than 3 character[m
[31m-	 * - don't contain a last name of more than 3 character[m
[31m-	 * to return true[m
[31m-	 * example "P!erre=3.141592"[m
[31m-	 *  */[m
[31m-	public static boolean checkStrongPassWord(String passord)[m
[31m-	{[m
[31m-		if(!checkPassWord( passord))[m
[32m+[m
[32m+[m	[32m/**[m
[32m+[m	[32m * password checker : - basic check of password - don't contain a first name of[m
[32m+[m	[32m * more than 3 character - don't contain a last name of more than 3 character to[m
[32m+[m	[32m * return true example "P!erre=3.141592"[m
[32m+[m	[32m */[m
[32m+[m	[32mpublic static boolean checkStrongPassWord(String passord) {[m
[32m+[m		[32mif (!checkPassWord(passord))[m
 			return false;[m
 		String passordl = passord.toLowerCase();[m
 		String[] first_names = JavaUtils.read("src/com/zoubworld/java/utils/security/first_names.all.txt").split("\n");[m
 		String[] last_names = JavaUtils.read("src/com/zoubworld/java/utils/security/last_names.all.txt").split("\n");[m
[31m-		for(String s:first_names)[m
[31m-			if (s.length()>3)[m
[31m-			if(passordl.contains(s))[m
[31m-				return false;[m
[31m-		for(String s:last_names)[m
[31m-			if (s.length()>3)[m
[31m-			if(passordl.contains(s))[m
[31m-				return false;[m
[32m+[m		[32mfor (String s : first_names)[m
[32m+[m			[32mif (s.length() > 3)[m
[32m+[m				[32mif (passordl.contains(s))[m
[32m+[m					[32mreturn false;[m
[32m+[m		[32mfor (String s : last_names)[m
[32m+[m			[32mif (s.length() > 3)[m
[32m+[m				[32mif (passordl.contains(s))[m
[32m+[m					[32mreturn false;[m
 [m
[31m-			return true;[m
[32m+[m		[32mreturn true;[m
 	}[m
[31m-	/** password checker :[m
[31m-	 * - strong check of password [m
[31m-	 * - don't contain a first name even if you replace some letter by special letter.[m
[31m-	 * - don't contain a last name even if you replace some letter by special letter.[m
[31m-	 * to return true[m
[32m+[m
[32m+[m	[32m/**[m
[32m+[m	[32m * password checker : - strong check of password - don't contain a first name[m
[32m+[m	[32m * even if you replace some letter by special letter. - don't contain a last[m
[32m+[m	[32m * name even if you replace some letter by special letter. to return true[m
 	 * example "P!*r=3.141592"[m
[31m-	 *  */[m
[31m-	public static boolean checkVeryStrongPassWord(String passordl)[m
[31m-	{[m
[31m-		if(!checkStrongPassWord( passordl))[m
[32m+[m	[32m */[m
[32m+[m	[32mpublic static boolean checkVeryStrongPassWord(String passordl) {[m
[32m+[m		[32mif (!checkStrongPassWord(passordl))[m
 			return false;[m
[31m-		passordl=passordl.toLowerCase();[m
[32m+[m		[32mpassordl = passordl.toLowerCase();[m
 		String[] first_names = JavaUtils.read("src/com/zoubworld/java/utils/security/first_names.all.txt").split("\n");[m
 		String[] last_names = JavaUtils.read("src/com/zoubworld/java/utils/security/last_names.all.txt").split("\n");[m
[31m-		[m
[31m-		passordl=passordl.replace('!', 'i').replace('@', 'a').replace('à', 'a');[m
[31m-		passordl=passordl.replace('&', 'e').replace('é', 'e').replace('è', 'e');[m
[31m-		passordl=passordl.replace('ê', 'e').replace('0', 'o').replace('ù', 'u');[m
[31m-		passordl=passordl.replace('µ', 'u').replace('$', 's').replace('%', 'x');[m
[31m-		passordl=passordl.replace('|', 'i').replace('ç', 'c').replace('£', 'l');[m
[31m-		passordl=passordl.replace('1', 'l').replace('5', 's').replace('9', 'g');[m
[31m-		for(String s:first_names)[m
[31m-			if (s.length()>3)[m
[31m-			if(passordl.contains(s))[m
[31m-				return false;[m
[31m-		for(String s:last_names)[m
[31m-			if (s.length()>3)[m
[31m-			if(passordl.contains(s))[m
[31m-				return false;[m
[32m+[m
[32m+[m		[32mpassordl = passordl.replace('!', 'i').replace('@', 'a').replace('à', 'a');[m
[32m+[m		[32mpassordl = passordl.replace('&', 'e').replace('é', 'e').replace('è', 'e');[m
[32m+[m		[32mpassordl = passordl.replace('ê', 'e').replace('0', 'o').replace('ù', 'u');[m
[32m+[m		[32mpassordl = passordl.replace('µ', 'u').replace('$', 's').replace('%', 'x');[m
[32m+[m		[32mpassordl = passordl.replace('|', 'i').replace('ç', 'c').replace('£', 'l');[m
[32m+[m		[32mpassordl = passordl.replace('1', 'l').replace('5', 's').replace('9', 'g');[m
[32m+[m		[32mfor (String s : first_names)[m
[32m+[m			[32mif (s.length() > 3)[m
[32m+[m				[32mif (passordl.contains(s))[m
[32m+[m					[32mreturn false;[m
[32m+[m		[32mfor (String s : last_names)[m
[32m+[m			[32mif (s.length() > 3)[m
[32m+[m				[32mif (passordl.contains(s))[m
[32m+[m					[32mreturn false;[m
 		return true;[m
[31m-		[m
[32m+[m
 	}[m
[31m-	/** password checker :[m
[31m-	 * it should have [m
[31m-	 * - more than 12 character[m
[31m-	 * - a letter  lower case[m
[31m-	 * - a letter  upper case[m
[31m-	 * - a number[m
[31m-	 * - a special character : &é"'(-è_çà)=}]@^\`|[{#&[m
[31m-	 * - more than 7 different character[m
[31m-	 * to return true[m
[32m+[m
[32m+[m	[32m/**[m
[32m+[m	[32m * password checker : it should have - more than 12 character - a letter lower[m
[32m+[m	[32m * case - a letter upper case - a number - a special character :[m
[32m+[m	[32m * &é"'(-è_çà)=}]@^\`|[{#& - more than 7 different character to return true[m
 	 * example "Pierre=3.141592"[m
[31m-	 * */[m
[31m-	public static boolean checkPassWord(String passord)[m
[31m-	{[m
[31m-		if (passord.length()<11)[m
[32m+[m	[32m */[m
[32m+[m	[32mpublic static boolean checkPassWord(String passord) {[m
[32m+[m		[32mif (passord.length() < 11)[m
 			return false;[m
 		if (!passord.matches(".*\\d+.*"))// a number[m
 			return false;[m
[36m@@ -90,16 +82,18 @@[m [mpublic class PassWordChecker {[m
 			return false;[m
 		if (!passord.matches(".*[A-Z]+.*"))// a letter upper case[m
 			return false;[m
[31m-		if (!passord.matches(".*[~<>,;:!?/\\.\\$+-°\\*/§&é\"'\\(-è_çà\\)=~#\\{\\[\\|`\\\\^@\\]\\}]+.*"))// a special char[m
[32m+[m		[32mif (!passord.matches(".*[~<>,;:!?/\\.\\$+-°\\*/§&é\"'\\(-è_çà\\)=~#\\{\\[\\|`\\\\^@\\]\\}]+.*"))// a special[m
[32m+[m																										[32m// char[m
 			return false;[m
[31m-		Set<Byte> s=new HashSet<Byte>();[m
[31m-		for(byte b:passord.getBytes())[m
[32m+[m		[32mSet<Byte> s = new HashSet<Byte>();[m
[32m+[m		[32mfor (byte b : passord.getBytes())[m
 			s.add(b);[m
[31m-		if (s.size()<8)// 8 different character[m
[32m+[m		[32mif (s.size() < 8)// 8 different character[m
 			return false;[m
[31m-		[m
[31m-					return true;[m
[32m+[m
[32m+[m		[32mreturn true;[m
 	}[m
[32m+[m
 	public static void main(String[] args) {[m
 		// TODO Auto-generated method stub[m
 [m
[1mdiff --git a/java/utils/security/securityTest.java b/java/utils/security/securityTest.java[m
[1mindex 778b305..984885d 100644[m
[1m--- a/java/utils/security/securityTest.java[m
[1m+++ b/java/utils/security/securityTest.java[m
[36m@@ -59,14 +59,14 @@[m [mpublic class securityTest {[m
 		assertTrue(!PassWordChecker.checkPassWord("P!erre000000"));[m
 		assertTrue(!PassWordChecker.checkPassWord("Pierre00000!"));[m
 		assertTrue(!PassWordChecker.checkPassWord("Pierre00000!"));[m
[31m-		[m
[32m+[m
 		assertTrue(PassWordChecker.checkPassWord("Pierre01234!"));[m
[31m-		[m
[32m+[m
 		assertTrue(!PassWordChecker.checkStrongPassWord("Dupont01234!"));[m
 		assertTrue(!PassWordChecker.checkStrongPassWord("Pierre01234!"));[m
 		assertTrue(PassWordChecker.checkStrongPassWord("P!erre01234i"));[m
 		assertTrue(PassWordChecker.checkStrongPassWord("P!erre=3.141592"));[m
[31m-		[m
[32m+[m
 		assertTrue(!PassWordChecker.checkVeryStrongPassWord("P!erre01234i"));[m
 		assertTrue(!PassWordChecker.checkVeryStrongPassWord("P!erre000000"));[m
 		assertTrue(!PassWordChecker.checkVeryStrongPassWord("Pierre01234!"));[m
[36m@@ -74,7 +74,7 @@[m [mpublic class securityTest {[m
 		assertTrue(!PassWordChecker.checkVeryStrongPassWord("Dµpont01234!"));[m
 		assertTrue(PassWordChecker.checkVeryStrongPassWord("/*-$Po12yhbd"));[m
 		assertTrue(PassWordChecker.checkVeryStrongPassWord("P!*r=3.141592"));[m
[31m-		[m
[32m+[m
 	}[m
 [m
 }[m
