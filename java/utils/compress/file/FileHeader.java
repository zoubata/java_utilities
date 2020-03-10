package com.zoubworld.java.utils.compress.file;

public class FileHeader {

	byte signature[];//[4] '0','v','\r','\n','\1A'
//status :
	boolean Redundancy=false;
		byte RedunAlgo;//[7:4]:algo ; [3:0] version
		//PAR, XOR,Reed–Solomon.....
		long param;//the parameter associated to the algo.
	boolean Encrypted=false;
		// when encryption is enable the encryption is done block by block, to allow to decrypt just a block.
		byte EncryptAlgo;//[7:4]:algo ; [3:0] version
		//AES256,AES128 
		//DES
		//3DES
		//RC4
		//CAST
		//CrcCipher
		//enigma
		//Blowfish
		//Twofish
		//Serpent
		//Square
		boolean SeedUsed=true;//used a seed to protect password from analytic attack by inserting on hash a random number known that will be different on each use of password.
			int seed;// germ used to generate the keys this is a random number.
		boolean KeyStretching=true;// The key is generated from a user-supplied passphrase using an algorithm based on the SHA-256 hash function. The SHA-256 is executed 218 (262144) times,[4] which causes a significant delay on slow PCs before compression or extraction starts. This technique is called key stretching and is used to make a brute-force search for the passphrase more difficult. Current GPU-based, and custom hardware attacks limit the effectiveness of this particular method of key stretching,[5] so it is still important to choose a strong password. 
			//SHA256 x2^12
			//CRC x12
		boolean KeyMultiplication=true;// generate several key on the file. 
		boolean HeaderEncrypted=false;// if true also the header from this point is encrypted like a block.also the tearoff is encrypted on this case, so intigrity of archived can't be check without the password.
	
	boolean Bincompressed=false;
		byte BinCompressAlgo;//[15:6]:algo ; [5:0] version
		//LZMA lZMA2 BZip2 PPMd 
		//DEFLATE LZ77+huff
		//BWT
	boolean SymCompressed=false;
	boolean solid=false;// the archive is solid by block defaulty, it can be on the whole archieve.
	boolean multiPartArchive=false;
		int PartNumber=0;//index of this file.
	boolean jarEmbeded=false;
		int JarBlockIndex;// where we can find the jar to uncompress the file.
	boolean fatEmbeded=false;// is there a File Allocation Table,
			//this is the last test block, if just 1 file, there isn't
		long fatBlockBitIndex;// where we can find the Fat block.
		//a specific syntax exist for this bit stream.
		// it contains the file property but not the data itself.
		// file's data are identify by a block id[32] and a bit index[32] inside the block.
		// block is located by a bit index[64] inside the overall file. last block is a 0 length block, so length is index(b+1)-index(b)
		// each block are encrypted independently with a different key.
		// block max size is 2^64 bit(including header/tearoff).
		// fat can include dummy block like an old fat block, a stickbit dummy identify them.
	
	
		boolean tmp=false;
	boolean CheckSumByBlock=false;
		byte CheckSumAlgoByBlock;//[7:2]algo; [1:0] version //MD5/SHA1/SHA256/SHA512/CRC32/CRC64/Adler32/RIPEMD-160/XOR/BLAKE2(https://en.wikipedia.org/wiki/BLAKE_(hash_function)#BLAKE2)/Authenticode Signature(https://en.wikipedia.org/wiki/Code_signing)
	boolean GlobalCheckSum=false;
		byte GlobalCheckSumAlgo;//[7:2]algo; [1:0] version //MD5/SHA1/SHA256/SHA512/CRC16/CRC32/CRC64/Adler32/RIPEMD-160/XOR/BLAKE2(https://en.wikipedia.org/wiki/BLAKE_(hash_function)#BLAKE2)/Authenticode Signature(https://en.wikipedia.org/wiki/Code_signing)
		int globalCrc=0;
		Long Bitsize;//bit index of where is the tearoff of file.
	
		class 	DataBlock{
		//if encryption is enable, all stream of data are encrypted
		//if redundancy is enable, the redundancy is apply on encrypted data.
		
	Long Bitsize;//size in bit of block including all.
	//byte flag;
	boolean Symbolcompresion=false;
		boolean MultiSymbolcompresion=false;
			byte SymAlgoCount=1;
		byte SymAlgo[/*SymAlgoCount*/];//see SymbolAlgo.table
	boolean Binarycompresion=false;
		byte BinAlgoCount;
	byte BinAlgo[];
			
	IBinaryReader Bin; //the stream of data, with any kind of compression.
	byte CheckSum[];//size is function of CheckSumAlgoByBlock : 4..
	}
	/** this class present the data segment of a dataBlock
	 * */
	class 	FatBlock {
		int blockCount=1;// including the fat/last one.
		long blockBitIndex[];// location of block inside the bit stream of archive
		byte blockStatus[];//bit 0 : dummy, others=0;
		
		//for files
		long fileCount=0;
		short propertysize=0;// in byte
		//files are ordered like the bit stream.
		int fileBlockid[];//location of files
		long fileBitIndex[];//location of files
		long fileProperty[][];
		
		long endOfString;//byte index
		String filename[];//for each file, the full qualified path, ending with \0.
		
	}
	public FileHeader() {
		// TODO Auto-generated constructor stub
	}

}
