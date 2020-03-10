package com.zoubworld.java.utils.cryptography;
/** 
 * This interface describe an algo to perform cryptographie on int only.
 * if you have a smaller type complete with bits at 0
 * the data must be provided in good order. the algo is order dependant
 * */
public interface ICryptoAlgo {

	//void buildkeys(int mykey, int keysize);
	/** this reset the algo, so after we start from 1st data
	 * */
	void reset();
	/** it convert the current data based on the past since last reset().
	 * this do a encryption.
	 * it return null in case of empty data return like during compression of data
	 * */
	Integer encrypt(int data);
	/** it insert data in case of redundancy insertion or compression with a ration upper 100%
	 * */
	Integer insert();
	/** it convert the current data based on the past since last reset().
	 *  this do a decryption.
	 *  it return null in case of empty data return like during removing redundancy.
	 * */
	Integer decrypt(int data);

}