/**
 * 
 */
package com.zoubworld.java.utils.cryptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.zoubworld.java.utils.cryptography.Enigma.*;
import com.zoubworld.java.utils.security.PassWordChecker;

/**
 * @author Pierre
 *
 */
public class EnigmaAlgo implements ICryptoAlgo {

	Machine M;
	Reflector reflectorB;
	Reflector reflectorC;
	Rotor rotor1;
	Rotor rotor2;
	Rotor rotor3;
	Rotor rotor4;
	Rotor rotor5;
	Rotor rotor6;
	Rotor rotor7;
	Rotor rotor8;

	Reflector getReflectorFromInt(int s) {
		Reflector r;
		if (s % 2 == 0) {
			r = reflectorB;
		} else if (s % 2 == 1) {
			r = reflectorC;
		} else {
			return null;
		}
		return r;
	}

	/**
	 * create an enigma machine for byte code based on a char password. string
	 * should be 12 char or longer.
	 */
	public void configureBin(Machine M, String passord) {

		if (!PassWordChecker.checkPassWord(passord))
			return;
		byte bytes[] = passord.getBytes();

		Checksum checksum = new CRC32();

		// update the current checksum with the specified array of bytes
		checksum.update(bytes, 0, bytes.length);

		// get the current checksum value
		int crc = (int) checksum.getValue();

		int key = crc ^ (crc >> 16);// crc(passord);

		// getInstance() method is called with algorithm SHA-384
		MessageDigest md;
		byte[] germe = null;
		try {
			md = MessageDigest.getInstance("SHA-384");

			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			germe = md.digest(passord.getBytes());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		M.setRotors(getReflectorFromInt(key & 0x1), getReflectorFromInt((key >> 1) & 0x7),
				getReflectorFromInt((key >> 4) & 0x7), getReflectorFromInt((key >> 7) & 0x7));
		M.setPositions((char) (crc & 0xff), (char) ((crc >> 8) & 0xff), (char) ((crc >> 16) & 0xff),
				(char) ((crc >> 24) & 0xff));
		// https://fr.wikipedia.org/wiki/SHA-2
		buildBinRotors(germe);
	}

	void buildBinRotors(byte[] germe) {
		Machine.CharSetSize = 256;
		int pos1 = germe[10];
		int pos2 = germe[11];
		rotor1 = Rotor.rotor(256,
				germe[0 * 4 + 0] << 24 + germe[0 * 4 + 0] << 16 + germe[0 * 4 + 0] << 8 + germe[0 * 4 + 0], germe[40]);
		rotor2 = Rotor.rotor(256,
				germe[1 * 4 + 0] << 24 + germe[1 * 4 + 0] << 16 + germe[1 * 4 + 0] << 8 + germe[1 * 4 + 0], germe[41]);
		rotor3 = Rotor.rotor(256,
				germe[2 * 4 + 0] << 24 + germe[2 * 4 + 0] << 16 + germe[2 * 4 + 0] << 8 + germe[2 * 4 + 0], germe[42]);
		rotor4 = Rotor.rotor(256,
				germe[3 * 4 + 0] << 24 + germe[3 * 4 + 0] << 16 + germe[3 * 4 + 0] << 8 + germe[3 * 4 + 0], germe[43]);
		rotor5 = Rotor.rotor(256,
				germe[4 * 4 + 0] << 24 + germe[4 * 4 + 0] << 16 + germe[4 * 4 + 0] << 8 + germe[4 * 4 + 0], germe[44]);
		rotor6 = Rotor.rotor(256,
				germe[5 * 4 + 0] << 24 + germe[5 * 4 + 0] << 16 + germe[5 * 4 + 0] << 8 + germe[5 * 4 + 0], germe[45]);
		rotor7 = Rotor.rotor(256,
				germe[6 * 4 + 0] << 24 + germe[6 * 4 + 0] << 16 + germe[6 * 4 + 0] << 8 + germe[6 * 4 + 0], germe[46]);
		rotor8 = Rotor.rotor(256,
				germe[7 * 4 + 0] << 24 + germe[7 * 4 + 0] << 16 + germe[7 * 4 + 0] << 8 + germe[7 * 4 + 0], germe[47]);
		reflectorB = Reflector.make(256,
				germe[8 * 4 + 0] << 24 + germe[8 * 4 + 0] << 16 + germe[8 * 4 + 0] << 8 + germe[8 * 4 + 0]);
		reflectorC = Reflector.make(256,
				germe[9 * 4 + 0] << 24 + germe[9 * 4 + 0] << 16 + germe[9 * 4 + 0] << 8 + germe[9 * 4 + 0]);
	}

	/**
	 * 
	 */
	public EnigmaAlgo(String passord) {
		M = new Machine();
		this.passord = passord;
		configureBin(M, passord);
	}

	private String passord;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.ICryptoAlgo#reset()
	 */
	@Override
	public void reset() {
		M = new Machine();
		configureBin(M, passord);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.ICryptoAlgo#encrypt(int)
	 */
	@Override
	public Integer encrypt(int data) {
		return M.convertChar((char) (data & 0xff)) | M.convertChar((char) ((data >> 8) & 0xff)) << 8
				| M.convertChar((char) ((data >> 16) & 0xff)) << 16 | M.convertChar((char) ((data >> 24) & 0xff)) << 24;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.ICryptoAlgo#decrypt(int)
	 */
	@Override
	public Integer decrypt(int data) {
		return M.convertChar((char) ((data >> 24) & 0xff)) << 24 | M.convertChar((char) ((data >> 16) & 0xff)) << 16
				| M.convertChar((char) ((data >> 8) & 0xff)) << 8 | M.convertChar((char) (data & 0xff));
	}

	@Override
	public Integer insert() {
		return null;
	}

}
