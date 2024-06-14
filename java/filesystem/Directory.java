/**
 * 
 */
package com.zoubworld.java.filesystem;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Directory {

	List<DirectoryEntry> contain=null;
	DirectoryEntry me=null;
	DirectoryEntry parent=null;
	
	/**
	 * 
	 */
	public Directory() {
		contain=new ArrayList<DirectoryEntry>();
		contain.add(me);
		contain.add(parent);
		}
	public Directory(DirectoryEntry me,
	DirectoryEntry parent) {
		contain=new ArrayList<DirectoryEntry>();
		contain.add(this.me=me);
		contain.add(this.parent=parent);
		}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
