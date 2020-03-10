package com.zoubworld.java.utils.cryptography;

public class todo {
/** AES256/RSA
 * 
 * "Honey Encryption"
 * */
	public todo() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * It seems like no matter how much companies may try to up their defenses, there will always be some industrious young hacker who manages to evade every roadblock in his way. One team of researchers, though, think they may have finally found a way to turn their defense into an attack on the hackers themselves—by spewing fake data at them and sending them drowning.
Target Confirms That Encrypted PINs Were Swiped in Black Friday Hack

After admitting yesterday that some encrypted data had been pulled by the hack potentially…
Read more

Currently, hackers will often use software that decrypts encrypted data by guessing hundred of thousands of potential keys. So anytime an incorrect key is tried, the hackers are left with an incomprehensible mess that is distinctly not data and a clear indicator that the key or password was wrong.

Ari Juels, previous chief scientist at computer security company RSA, and Thomas Ristenpart of the University of Wisconsin worked together to develop a different type of encryption device with a twist; any time an incorrect password or encryption key is guessed, the system responds by delivering fake data to the intruder. The string resembles the actual data to the point that attackers won't be able to tell what is and isn't real. So even if/when the hacker guesses the actual password, the real information will be completely lost amongst the mass of bogus data surrounding it.

This doesn't mean that the new Honey Encryption method is totally foolproof, though. As MIT Tech Review notes:

    Hristo Bojinov, CEO and founder of mobile software company Anfacto, who has previously worked on the problem of protecting password vaults as a security researcher, says Honey Encryption could help reduce their vulnerability. But he notes that not every type of data will be easy to protect this way since it's not always possible to know the encrypted data in enough detail to produce believable fakes. "Not all authentication or encryption systems yield themselves to being 'honeyed.'"

Still, if Honey Encryption works like its creators intend, this will definitely make hackers' jobs infinitely harder if not entirely impossible. And with the number of mass data leaks constantly increasing, we need all the help we can get. [MIT Tech Review]
*/
/*
 1. Triple DES

Triple DES was designed to replace the original Data Encryption Standard (DES) algorithm, which hackers eventually learned to defeat with relative ease. At one time, Triple DES was the recommended standard and the most widely used symmetric algorithm in the industry.

Triple DES uses three individual keys with 56 bits each. The total key length adds up to 168 bits, but experts would argue that 112-bits in key strength is more like it.

Despite slowly being phased out, Triple DES still manages to make a dependable hardware encryption solution for financial services and other industries.
2. RSA

RSA is a public-key encryption algorithm and the standard for encrypting data sent over the internet. It also happens to be one of the methods used in our PGP and GPG programs.

Unlike Triple DES, RSA is considered an asymmetric algorithm due to its use of a pair of keys. You’ve got your public key, which is what we use to encrypt our message, and a private key to decrypt it. The result of RSA encryption is a huge batch of mumbo jumbo that takes attackers quite a bit of time and processing power to break.
3. Blowfish

Blowfish is yet another algorithm designed to replace DES. This symmetric cipher splits messages into blocks of 64 bits and encrypts them individually.

Blowfish is known for both its tremendous speed and overall effectiveness as many claim that it has never been defeated. Meanwhile, vendors have taken full advantage of its free availability in the public domain.

Blowfish can be found in software categories ranging from e-commerce platforms for securing payments to password management tools, where it used to protect passwords. It’s definitely one of the more flexible encryption methods available.
4. Twofish

Computer security expert Bruce Schneier is the mastermind behind Blowfish and its successor Twofish. Keys used in this algorithm may be up to 256 bits in length and as a symmetric technique, only one key is needed.

Twofish is regarded as one of the fastest of its kind, and ideal for use in both hardware and software environments. Like Blowfish, Twofish is freely available to anyone who wants to use it. As a result, you’ll find it bundled in encryption programs such as PhotoEncrypt, GPG, and the popular open source software TrueCrypt.
5. AES

The Advanced Encryption Standard (AES) is the algorithm trusted as the standard by the U.S. Government and numerous organizations.

Although it is extremely efficient in 128-bit form, AES also uses keys of 192 and 256 bits for heavy duty encryption purposes.

AES is largely considered impervious to all attacks, with the exception of brute force, which attempts to decipher messages using all possible combinations in the 128, 192, or 256-bit cipher. Still, security experts believe that AES will eventually be hailed the de facto standard for encrypting data in the private sector.
*/
}
