package com.zoubworld.java.filesystem.myFS;

import com.zoubworld.java.filesystem.Cluster;

public class Disk {

	public Disk() extends LocalClusterTable{
		// TODO Auto-generated constructor stub
		Cluster data[]=new Cluster[99];
		LocalClusterTable B0;
		Cluster bootDir=data[1];//NodeIdAllocation
		Cluster bootDir=data[2];
		Cluster rootDir=data[3];//DirectoryEntry
	}

}
