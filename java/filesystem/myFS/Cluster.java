package com.zoubworld.java.filesystem.myFS;

public class Cluster {
	Long type;//
	Long addrMsb;//0..2^64
	Long sizeCluster;//0..2^64
	Byte data[];//List<DirectoryEntry>
	Byte[] ReedSolomon=null;
	public Cluster() {
		// TODO Auto-generated constructor stub
	}
}
