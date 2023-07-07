package com.zoubworld.Crypto.server.blockChain;


/** memory prepresentation of a block.
it could be store as binary: .bin, as text(bin->HexaText+\n), as gz, bz2


https://en.bitcoin.it/wiki/Block_hashing_algorithm
https://ethereum.org/en/developers/docs/blocks/

*/
class ABlock 
{
	Class ShaData
	{
		byte data=new char[32]
	}
	int MagicWord=0xD9B4BEF9;//0xD9B4BEF9=bitcoin
	int blockVersion=0x0001;
	byte noncePOW[]=new char[32];	
	/** block number N 
	*/
	long blockNumber;
	/** id of the subblock, should be below or equl to NSubBlock
	if 0 we are on the main chain.
	if 1, field period,difficulty, can be updated for the shaded chain.
	if 	NSubBlock : last block of shade, chain must be closed on a next  block of root chain by storing (blockNumber,NSubBlock,ShaBlockNk[NSubBlock]) into data.
	*/
	long SubBlockNumber=0;
	/** java GMT time stamp 
	*/
	long timestamp
	/** nonce used for ShaPreviousBlock, it is the ShaBlockN(N-1)
	L1
	*/
	ShaData nonceSha;
	/** numbit of zero requested to POW*/
	int difficulty=10;
	/** the targated time interval between to block, difficulty is updated based on SMA100 of real interval.
	
	*/
	int period=5*60;
	
	/** Account ID of miner of this block
	*/
	AccountID miner;
	/** Account ID of vallidator of this block
	*/
	AccountID validator[Nsig];
	/** size of usefull data is between 0 byte and 32MB, in power of 2, the filling pattern is 0x00.00
	the size is define depending on the queue size. if data in queue is Nq,the  size will be close to Nq/4, can't be upper than Nq, and lower than Nq/16.
	*/
	long datasize=1MB;
	/** number of sub block starting from this point to create a shard
	0 none,
	-1 : infinit 
	*/
	long NSubBlock=0;
	/** deep of the proof of history
	*/
	long Nsha=10;
	/** number of validator, it should be odd.
	*/
	long Nsig=4+1;
	
	
	
	/** sha of 12 previous block, N-2^k.
	if the block not exist 0x00..00
	ShaPreviousBlock[k]=SHA(nonceSha+block(N-2^k)) 
	
	L3
	*/
	ShaData ShaPreviousBlock[Nsha];//n-1,n-2,n-4,n-8,n-16,n-32,n-64,n-128,n-256,n-512,n-1024,n-2048
	/** usefull data*/
	byte data[]
	/** signature by validator
	number of validator is link to TVL versus amount on data.
	L3 */
	SigData signature[Nsig];
	/** POW is done in the way to have a lot of zero, for that noncePOW is changed
	L2 */
	ShaData POW;
	
	/** integrity of the block : 
	L1 */
	ShaData ShaBlockN;
	
}
/* security comes from 
- L1 the daisy chain with hash.
- L2 the proof of work that avoid to generate fastly a block.
- L3 the signature of the validator (POS) Proof of stake
- L4 history proof


*/

a year= 31 536 000 s

105 120 blocks.
