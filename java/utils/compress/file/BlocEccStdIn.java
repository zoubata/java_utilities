package com.zoubworld.java.utils.compress.file;

public class BlocEccStdIn {

	/**
	 * factor : 
	 *  0: no ecc, one parity : 1 bit
	 *  1: one bit repaired on the block by ecc 2*n+1*ecc.size
	 *  5: one bit repaired on the block by ecc n*n+1*ecc.size
	 *  2: one bit repaired per words up to n words on the block 2*n*ecc.size
	 *  3: one bit repaired per words on the block n*n*ecc.size
	 *  4: one full word repaired on the bloc : n*2*word.size
	 *  7: 4+3 
	 * 1x : CRC32
	 *1xx : block redondancy : need 1x, 1 block repaired:block.size for k blocks in total
	 *2xx : block redondancy : need 1x, k blocks repaired:k*block.size for 2^k blocks in total
	 *
	 * example of cost for n=100,w=8; and 2^k block,k=10: 80Mo size
	 * M:cost      nb bit repaired      corruption detection
	 *  0:0.00125%  0                    too poor
	 *  1:0.0325%   1                    too poor 
	 *  5:1,56%     0                        mediun  
	 *  2:0.25%     1..100                   poor
	 *  3:12.5%     1..10000                 medium
	 *  4:2%        1..64                    medium
	 *  7:14.5%     64..10703                good
	 * 1x:0.005%    0                        high
	 *1xx:+0.1%       1..640000                very high
	 *2xx:+1%      1..10 blocks            very very high
	 * 
	 * Recommended strategy :
	 * 010 : for very good medium (<<1e-9 error) ei: HDD,optical fiber
	 * 112 : for good medium  (<1e-6 error) ei CD,DVD,BR,ADSL
	 * 215 : for poor medium  (<<1% error) ei :tape, RTC line,USBKEY
	 *
	 * */
	public BlocEccStdIn(BlocStdIn bin,Ecc ecc,int factory) {
		// TODO Auto-generated constructor stub
	}

}
